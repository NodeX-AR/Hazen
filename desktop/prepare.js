const fs=require('fs'),path=require('path');fs.copyFileSync(path.join(__dirname,'..','demo.html'),path.join(__dirname,'demo.html'));console.log('demo.html copied');
