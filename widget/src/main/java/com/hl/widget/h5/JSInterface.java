package com.hl.widget.h5;

import android.webkit.JavascriptInterface;

/**
 * Created on 2017/4/27
 */
public class JSInterface {

    private JsHandler mJsHandler;

    public JSInterface(JsHandler jsHandler) {
        mJsHandler = jsHandler;
    }

    @JavascriptInterface
    public void goScan(String params) {
        mJsHandler.sendMessage(JsHandler.getMsg(JsHandler.GO_SCAN, params));
    }

    @JavascriptInterface
    public void getNetworkType(String params) {
        mJsHandler.sendMessage(JsHandler.getMsg(JsHandler.NETWORK_TYPE, params));
    }

    @JavascriptInterface
    public void getDeviceInfo(String params) {
        mJsHandler.sendMessage(JsHandler.getMsg(JsHandler.DEVICE_INFO, params));
    }

    @JavascriptInterface
    public void chooseImage(String params) {
        mJsHandler.sendMessage(JsHandler.getMsg(JsHandler.CHOOSE_IMAGE, params));
    }

    @JavascriptInterface
    public void getDataUrl(String params) {
        mJsHandler.sendMessage(JsHandler.getMsg(JsHandler.DATA_URL, params));
    }

    @JavascriptInterface
    public void uploadFile(String params) {
        mJsHandler.sendMessage(JsHandler.getMsg(JsHandler.UPLOAD_FILE, params));
    }

    @JavascriptInterface
    public void downloadFile(String params) {
        mJsHandler.sendMessage(JsHandler.getMsg(JsHandler.DOWNLOAD_FILE, params));
    }

    @JavascriptInterface
    public void previewImage(String params) {
        mJsHandler.sendMessage(JsHandler.getMsg(JsHandler.PREVIEW_IMAGE, params));
    }

    @JavascriptInterface
    public void startRecord(String params) {
        mJsHandler.sendMessage(JsHandler.getMsg(JsHandler.START_RECORD, params));
    }

    @JavascriptInterface
    public void stopRecord(String params) {
        mJsHandler.sendMessage(JsHandler.getMsg(JsHandler.STOP_RECORD, params));
    }

    @JavascriptInterface
    public void playAudio(String params) {
        mJsHandler.sendMessage(JsHandler.getMsg(JsHandler.PLAY_AUDIO, params));
    }

    @JavascriptInterface
    public void pauseAudio(String params) {
        mJsHandler.sendMessage(JsHandler.getMsg(JsHandler.PAUSE_AUDIO, params));
    }

    @JavascriptInterface
    public void stopAudio(String params) {
        mJsHandler.sendMessage(JsHandler.getMsg(JsHandler.STOP_AUDIO, params));
    }

    @JavascriptInterface
    public void screenOrientation(String params) {
        mJsHandler.sendMessage(JsHandler.getMsg(JsHandler.SCREEN_ORIENTATION, params));
    }

    @JavascriptInterface
    public void openLocation(String params) {
        mJsHandler.sendMessage(JsHandler.getMsg(JsHandler.OPEN_LOCATION, params));
    }

    @JavascriptInterface
    public void closeLocation(String params) {
        mJsHandler.sendMessage(JsHandler.getMsg(JsHandler.CLOSE_LOCATION, params));
    }

    @JavascriptInterface
    public void getAddressByType(String params) {
        mJsHandler.sendMessage(JsHandler.getMsg(JsHandler.ADDRESS_TYPE, params));
    }
}