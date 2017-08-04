package com.hl.widget.h5;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hl.utils.DeviceUtils;
import com.hl.utils.FileUtils;
import com.hl.utils.MD5Utils;
import com.hl.utils.Utils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class JSWebViewActivity extends AppCompatActivity {

    private static final int READ_PHONE_STATE = 2001;
    public final static int GOSCAN_RESULTCODE = 2;

    private WebView wv;

    private JsHandler mJsHandler;
    private JSInterface jsInterface;

    private static final String URL = "file:///android_asset/hello.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wv = new WebView(this);
        setContentView(wv);

        mJsHandler = new JsHandler(this, wv);
        jsInterface = new JSInterface(mJsHandler);

        wv.getSettings().setJavaScriptEnabled(true);  //支持js
        wv.addJavascriptInterface(jsInterface, "messageHandlers"); // 公共JS接口
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String initJs = "javascript:";
                try {
                    InputStream in = getAssets().open("initjsformethods.js");
                    int lenght = in.available();
                    byte[] buffer = new byte[lenght];
                    in.read(buffer);
                    initJs += new String(buffer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                view.loadUrl(initJs);
            }
        });

        wv.loadUrl(URL);
    }

    public void getDeviceInfo() {
        boolean b = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED;
        if (b) {
            ArrayList<String> list = new ArrayList<String>();
            if (b) list.add(Manifest.permission.READ_PHONE_STATE);
            String[] strings = new String[list.size()];
            list.toArray(strings);
            ActivityCompat.requestPermissions(this, strings, READ_PHONE_STATE);
        } else {
            cbDevicesInfo();
        }
    }

    private void cbDevicesInfo() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("result", DeviceUtils.getDeviceInfo(this));
            mJsHandler.sendMessage(JsHandler.getMsg(JsHandler.CB_DEVICE_INFO, jsonObject.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GOSCAN_RESULTCODE) {
            switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
                case RESULT_OK:
                    try {
                        Bundle b = data.getExtras(); //data为B中回传的Intent
                        String opid = b.getString("opid");//str即为回传的值
                        String result = b.getString("result");
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("opid", opid);
                        jsonObject.put("result", result);
                        mJsHandler.sendMessage(JsHandler.getMsg(JsHandler.CB_GO_SCAN, jsonObject.toString()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        } else if (requestCode == PictureConfig.CHOOSE_REQUEST) {
            if (resultCode == RESULT_OK) {
                // 图片选择结果回调
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                if (selectList == null || selectList.size() == 0) {
                    return;
                }
                // 例如 LocalMedia 里面返回三种path
                // 1.media.getPath(); 为原图path
                // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的

                // 将选择的文件复制到APP缓存目录下、以文件MD5命名
                try {
                    JSONObject jsonObject = new JSONObject();
                    JSONArray ja = new JSONArray();
                    jsonObject.put("opid", JsHandler.CHOOSE_IMAGE_SUCCESSOPID);

                    for (LocalMedia lm : selectList) {
                        String path;
                        if (lm.isCut()) {
                            path = lm.getCutPath();
                        } else {
                            path = lm.getPath();
                        }
                        if (!new File(path).exists()) {
                            continue;
                        }
                        String tarPath = Utils.getJsCachePath(this) + File.separator + MD5Utils.getMd5ByFile(path) + "." + FileUtils.getFilePrefix(path);
                        FileUtils.copyFile(path, tarPath);
                        if (!new File(tarPath).exists()) {
                            continue;
                        }
                        ja.put(MD5Utils.getMd5ByFile(path) + "." + FileUtils.getFilePrefix(path));
                    }
                    jsonObject.put("ids", ja);
                    mJsHandler.sendMessage(JsHandler.getMsg(JsHandler.CB_CHOOSE_IMAGE, jsonObject.toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("opid", JsHandler.CHOOSE_IMAGE_CANCELOPID);
                    JSONArray ja = new JSONArray();
                    jsonObject.put("ids", ja);
                    mJsHandler.sendMessage(JsHandler.getMsg(JsHandler.CB_CHOOSE_IMAGE, jsonObject.toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case READ_PHONE_STATE: {
                if (grantResults.length > 0) {
                    for (int s : grantResults) {
                        if (s != PackageManager.PERMISSION_GRANTED) {
                            Log.e("TAG", "已拒绝权限申请");
//                            T.showShort(getActivity(), "请在设置内开启权限");
                            return;
                        }
                    }
                    Log.e("TAG", "已同意权限申请");
                    cbDevicesInfo();
                } else {
                    Log.e("TAG", "已拒绝权限申请");
//                    T.showShort(this, "请开启权限");
                }
                return;
            }
        }
    }
}
