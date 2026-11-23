package app.hazen.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.WindowManager
import android.webkit.ConsoleMessage
import android.webkit.JavascriptInterface
import android.webkit.PermissionRequest
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.webkit.WebViewAssetLoader
import java.io.OutputStream
import org.json.JSONArray

/** Hosts the web camera app in a full-screen WebView and saves captures to the phone's gallery. */
class MainActivity : ComponentActivity() {
    private lateinit var web: WebView
    private var pending: PermissionRequest? = null
    private var fileCb: ValueCallback<Array<Uri>>? = null

    // lets the page's Import button open the phone's photo/video picker
    private val pickFile = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { r ->
        fileCb?.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(r.resultCode, r.data)); fileCb = null
    }

    private val askPerms = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        pending?.let { answer(it) }; pending = null
    }

    private fun has(p: String) = ContextCompat.checkSelfPermission(this, p) == PackageManager.PERMISSION_GRANTED

    private fun neededFor(r: PermissionRequest) = r.resources.mapNotNull {
        when (it) {
            PermissionRequest.RESOURCE_VIDEO_CAPTURE -> Manifest.permission.CAMERA
            PermissionRequest.RESOURCE_AUDIO_CAPTURE -> Manifest.permission.RECORD_AUDIO
            else -> null
        }
    }

    private fun answer(r: PermissionRequest) {
        val ok = r.resources.filter {
            when (it) {
                PermissionRequest.RESOURCE_VIDEO_CAPTURE -> has(Manifest.permission.CAMERA)
                PermissionRequest.RESOURCE_AUDIO_CAPTURE -> has(Manifest.permission.RECORD_AUDIO)
                else -> false
            }
        }
        if (ok.isEmpty()) r.deny() else r.grant(ok.toTypedArray())
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        val loader = WebViewAssetLoader.Builder()
            .addPathHandler("/assets/", WebViewAssetLoader.AssetsPathHandler(this))
            .build()

        web = WebView(this).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.mediaPlaybackRequiresUserGesture = false
            settings.allowFileAccess = false
            settings.allowContentAccess = true
            addJavascriptInterface(Bridge(applicationContext), "CarCamAndroid")
            webViewClient = object : WebViewClient() {
                override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse? {
                    val res = loader.shouldInterceptRequest(request.url) ?: return null
                    // The bundled AI engine needs exact MIME types (WebAssembly streaming, ES modules)
                    val path = request.url.path ?: return res
                    val mime = when {
                        path.endsWith(".wasm") -> "application/wasm"
                        path.endsWith(".js") -> "text/javascript"
                        path.endsWith(".task") || path.endsWith(".tflite") -> "application/octet-stream"
                        else -> null
                    }
                    if (mime != null) res.mimeType = mime
                    return res
                }

                override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                    if (request.url.host == "appassets.androidplatform.net") return false
                    startActivity(Intent(Intent.ACTION_VIEW, request.url)); return true
                }
            }
            webChromeClient = object : WebChromeClient() {
                override fun onPermissionRequest(request: PermissionRequest) {
                    runOnUiThread {
                        val missing = neededFor(request).filter { !has(it) }
                        if (missing.isEmpty()) answer(request) else { pending = request; askPerms.launch(missing.toTypedArray()) }
                    }
                }
                override fun onConsoleMessage(m: ConsoleMessage): Boolean {
                    Log.d("Hazen", "${m.message()} (${m.sourceId()}:${m.lineNumber()})"); return true
                }
                override fun onShowFileChooser(view: WebView, cb: ValueCallback<Array<Uri>>, params: FileChooserParams): Boolean {
                    fileCb?.onReceiveValue(null); fileCb = cb
                    return try { pickFile.launch(params.createIntent()); true } catch (e: Exception) { fileCb = null; false }
                }
            }
        }
        WebView.setWebContentsDebuggingEnabled((applicationInfo.flags and android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE) != 0)   // debug builds only
        setContentView(web)
        web.loadUrl("https://appassets.androidplatform.net/assets/index.html")
    }

    override fun onResume() { super.onResume(); web.onResume() }
    override fun onPause() { web.onPause(); super.onPause() }
}

/** Called from the page: streams a captured photo/video into the gallery (Pictures/Hazen, Movies/Hazen). */
class Bridge(private val ctx: Context) {
    private var out: OutputStream? = null
    private var uri: Uri? = null

    @JavascriptInterface
    fun begin(name: String, mime: String): Boolean {
        val video = mime.startsWith("video")
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, mime)
            put(MediaStore.MediaColumns.RELATIVE_PATH, (if (video) Environment.DIRECTORY_MOVIES else Environment.DIRECTORY_PICTURES) + "/Hazen")
            put(MediaStore.MediaColumns.IS_PENDING, 1)
        }
        val collection = if (video) MediaStore.Video.Media.EXTERNAL_CONTENT_URI else MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        uri = ctx.contentResolver.insert(collection, values)
        out = uri?.let { ctx.contentResolver.openOutputStream(it) }
        return out != null
    }

    @JavascriptInterface
    fun chunk(base64: String) { out?.write(Base64.decode(base64, Base64.DEFAULT)) }

    @JavascriptInterface
    fun end(): String {
        out?.close(); out = null
        uri?.let { ctx.contentResolver.update(it, ContentValues().apply { put(MediaStore.MediaColumns.IS_PENDING, 0) }, null, null) }
        return uri?.toString() ?: ""
    }

    /** Opens the system share sheet for one saved photo/video. */
    @JavascriptInterface
    fun share(uri: String, mime: String) {
        val u = Uri.parse(uri)
        val send = Intent(Intent.ACTION_SEND).apply {
            type = mime
            putExtra(Intent.EXTRA_STREAM, u)
            clipData = ClipData.newRawUri("", u)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        ctx.startActivity(Intent.createChooser(send, "Share").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    /** Share several items at once; [json] is a JSON array of content URIs. */
    @JavascriptInterface
    fun shareMany(json: String, mime: String) {
        val arr = JSONArray(json)
        val uris = ArrayList<Uri>()
        for (i in 0 until arr.length()) uris.add(Uri.parse(arr.getString(i)))
        if (uris.isEmpty()) return
        val clip = ClipData.newRawUri("", uris[0])
        for (i in 1 until uris.size) clip.addItem(ClipData.Item(uris[i]))
        val send = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
            type = mime
            putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
            clipData = clip
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        ctx.startActivity(Intent.createChooser(send, "Share").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    /** Deletes a file this app saved (used when an item is deleted from the Album). */
    @JavascriptInterface
    fun remove(uri: String) {
        try { ctx.contentResolver.delete(Uri.parse(uri), null, null) } catch (_: Exception) {}
    }
}
