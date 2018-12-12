package com.hl.utils.net;

import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.hl.utils.L;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created on 2017/3/6.
 */
public class HttpCallback implements Observer<JsonElement> {

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
    public void onError(Throwable e) {
        L.e( "onError");
        if (mLoadingView != null && isLoading) mLoadingView.hideLoading();
        L.e( e.getMessage());
        e.printStackTrace();
    }

    @Override
    public void onComplete() {
        L.e( "onComplete");
    }

    @Override
    public void onSubscribe(Disposable d) {
        L.e( "onSubscribe");
        if (mLoadingView != null && isLoading) mLoadingView.showLoading();
    }

    @Override
    public final void onNext(JsonElement result) {
        L.e( "onNext");
        if (mLoadingView != null && isLoading) mLoadingView.hideLoading();
        Gson gson = new Gson();
        L.e("TAG", gson.toJson(result));
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
