const C='hazen-2',M='hazen-models-v1';
self.addEventListener('install',e=>{e.waitUntil(caches.open(C).then(c=>c.addAll(['./','demo.html','index.html','manifest.webmanifest'])));self.skipWaiting()});
self.addEventListener('activate',e=>e.waitUntil(caches.keys().then(ks=>Promise.all(ks.filter(k=>k!==C&&k!==M).map(k=>caches.delete(k)))).then(()=>clients.claim())));
self.addEventListener('fetch',e=>{
  const r=e.request,u=new URL(r.url);
  if(r.method!=='GET'||u.origin!==location.origin)return;
  /* AI models: saved once, served from the device after that */
  if(u.pathname.includes('/models/')){
    if(u.pathname.endsWith('manifest.json')){e.respondWith(caches.open(M).then(c=>fetch(r).then(n=>{if(n.ok)c.put(r,n.clone());return n}).catch(()=>c.match(r))));return}
    e.respondWith(caches.open(M).then(async c=>{const hit=await c.match(r);if(hit)return hit;const n=await fetch(r);if(n.ok)c.put(r,n.clone());return n}));return}
  e.respondWith(fetch(r).then(n=>{const c=n.clone();caches.open(C).then(x=>x.put(r,c));return n}).catch(()=>caches.match(r)));
});
