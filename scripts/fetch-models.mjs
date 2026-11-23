// Downloads the on-device AI engine and models once, at build time, so installed apps
// carry them inside the package and never download them again.
// Usage: node scripts/fetch-models.mjs <output-folder>
import { mkdir, writeFile } from 'node:fs/promises';
import { dirname, join } from 'node:path';

const out = process.argv[2] || 'models';
const V = '0.10.14';
const CDN = `https://cdn.jsdelivr.net/npm/@mediapipe/tasks-vision@${V}`;
const G = 'https://storage.googleapis.com/mediapipe-models';
const groups = [
  { id: 'engine', label: 'AI engine', files: [
    { p: 'vision_bundle.js', u: `${CDN}/vision_bundle.mjs` },
    { p: 'wasm/vision_wasm_internal.js', u: `${CDN}/wasm/vision_wasm_internal.js`, v: 'simd' },
    { p: 'wasm/vision_wasm_internal.wasm', u: `${CDN}/wasm/vision_wasm_internal.wasm`, v: 'simd' },
    { p: 'wasm/vision_wasm_nosimd_internal.js', u: `${CDN}/wasm/vision_wasm_nosimd_internal.js`, v: 'nosimd' },
    { p: 'wasm/vision_wasm_nosimd_internal.wasm', u: `${CDN}/wasm/vision_wasm_nosimd_internal.wasm`, v: 'nosimd' } ] },
  { id: 'face', label: 'Face tools', files: [
    { p: 'face_landmarker.task', u: `${G}/face_landmarker/face_landmarker/float16/1/face_landmarker.task` } ] },
  { id: 'seg', label: 'Portrait cut-out', files: [
    { p: 'selfie_segmenter.tflite', u: `${G}/image_segmenter/selfie_segmenter/float16/latest/selfie_segmenter.tflite` } ] },
  { id: 'gest', label: 'Hand gestures', files: [
    { p: 'gesture_recognizer.task', u: `${G}/gesture_recognizer/gesture_recognizer/float16/1/gesture_recognizer.task` } ] }
];

async function get(url) {
  for (let i = 1; i <= 4; i++) {
    try {
      const r = await fetch(url);
      if (!r.ok) throw new Error('HTTP ' + r.status);
      const b = Buffer.from(await r.arrayBuffer());
      if (!b.length) throw new Error('empty file');
      return b;
    } catch (e) {
      if (i === 4) throw new Error(`${url}: ${e.message}`);
      await new Promise(res => setTimeout(res, 1500 * i));
    }
  }
}

let total = 0;
for (const g of groups) {
  for (const f of g.files) {
    const buf = await get(f.u);
    const dest = join(out, f.p);
    await mkdir(dirname(dest), { recursive: true });
    await writeFile(dest, buf);
    f.s = buf.length; total += buf.length;
    delete f.u;
    console.log(`  ${f.p}  ${(buf.length / 1048576).toFixed(1)} MB`);
  }
}
await writeFile(join(out, 'manifest.json'), JSON.stringify({ version: V, groups }, null, 1));
console.log(`Models ready in ${out} (${(total / 1048576).toFixed(1)} MB)`);
