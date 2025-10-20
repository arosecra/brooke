// import required packages
const express = require('express');

const https = require('https');
const http = require('http');

const fs = require('fs');
const path = require('path');


const app = express();

// // create a route for the app
// app.get('/', (req, res) => {
//   res.send('Hello dev.to!');
// });

// // another route
// app.get('/omergulen', (req, res) => {
//   res.send('Hello Omer! Welcome to dev.to!');
// });

app.use(express.static(path.join(__dirname, '..', 'angular', 'dist', 'brooke-ui', 'browser')));

// Listen both http & https ports
const httpsServer = https.createServer({
  key: fs.readFileSync('./.cert/localhost+1-key.pem'),
  cert: fs.readFileSync('./.cert/localhost+1.pem'),
}, app);

httpsServer.listen(443, () => {
    console.log('HTTPS Server running on port 443');
});