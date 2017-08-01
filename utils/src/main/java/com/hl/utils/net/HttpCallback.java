package com.hl.utils.net;

import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.hl.utils.LogUtils;

import rx.Observer;
import rx.functions.Action0;

/**
 * Created on 2017/3/6.
 */
public class HttpCallback implements Observer<JsonElement>, Action0 {

    private static final String TAG = HttpCallback.class.getSimpleName();
    private boolean isToast;
    private boolean isLoading;
    private ILoadingView mLoadingView;

    public HttpCallback() {
        init(null, false, false);
    }

    public HttpCallback(ILoadingView loadingView, boolean isToast, boolean isLoading) {
        init(loadingView, isToast, isLoading);
    }

    private void init(ILoadingView loadingView, boolean isToast, boolean isLoading) {
        this.mLoadingView = loadingView;
        this.isToast = isToast;
        this.isLoading = isLoading;
        if (mLoadingView == null) mLoadingView = new LoadingViewImpl();
    }

    @Override
    public void call() {
        if (mLoadingView != null && isLoading) mLoadingView.showLoading();
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        if (mLoadingView != null && isLoading) mLoadingView.hideLoading();
        if (isToast) Toast.makeText(mLoadingView.getContext(), "网络异常", Toast.LENGTH_SHORT).show();
        LogUtils.e(TAG, e.getMessage());
        e.printStackTrace();
    }

    @Override
    public final void onNext(JsonElement result) {
        if (mLoadingView != null && isLoading) mLoadingView.hideLoading();
        Gson gson = new Gson();
        LogUtils.e("TAG", gson.toJson(result));
        HttpBaseEntity entity = gson.fromJson(gson.toJson(result), new TypeToken<HttpBaseEntity>() {
        }.getType());
        if (entity.getStatus() == HttpConstant.RESULT_CODE_SUCCESS) { // 返回成功
            onSuccess(entity.getData());
        } else {
            onFailure(entity.getDesc());
        }
    }

    protected void onSuccess(Object t) {

    }

    protected void onFailure(String message) {
        if (isToast) Toast.makeText(mLoadingView.getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
