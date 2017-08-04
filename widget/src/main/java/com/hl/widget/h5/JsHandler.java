package com.hl.widget.h5;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.webkit.WebView;

import com.hl.api.zxing.activity.CaptureActivity;
import com.hl.utils.BitmapUtils;
import com.hl.utils.LogUtils;
import com.hl.utils.MD5Utils;
import com.hl.utils.NetworkUtils;
import com.hl.utils.PicUtils;
import com.hl.utils.Utils;
import com.hl.utils.record.IRecord;
import com.hl.utils.record.Mp3RecordImpl;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created on 2017/4/27.
 */
public class JsHandler extends Handler {

    private static final String TAG = JsHandler.class.getSimpleName();

    protected WebView mWebView;
    protected Context mContext;

    public static final int DEVICE_INFO = 1001;
    public static final int CB_DEVICE_INFO = 1013;
    public static final int NET_CHANGE = 1002;
    public static final int CB_GO_SCAN = 1003;
    public static final int GO_SCAN = 1004;
    public static final int NETWORK_TYPE = 1005;
    public static final int OPEN_LOCATION = 1006;
    public static final int CLOSE_LOCATION = 1007;
    public static final int ADDRESS_TYPE = 1008;
    public static final int RESUME = 1009;
    public static final int PAUSE = 1010;
    public static final int CHOOSE_IMAGE = 1011;
    public static final int CB_CHOOSE_IMAGE = 1012;
    public static final int DATA_URL = 1014;
    public static final int CB_DATA_URL = 1015;
    public static final int PREVIEW_IMAGE = 1016;
    public static final int UPLOAD_FILE = 1017;
    public static final int UPLOAD_FILE_SUCCESS = 1018;
    public static final int UPLOAD_FILE_FAILURE = 1019;
    public static final int UPLOAD_FILE_PROGRESS = 1020;
    public static final int DOWNLOAD_FILE = 1021;
    public static final int DOWNLOAD_FILE_SUCCESS = 1022;
    public static final int DOWNLOAD_FILE_FAILURE = 1023;
    public static final int DOWNLOAD_FILE_PROGRESS = 1024;
    public static final int START_RECORD = 1025;
    public static final int STOP_RECORD = 1026;
    public static final int PLAY_AUDIO = 1027;
    public static final int PAUSE_AUDIO = 1028;
    public static final int STOP_AUDIO = 1029;
    public static final int SCREEN_ORIENTATION = 1030;

    public static String CHOOSE_IMAGE_SUCCESSOPID;
    public static String CHOOSE_IMAGE_CANCELOPID;
    public static String DEVICE_INFO_CANCELOPID;
    public static String SCAN_OPID;


    private IRecord record;
    private MediaPlayer mp;

    public JsHandler(Context context, WebView webView) {
        mWebView = webView;
        mContext = context;
    }

    public static Message getMsg(int what, String obj) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = obj;
        return msg;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        try {
            String obj = (String) msg.obj;
            JSONObject json = new JSONObject();
            if (!TextUtils.isEmpty(obj)) {
                json = new JSONObject(obj);
            }
            LogUtils.e(TAG, "js接口 : " + msg.what + "  " + json);
            switch (msg.what) {
                case CHOOSE_IMAGE: // 选择照片
                    CHOOSE_IMAGE_SUCCESSOPID = json.getString("successOpid");
                    CHOOSE_IMAGE_CANCELOPID = json.getString("cancelOpid");
                    String sourceType = json.getString("sourceType");
                    String count = json.getString("count");
                    String cropMode = json.getString("cropMode");
                    new PicUtils().startSelect((JSWebViewActivity) mContext, count, cropMode);
                    break;
                case CB_CHOOSE_IMAGE:
                    JSONObject p5 = new JSONObject();
                    p5.put("ids", json.getJSONArray("ids"));
                    mWebView.loadUrl("javascript:cbChooseImage(" + json.getString("opid") + ",'" + p5.toString() + "');");
                    break;
                case DATA_URL:
                    try {
                        String file = Utils.getJsCachePath(mContext) + File.separator + json.get("ids");
                        if (!new File(file).exists()) {
                            return;
                        }
                        Bitmap b = BitmapUtils.getimage(file, 120f, 120f);
                        String base64 = BitmapUtils.getBitmapBase64(b, BitmapUtils.getBitmapType(file));
//                        String base64 = Utils.encodeBase64File(file);
                        mWebView.loadUrl("javascript:cbDataUrl(" + json.getString("opid") + ",'" + base64 + "');");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case PREVIEW_IMAGE: // {"urls":["aaa","bbb"],"index":1}
                    List<LocalMedia> selectList = new ArrayList<LocalMedia>();
                    JSONArray urls = json.getJSONArray("urls");
                    for (int i = 0; i < urls.length(); i++) {
                        LocalMedia lm = new LocalMedia();
                        String path = Utils.getJsCachePath(mContext) + File.separator + urls.getString(i);
                        if (new File(path).exists()) {
                            lm.setPath(path);
                            selectList.add(lm);
                        }
                    }
                    int index = json.getInt("index");
                    if (index < 1) index = 1;
                    PictureSelector.create((JSWebViewActivity) mContext).externalPicturePreview(index - 1, selectList);
                    break;
                case UPLOAD_FILE: // 上传文件
                    final JSONArray ids = json.getJSONArray("localIds");
                    JSONObject stringParams = json.getJSONObject("stringParams");
                    String inputName = json.getString("inputName");
                    JSONObject header = json.getJSONObject("header");
                    List<String> list = new ArrayList<>();
                    for (int i = 0; i < ids.length(); i++) {
                        list.add(ids.getString(i));
                    }
                    Map<String, String> stringMap = new HashMap<>();
                    Iterator it = stringParams.keys();
                    while (it.hasNext()) {
                        String key = (String) it.next();
                        String value = stringParams.getString(key);
                        stringMap.put(key, value);
                    }
                    UploadFile uFile = new UploadFile(list, stringMap, json.getString("serverURL"), inputName, json.getInt("successOpid"), json.getInt("failOpid"), json.getInt("statusOpid"));
                    UploadUtils h = new UploadUtils(this, mContext, uFile);
                    h.startUp();
                    break;
                case UPLOAD_FILE_SUCCESS:
                    mWebView.loadUrl("javascript:cbUploadFile(" + json.getString("opid") + ",'" + json.getString("result") + "');");
                    break;
                case UPLOAD_FILE_FAILURE:
                    mWebView.loadUrl("javascript:cbUploadFile(" + json.getString("opid") + ",'" + json.getString("result") + "');");
                    break;
                case UPLOAD_FILE_PROGRESS:
                    mWebView.loadUrl("javascript:cbUploadFile(" + json.getString("opid") + ",'" + json.getString("result") + "');");
                    break;
                case DOWNLOAD_FILE: // 下载文件
                    final JSONArray jUrls = json.getJSONArray("urls");
                    List<String> urlsList = new ArrayList<>();
                    for (int i = 0; i < jUrls.length(); i++) {
                        urlsList.add(jUrls.getString(i));
                    }
                    new UploadUtils(this, mContext, new UploadFile(urlsList, null, null, "", json.getInt("successOpid"), json.getInt("failOpid"), json.getInt("statusOpid")))
                            .startDown();
                    break;
                case DOWNLOAD_FILE_SUCCESS:
                    mWebView.loadUrl("javascript:cbDownloadFile(" + json.getString("opid") + ",'" + json.getString("result") + "');");
                    break;
                case DOWNLOAD_FILE_FAILURE:
                    mWebView.loadUrl("javascript:cbDownloadFile(" + json.getString("opid") + ",'" + json.getString("result") + "');");
                    break;
                case DOWNLOAD_FILE_PROGRESS:
                    mWebView.loadUrl("javascript:cbDownloadFile(" + json.getString("opid") + ",'" + json.getString("result") + "');");
                    break;
                case START_RECORD:
                    record = new Mp3RecordImpl();
//                   record = new RecordUtils(mContext);

                    String id = MD5Utils.get32MD5(String.valueOf(System.currentTimeMillis())) + ".mp3";
                    String path = Utils.getJsCachePath(mContext) + File.separator + id;
                    record.setSavePath(path);
                    record.startRecording();
                    mWebView.loadUrl("javascript:cbStartRecord(" + json.getString("opid") + ",'" + id + "');");
                    break;
                case STOP_RECORD:
                    record.stopRecording();
                    break;
                case PLAY_AUDIO:
                    String audioPath = Utils.getJsCachePath(mContext) + File.separator + json.getString("id");
                    if (new File(audioPath).exists()) {
                        if (mp == null) {
                            mp = new MediaPlayer();
                            try {
                                mp.setDataSource(audioPath);
                                mp.prepare();
                                mp.start();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else if (!mp.isPlaying()) {
                            mp.start();
                        }
                    }
                    break;
                case PAUSE_AUDIO:
                    if (mp != null) {
                        if (mp.isPlaying()) {
                            mp.pause();
                        }
                    }
                    break;
                case STOP_AUDIO:
                    if (mp != null && mp.isPlaying()) {
                        mp.stop();
                        mp.release();
                        mp = null;
                    }
                    break;
                case NET_CHANGE:
                    mWebView.loadUrl("javascript:cbOnNetworkStateChange(" + json.getString("netMobile") + ");");
                    break;
                case DEVICE_INFO:
                    DEVICE_INFO_CANCELOPID = json.getString("opid");
                    ((JSWebViewActivity) mContext).getDeviceInfo();
                    break;
                case CB_DEVICE_INFO:
                    mWebView.loadUrl("javascript:cbGetDeviceInfo(" + DEVICE_INFO_CANCELOPID + ", '" + json.getString("result") + "');");
                    break;
                case GO_SCAN:
                    Intent intent = new Intent();
                    intent.setClass(mContext, CaptureActivity.class);
                    SCAN_OPID = json.getString("opid");
                    ((Activity) mContext).startActivityForResult(intent, JSWebViewActivity.GOSCAN_RESULTCODE);
                    break;
                case CB_GO_SCAN:
                    JSONObject p4 = new JSONObject();
                    p4.put("result", json.getString("result"));
                    mWebView.loadUrl("javascript:cbGoScan(" + SCAN_OPID + ",'" + p4.toString() + "');");
                    break;
                case NETWORK_TYPE:
                    String opid = json.getString("opid");
                    int res = -1;
                    NetworkUtils.NetworkType networkType = NetworkUtils.getNetworkType(mContext);
                    switch (networkType) {
                        case NETWORK_WIFI:
                            res = 0;
                        break;
                        case NETWORK_3G:
                            res = 1;
                        break;
                        case NETWORK_2G:
                            res = 2;
                        break;
                        case NETWORK_4G:
                            res = 3;
                        break;
                    }
                    mWebView.loadUrl("javascript:cbGetNetworkType(" + opid + ", " + res + ");");
                    break;
                case OPEN_LOCATION:
                    break;
                case CLOSE_LOCATION:
                    break;
                case ADDRESS_TYPE:
                    double lat = json.getDouble("latitude");
                    double log = json.getDouble("longitude");
                    break;
                case SCREEN_ORIENTATION: // 1 横  2 竖  3 旋转
                    switch (json.getInt("type")) {
                        case 1:
                            if (((JSWebViewActivity) mContext).getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                                ((JSWebViewActivity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                            }
                            break;
                        case 2:
                            if (((JSWebViewActivity) mContext).getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                                ((JSWebViewActivity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            }
                            break;
                        case 3:
                            if (((JSWebViewActivity) mContext).getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_SENSOR) {
                                ((JSWebViewActivity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                            }
                            break;
                    }
                    break;
                case RESUME:
                    mWebView.loadUrl("javascript:cbOnResume();");
                    break;
                case PAUSE:
                    mWebView.loadUrl("javascript:cbOnPause();");
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
