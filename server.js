var express = require('express');
var ExpressPeerServer = require('peer').ExpressPeerServer;


var app = express();
var server = app.listen(9000);
var options = {
  debug: true,
  allow_discovery: true
};

var peerServer = ExpressPeerServer(server, options);
app.use(express.static('public'));
app.use('/flock', peerServer);


peerServer.on('connection', function(id) {
  console.log('client connected! ' + id);

});

server.on('disconnect', function(id) {
  console.log('client disconnected! ' + id);
});

