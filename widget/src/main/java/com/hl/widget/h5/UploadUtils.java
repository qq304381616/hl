package com.hl.widget.h5;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.hl.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 2017/7/19.
 */
public class UploadUtils {

    private static final String TAG = UploadUtils.class.getSimpleName();

    private Context mContext;
    private Handler mHandler;
    private UploadFile uFile;
    private HttpUtils http;

    private int success;
    private int failure;
    private int mProgress;

    public UploadUtils(Handler handler, Context context, UploadFile file) {
        mContext = context;
        mHandler = handler;
        uFile = file;
        http = new HttpUtils();
        http.setCallback(new HttpUtils.FileCallback() {
            @Override
            public void success(String result) {
                Log.e(TAG, "success");
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("opid", uFile.getSuccessOpid());
                    jsonObject.put("result", result);
                    mHandler.sendMessage(JsHandler.getMsg(success, jsonObject.toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure() {
                Log.e(TAG, "failure");
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("opid", uFile.getFailOpid());
                    jsonObject.put("result", "failure");
                    mHandler.sendMessage(JsHandler.getMsg(failure, jsonObject.toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void progress(long progress) {
                Log.e(TAG, "progress " + progress);
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("opid", uFile.getStatusOpid());
                    jsonObject.put("result", getNum(progress, http.getCount()));
                    mHandler.sendMessage(JsHandler.getMsg(mProgress, jsonObject.toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void startUp() {
        success = JsHandler.UPLOAD_FILE_SUCCESS;
        failure = JsHandler.UPLOAD_FILE_FAILURE;
        mProgress = JsHandler.UPLOAD_FILE_PROGRESS;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> map = new HashMap<>();
                long length = 0;
                for (String id : uFile.getLocalIds()) {
                    String path = Utils.getJsCachePath(mContext) + File.separator + id;
                    map.put(id, path);
                    length += new File(path).length();
                }
                http.setCount(length);
                http.uploadFile(uFile.getInputName(), uFile.getServerURL(), uFile.getStringParams(), map);
            }
        }).start();
    }

    public void startDown() {
        success = JsHandler.DOWNLOAD_FILE_SUCCESS;
        failure = JsHandler.DOWNLOAD_FILE_FAILURE;
        mProgress = JsHandler.DOWNLOAD_FILE_PROGRESS;
        final List<String> urls = uFile.getLocalIds();
        new Thread(new Runnable() {
            @Override
            public void run() {
                http.downloadFile(urls.get(0), Utils.getJsCachePath(mContext));
            }
        }).start();
    }

    private String getNum(long l1, long l2) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        return numberFormat.format((double) l1 / (double) l2 * 100);
    }
}
