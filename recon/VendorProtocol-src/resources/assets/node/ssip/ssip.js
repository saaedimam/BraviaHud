/*****************************************************************************
 * Copyright 2014 Sony Corporation. All rights reserved.
 *****************************************************************************/
/*
 * Sony Simple IP Protocol Server.
 *
 * Usage: node ssip_server <log mode (Release or Debug)> <psk or token> <port number>
 */

var net = require('net');
var protocolMod = require('ssip_protocol.js');
var irccMod = require('ircc-ip');
var webapiMod = require('webapi');
var loggerMod = require('logger');

(function(){
    var API_IP = '127.0.0.1';
    var API_PORT = 80;
    var RECONNECTION_TIMEOUT = 1000;
    var MONITOR_TIMEOUT = 5000;
    var INITIALIZE_TIMEOUT = 1000;

    // setup Logger
    var lmode = process.argv[2];
    global['Logger'] = loggerMod.Logger;
    global['logger'] = loggerMod.createLogger('SSIP', lmode);

    var token = process.argv[3]; // psk
    var port = process.argv[4]; // port

    if (token == null || token == "" || port == null || port == "") {
        logger.rlog(Logger.ERROR, 'argment is null');
    }

    var socketBuf = [];
    // TV's status
    var status = {
            power: true,
            uri: '',
            channel: '',
            input: '',
            port: '',
            mute: false,
            volume: '',
            //pip: false,
            picmute: false
    };
    var irccip = irccMod.createIRCCIP(API_IP, API_PORT, token, 'SSIP Server');
    var webapi = webapiMod.createWebAPI(API_IP, API_PORT, token, 'SSIP Server');

    var SSIPProtocol = protocolMod.SSIPProtocol;

    process.on('exit', function(){
        logger.dlog(Logger.INFO, '[EVENT] process exit');
    });

    var signal_handler = function() {
        for (var i = 0; i < socketBuf.length; i++) {
            socketBuf[i].destroy();
        }
    };

    process.on('SIGTERM', function() {
        logger.rlog(Logger.INFO, '[EVENT] process SIGTERM');
        signal_handler();
        process.exit(0);
    });

    process.on('SIGINT', function() {
        logger.dlog(Logger.INFO, '[EVENT] process SIGINT');
        signal_handler();
        process.exit(0);
    });

    var sendNotify = function(mask) {
        logger.dlog(Logger.VERBOSE, '@@@@@@@@@@@@@@@ -----------> sendNotify');
        for (var i = 0; i < socketBuf.length; i++) {
            var socket = socketBuf[i];
            if (!socket['CLOSED']) {
                logger.dlog(Logger.VERBOSE, '@@@@@@@@@@@@@@@ -----------> SSIP.sendNotify');
                if (socket['PROTOCOL'] != null && socket['PROTOCOL'] != undefined) {
                    socket['PROTOCOL'].sendNotify(socket, status, mask);
                }
            }
        }
    };

    var monitor = null;
    monitor = function() {
        webapi.getPowerStatus(function(response) {
            if (response != null && response.error == undefined) {
                var power = (response.result[0].status == 'active') ? true : false;
                if (status.power != power) {
                    status.power = power;
                    logger.dlog(Logger.VERBOSE, '@@@@@@@@@@@@@@@ POWR '+SSIPProtocol.MASK_POWER);
                    sendNotify(SSIPProtocol.MASK_POWER);
                }
            }
        }, true);

        webapi.getPlayingContentInfo(function(response) {
            if (response != null && response.error == undefined) {
                var uri = response.result[0].uri;
                if (status.uri != uri) {
                    status.uri = uri;
                    var input = '';
                    var channel = '';
                    var port = '';
                    if (uri.indexOf('tv') == 0) {
                        input = 'tv';
                        port = '0';
                        channel = response.result[0].dispNum;
                        channel = parseFloat(channel);
                        channel = channel.toString(10);
                    } else {
                        if(uri.match(/extInput:([^?]*)[?]port=([0-9]+)/) != null) {
                            input = RegExp.$1;
                            port = RegExp.$2;
                            channel = '';
                        }
                    }
                    if (status.input != input || (status.input == input && status.port != port)) {
                        status.input = input;
                        status.port = port;

                        logger.dlog(Logger.VERBOSE, '@@@@@@@@@@@@@@@ INPUT (' + status.input + ') (' + status.port + ')');
                        sendNotify(SSIPProtocol.MASK_INPUT);
                    }
                    if (channel != '' && status.channel != channel) {
                        status.channel = channel;
                        logger.dlog(Logger.VERBOSE, '@@@@@@@@@@@@@@@ CHANNEL');
                        sendNotify(SSIPProtocol.MASK_CHANNEL);
                    }
                }
            }
        }, true);

        webapi.getVolumeInformation(function(response) {
            if (response != null && response.error == undefined) {
                var obj = response.result[0];
                var volume = '';
                var mute = false;
                for (var i = 0; i < obj.length; i++) {
                    if (obj[i].target == "speaker") {
                        volume = obj[i].volume;
                        mute = obj[i].mute;

                        if (status.volume != volume) {
                            status.volume = volume;
                            logger.dlog(Logger.VERBOSE, '@@@@@@@@@@@@@@@ VOLUME');
                            sendNotify(SSIPProtocol.MASK_VOLUME);
                        }
                        if (status.mute != mute) {
                            status.mute = mute;
                            logger.dlog(Logger.VERBOSE, '@@@@@@@@@@@@@@@ MUTE');
                            sendNotify(SSIPProtocol.MASK_MUTE);
                        }
                        break;
                    }
                }
            }
        }, true);

        /*
        webapi.getMultiScreenMode(function(response) {
            if (response != null && response.error == undefined) {
                var mode = response.result[0].mode;
                if (mode != null && mode != undefined) {
                    var pip = false;
                    if(mode == 'PIP') {
                        pip = true;
                    } else if(mode == 'single') {
                        pip = false;
                    } else {
                        pip = false;
                    }
                    if (status.pip != pip) {
                        status.pip = pip;
                        logger.dlog(Logger.VERBOSE, '@@@@@@@@@@@@@@@ PIP');
                        sendNotify(SSIPProtocol.MASK_PIP);
                    }
                }
            }
        }, true);
        */

        webapi.getPowerSavingMode(function(response) {
            if (response != null && response.error == undefined) {
                var mode = response.result[0].mode;
                if ( mode != null && mode != undefined) {
                    var picmute = false;
                    if(mode == 'pictureOff') {
                        picmute = true;
                    } else {
                        picmute = false;
                    }
                    if (status.picmute != picmute) {
                        status.picmute = picmute;
                        logger.dlog(Logger.VERBOSE, '@@@@@@@@@@@@@@@ PIC MUTE');
                        sendNotify(SSIPProtocol.MASK_PICMUTE);
                    }
                }
            }
        }, true);
    };

    var initialize = null;
    initialize = function(){
        webapi.setWolMode(true, function(response) {
            if (response == null || response.error != undefined) {
                logger.rlog(Logger.WARN, 'setWolMode failed!!');
                if (initialize != null) {
                    setTimeout(initialize, INITIALIZE_TIMEOUT);
                }
            }
        }, true);
    };
    initialize();

    setInterval(monitor, MONITOR_TIMEOUT);

    var num = 0;

    var server = net.createServer(function(socket){
        // socket is net.Socket instance.
        // this function is automatically set as a listener for the 'connection' event
        // Event: 'connection'
        logger.dlog(Logger.VERBOSE, '[EVENT] socket connection <= ' + num);

        socketBuf.push(socket);
        socket['CLOSED'] = false;
        socket.setEncoding('ascii');
        // DEBUG
        socket['NUM'] = num;
        ++num;

        socket['PROTOCOL'] = protocolMod.createSSIPProtocol(irccip, webapi);
        socket['DATA'] = '';

        socket.on('data', function(data) {
            ///socket.setTimeout(0);

            logger.dlog(Logger.VERBOSE, '[EVENT] socket data');
            //assert(typeof data, 'string');
            socket['PROTOCOL'].dispatch(socket, data);
        });

        socket.on('close', function(had_error) {
            logger.dlog(Logger.VERBOSE, '[EVENT] socket close => ' + socket['NUM']);
            for (var i = 0; i < socketBuf.length; i++) {
                if (socketBuf[i] == socket) {
                    logger.dlog(Logger.VERBOSE, 'socket ref is match!!');
                    socketBuf.splice(i, 1);
                }
            }
            logger.dlog(Logger.VERBOSE, '[EVENT] socket close  end');
        });

        socket.on('timeout', function() {
            logger.dlog(Logger.VERBOSE, '[EVENT] socket timeout');
            socket.destroy();
        });

        socket.on('error', function(e) {
            logger.rlog(Logger.ERROR, '[EVENT] socket error => ' + socket['NUM']);
            logger.rlog(Logger.VERBOSE, e);
        });

        socket.on('end', function(){
            logger.dlog(Logger.VERBOSE, '[EVENT] socket end => ' + socket['NUM']);
            socket['CLOSED'] = true;
        });
    });

    var listenCallback = function() {
        // Event: 'listening'
        logger.dlog(Logger.VERBOSE, '[EVENT] server listening');
    };

    // server.maxConnections = 5;

    server.listen(port, listenCallback);

    server.on('close', function() {
        // Event: 'close'
        logger.dlog(Logger.VERBOSE, '[EVENT] server close');
    });

    server.on('error', function(e) {
        if (e.code == 'EADDRINUSE') {
            logger.rlog(Logger.WARN, 'Address in use, retrying...');
            setTimeout(function() {
                server.close();
                server.listen(PORT, listenCallback);
            }, RECONNECTION_TIMEOUT);
        }
    });
})();