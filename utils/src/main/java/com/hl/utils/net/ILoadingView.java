package com.hl.utils.net;

import android.content.Context;

/**
 * 加载等待进度条
 */
public interface ILoadingView {

    /**
     * 显示
     */
    void showLoading();

    /**
     * 隐藏
     */
    void hideLoading();

    Context getContext();
}
