/*****************************************************************************
 * Copyright 2014 Sony Corporation. All rights reserved.
 *****************************************************************************/
/*
 * Simple Device Discovery Protocol Server.
 *
 * Usage: node sddp_server <log mode (Release or Debug)> <psk or token> <port number>
 */

// const
var SDDP_MULTI_ADDR = "239.255.255.250";
var SDDP_NETIF = "lm0";
var SDDP_TTL = 32;
var MIN_ADVERT_PERIOD = 60;
var DISABLE_PERIODIC_ADVERTS = 0;
var MAX_SDDP_FRAME_SIZE = 512;

// file read
var net = require('net');
var sddpDeviceMod = require('sddp_device.js');
var SddpDeviceObject = sddpDeviceMod.SddpDevice;
var sddpPacketMod = require('sddp_packet.js');
var SddpPacketObject = sddpPacketMod.SddpPacket;
var loggerMod = require('logger');

// setup Logger
var lmode = process.argv[2];
global['Logger'] = loggerMod.Logger;
global['logger'] = loggerMod.createLogger('SDDP', lmode);

var modelName = process.argv[3];
var macAddress = process.argv[4];
var ipAddress = process.argv[5];

// check argment
if (modelName == null || modelName == "" || macAddress == null || macAddress == "" || ipAddress == null || ipAddress == "") {
  logger.rlog(Logger.ERROR, "argment is null");
}

// Create SddpDevice
var sddpDevice = sddpDeviceMod.createSddpDevice(modelName, ipAddress, macAddress);

// process event
var signal_handler = function() {
  logger.dlog(Logger.DETAIL, "signal_handler");
  if (notifyAliveTimer != null) {
    clearTimeout(notifyAliveTimer);
  }
  notifyLeave();
};

process.on('SIGTERM', function () {
  logger.rlog(Logger.WARN, "SIGTERM Received");
  signal_handler();
});

process.on('SIGINT', function () {
  logger.rlog(Logger.ERROR, "SIGINT Received");
  signal_handler();
});

// Create notify socket
var dgram = require('dgram');
if (dgram == null) {
  logger.rlog(Logger.ERROR, "dgram is null");
}
var notifySocket = dgram.createSocket("udp4");
if (notifySocket == null) {
  logger.rlog(Logger.ERROR, "notifySocket is null");
}
notifySocket.on("message", function (msg, rinfo) {
  logger.dlog(Logger.DETAIL, "got: " + msg + " from " + rinfo.address);
  var sddpPacket = parseSddpPacket(msg.toString());

  if (sddpPacket.getPacketType() != SddpPacketObject.PacketType.REQUEST) {
    return;
  }

  if (sddpPacket.getVersion() != SddpPacketObject.SDDP_VERSION) {
    // unsupported
    return;
  }

  if (sddpPacket.getMethod() == "SEARCH") {
    var arg = sddpPacket.getArgument();
    var productName = sddpDevice.getProductName();
    var shortProductName = productName.substring(0, arg.length);
    var ch = productName.substr(arg.length, 1);
    if (arg == "*" || arg == productName ||
        (arg == shortProductName && ch == ":")) {
      if (!sddpDevice.isLocalOnly()) {
        logger.dlog(Logger.DETAIL, "SDDP Search target MATCH");
        sddpDevice.setSearchResponse(true);
        sddpDevice.setTran(sddpPacket.getTran());
        notifyAlive(false, rinfo.address);
        sddpDevice.setSearchResponse(false);
      }
    }
  }
});

notifySocket.on("listening", function () {
  logger.dlog(Logger.DETAIL, "notifySocket listening " + notifySocket.address().address);
});

// Notify C4 message
var notifyAliveTimer = null;
var notifyAlive = function(isMulticast, unicastAddress) {
  if (typeof isMulticast != "boolean") {
    return;
  }

  var sddpPacket = sddpPacketMod.createSddpPacket();
  sddpDevice.setNotificationType(SddpDeviceObject.NotificationType.ALIVE);
  var aliveMessage = sddpPacket.createPacketFromDevice(sddpDevice);
  var message = new Buffer(aliveMessage);
  if (isMulticast) {
    notifySocket.send(message, 0, message.length, SddpPacketObject.SDDP_PORT, SDDP_MULTI_ADDR);
    notifyAliveTimer = setTimeout(notifyAlive, sddpDevice.getAliveInterval(), true);
  } else {
    // unicast
    if (typeof unicastAddress == "string") {
      notifySocket.send(message, 0, message.length, SddpPacketObject.SDDP_PORT, unicastAddress);
    }
  }
  logger.dlog(Logger.DETAIL, "Sent " + message);
};

var notifyLeave = function() {
  // Not use message
  logger.rlog(Logger.WARN, "/$$$/ notifyLeave");
  var sddpPacket = sddpPacketMod.createSddpPacket();
  sddpDevice.setNotificationType(SddpDeviceObject.NotificationType.OFFLINE);
  sddpDevice.setSearchResponse(false);
  var leaveMessage = sddpPacket.createPacketFromDevice(sddpDevice);
  var message = new Buffer(leaveMessage);
  notifySocket.send(message, 0, message.length, SddpPacketObject.SDDP_PORT, SDDP_MULTI_ADDR, function() {
    logger.dlog(Logger.DETAIL, "Sent " + message + " to the wire...");
    notifySocket.close();
    process.exit(0);
  });
  logger.dlog(Logger.DETAIL, "Sent " + message);
};
var notifyIdentify = function() {
  // Not use message
  logger.rlog(Logger.WARN, "/$$$/ notifyIdentify");
  var sddpPacket = sddpPacketMod.createSddpPacket();
  sddpDevice.setNotificationType(SddpDeviceObject.NotificationType.IDENTIFY);
  sddpDevice.setSearchResponse(false);
  var identifyMessage = sddpPacket.createPacketFromDevice(sddpDevice);
  var message = new Buffer(identifyMessage);
  notifySocket.send(message, 0, message.length, SddpPacketObject.SDDP_PORT, SDDP_MULTI_ADDR);
  logger.dlog(Logger.DETAIL, "Sent " + message);
};

// Sddp packet parse
parseSddpPacket = function (data) {
  if (typeof data != "string") {
    return null;
  }

  logger.rlog(Logger.WARN, "/$$$/ parseSddpPacket");

  var lines = data.split("\r\n");
  var sddpPacket = sddpPacketMod.createSddpPacket();
  var index = lines[0].indexOf("SDDP");

  // First line
  if (index == -1) {
    // "SDDP" not found
    return null;
  }

  if (index == 0) {
    // Response
    logger.rlog(Logger.WARN, "/$$$/ parseSddpPacket Response");
    sddpPacket.setPacketType(SddpPacketObject.PacketType.RESPONSE);
  } else {
    // Request
    logger.rlog(Logger.WARN, "/$$$/ parseSddpPacket Request");
    sddpPacket.setPacketType(SddpPacketObject.PacketType.REQUEST);
    var separator = /[ \/]/;
    var token = lines[0].split(separator);
    sddpPacket.setMethod(token[0]);
    sddpPacket.setArgument(token[1]);
    sddpPacket.setVersion(token[3]);

    var i;
    var value = null;
    for (i = 1; i < lines.length; i++) {
      token = null;
      token = lines[i].split(" ");

      value = null;
      if (token.length == 1) {
        value = "";
      } else if (token.length == 2) {
        value = token[1].replace("\"", "");
      }

      if (token[0] == "From:") {
        sddpPacket.setFrom(value);
      } else if (token[0] == "Host:") {
        sddpPacket.setHost(value);
      } else if (token[0] == "Tran:") {
        sddpPacket.setTran(value);
      } else if (token[0] == "Max-Age:") {
        sddpPacket.setMaxAge(value);
      } else if (token[0] == "Timeout:") {
        sddpPacket.setTimeout(value);
      } else if (token[0] == "Primary-Proxy:") {
        sddpPacket.setPrimaryProxy(value);
      } else if (token[0] == "Proxies:") {
        sddpPacket.setProxies(value);
      } else if (token[0] == "Manufacturer:") {
        sddpPacket.setManufacturer(value);
      } else if (token[0] == "Model:") {
        sddpPacket.setModel(value);
      } else if (token[0] == "Driver:") {
        sddpPacket.setDriver(value);
      } else if (token[0] == "Type:") {
        sddpPacket.setType(value);
      }
    }
  }
  return sddpPacket;
}

// Initialize sddpDevice
sddpDevice.setNotificationType(SddpDeviceObject.NotificationType.NONE);
sddpDevice.setSearchResponse(false);

notifySocket.bind(SddpPacketObject.SDDP_PORT, function() {
logger.rlog(Logger.WARN, "/$$$/ notifySocket.bind start");
  notifySocket.setMulticastTTL(SDDP_TTL);
logger.rlog(Logger.WARN, "/$$$/ after setMulticast");
  notifySocket.addMembership(SDDP_MULTI_ADDR);
logger.rlog(Logger.WARN, "/$$$/ after setMembership");

  var maxAge = sddpDevice.getMaxAge();
logger.rlog(Logger.WARN, "/$$$/ maxAge="+maxAge);

  if (maxAge > DISABLE_PERIODIC_ADVERTS && maxAge < MIN_ADVERT_PERIOD) {
logger.rlog(Logger.WARN, "/$$$/ before setMaxAge");
    sddpDevice.setMaxAge(MIN_ADVERT_PERIOD);
logger.rlog(Logger.WARN, "/$$$/ after setMaxAge");
  }
logger.rlog(Logger.WARN, "/$$$/ before setAliveInterval");
  sddpDevice.setAliveInterval(((sddpDevice.getMaxAge() * 2) / 3) * 1000); // msec
logger.rlog(Logger.WARN, "/$$$/ setAliveInterval="+((((sddpDevice.getMaxAge() * 2) / 3) * 1000)));

  notifyAlive(true);
logger.rlog(Logger.WARN, "/$$$/ notifyAlive");
  
});

