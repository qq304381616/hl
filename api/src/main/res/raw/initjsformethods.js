(function () {
    window.XiaoTunYunJS = ( function (me) {
        var idmax = 65536;
        var id = 5000;

        function getOptionId() {
            return id = (id + 1) % idmax;
        };
        var cbMap = {};
        me.addCB = function (obj) {
            var opid = getOptionId();
            cbMap['' + opid] = obj;
            return opid;
        };
        me.addCB2ById = function (id, obj) {
            opid = id;
            cbMap['' + opid] = obj;
            return opid;
        };
        me.callCB = function (opid, res) {
            var cb = cbMap['' + opid];
            cb(res);
        };
        return me;
    }({}));
    window.XiaoTunYunJS.getNetworkType = function (cb) {
      cbGetNetworkType = function (opid, res) {
            window.XiaoTunYunJS.callCB(opid, res);
        };
        var opid = window.XiaoTunYunJS.addCB(cb);
        var paramsMap = {};
        paramsMap.opid = opid;
        window.messageHandlers.getNetworkType(JSON.stringify(paramsMap));
    };
    window.XiaoTunYunJS.getDeviceInfo = function (cb) {
        cbGetDeviceInfo = function (opid, res) {
            window.XiaoTunYunJS.callCB(opid, res);
        };
        var opid = window.XiaoTunYunJS.addCB(cb);
        var paramsMap = {};
        paramsMap.opid = opid;
        window.messageHandlers.getDeviceInfo(JSON.stringify(paramsMap));
    };
   window.XiaoTunYunJS.chooseImage = function (paramsMap) {
        cbChooseImage = function (opid, res) {
            window.XiaoTunYunJS.callCB(opid, res);
        };
        var successOpid = window.XiaoTunYunJS.addCB(paramsMap.success);
        var cancelOpid = window.XiaoTunYunJS.addCB(paramsMap.cancel);
        paramsMap.successOpid = successOpid;
        paramsMap.cancelOpid = cancelOpid;
        window.messageHandlers.chooseImage(JSON.stringify(paramsMap));
    };
   window.XiaoTunYunJS.getDataUrl = function (ids, cb) {
        cbDataUrl = function (opid, res) {
            window.XiaoTunYunJS.callCB(opid, res);
        };
        var opid = window.XiaoTunYunJS.addCB(cb);
        var paramsMap = {};
        paramsMap.ids = ids;
        paramsMap.opid = opid;
        window.messageHandlers.getDataUrl(JSON.stringify(paramsMap));
    };
    window.XiaoTunYunJS.previewImage = function (paramsMap) {
        window.messageHandlers.previewImage(JSON.stringify(paramsMap));
    };

      window.XiaoTunYunJS.uploadFile = function (paramsMap) {
            cbUploadFile = function (opid, res) {
                window.XiaoTunYunJS.callCB(opid, res);
            };
            var successOpid = window.XiaoTunYunJS.addCB(paramsMap.success);
            var failOpid = window.XiaoTunYunJS.addCB(paramsMap.fail);
            var statusOpid = window.XiaoTunYunJS.addCB(paramsMap.status);
            paramsMap.successOpid = successOpid;
            paramsMap.failOpid = failOpid;
            paramsMap.statusOpid = statusOpid;
            window.messageHandlers.uploadFile(JSON.stringify(paramsMap));
        };

      window.XiaoTunYunJS.downloadFile = function (paramsMap) {
            cbDownloadFile = function (opid, res) {
                window.XiaoTunYunJS.callCB(opid, res);
            };
            var successOpid = window.XiaoTunYunJS.addCB(paramsMap.success);
            var failOpid = window.XiaoTunYunJS.addCB(paramsMap.fail);
            var statusOpid = window.XiaoTunYunJS.addCB(paramsMap.status);
            paramsMap.successOpid = successOpid;
            paramsMap.failOpid = failOpid;
            paramsMap.statusOpid = statusOpid;
            window.messageHandlers.downloadFile(JSON.stringify(paramsMap));
        };
    window.XiaoTunYunJS.startRecord = function (p1,p2, cb) {
        cbStartRecord = function (opid, res) {
            window.XiaoTunYunJS.callCB(opid, res);
        };
        var opid = window.XiaoTunYunJS.addCB(cb);
        var paramsMap = {};
        paramsMap.opid = opid;
        window.messageHandlers.startRecord(JSON.stringify(paramsMap));
    };
    window.XiaoTunYunJS.stopRecord = function () {
        window.messageHandlers.stopRecord("");
    };
    window.XiaoTunYunJS.playAudio = function (params, cb) {
        cbPlayAudio = function (opid, res) {
            window.XiaoTunYunJS.callCB(opid, res);
        };
        var opid = window.XiaoTunYunJS.addCB(cb);
         var paramsMap = {};
        paramsMap.id = params;
        paramsMap.opid = opid;
        window.messageHandlers.playAudio(JSON.stringify(paramsMap));
    };
   window.XiaoTunYunJS.pauseAudio = function (params) {
        window.messageHandlers.pauseAudio("");
    };
   window.XiaoTunYunJS.stopAudio = function (params) {
        window.messageHandlers.stopAudio("");
    };
   window.XiaoTunYunJS.screenOrientation = function (params) {
        window.messageHandlers.screenOrientation(JSON.stringify(params));
    };
     window.XiaoTunYunJS.goScan = function (paramsMap, cb) {
        cbGoScan = function (opid, res) {
            window.XiaoTunYunJS.callCB(opid, res);
        };
        var opid = window.XiaoTunYunJS.addCB(cb);
        var paramsMap = {};
        paramsMap.opid = opid;
        window.messageHandlers.goScan(JSON.stringify(paramsMap));
    };
    window.XiaoTunYunJS.openLocation = function (status, callBack) {
        cbOpenLocation = callBack;
        cbOpenLocationStatus = status;
        var paramsMap = {};
        window.messageHandlers.openLocation(JSON.stringify(paramsMap));
    };
    window.XiaoTunYunJS.closeLocation = function () {
        var paramsMap = {};
        window.messageHandlers.closeLocation(JSON.stringify(paramsMap));
    };
    window.XiaoTunYunJS.getAddressByType = function (latitude, longitude, callBack) {
        cbGetAddressByType = callBack;
        var paramsMap = {};
        paramsMap.latitude = latitude;
        paramsMap.longitude = longitude;
        window.messageHandlers.getAddressByType(JSON.stringify(paramsMap));
    };

    window.XiaoTunYunJS.onResume = function (cb) {
        cbOnResume = function () {
            window.XiaoTunYunJS.callCB(opid, "resume");
        };
        var opid = window.XiaoTunYunJS.addCB(cb);
    };
    window.XiaoTunYunJS.onPause = function (cb) {
        cbOnPause = function () {
            window.XiaoTunYunJS.callCB(opid, "resume");
        };
        var opid = window.XiaoTunYunJS.addCB(cb);
    };
    window.XiaoTunYunJS.onNetworkStateChange = function (cb) {
        cbOnNetworkStateChange = function (res) {
            window.XiaoTunYunJS.callCB(opid, res);
        };
        var opid = window.XiaoTunYunJS.addCB(cb);
    };

    if (typeof window.XiaoTunYunJSReady === 'function') {
        window.XiaoTunYunJSReady();
    }

})();