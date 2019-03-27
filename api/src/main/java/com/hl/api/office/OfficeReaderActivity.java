package com.hl.api.office;


import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.FrameLayout;

import com.hl.api.R;
import com.hl.base.BaseActivity;
import com.hl.utils.FileUtils;
import com.tencent.smtt.sdk.TbsReaderView;

import java.io.File;

/**
 * 腾讯 解析展示 office 文件。 需要加载一个jar文件。
 */
public class OfficeReaderActivity extends BaseActivity {
    private FrameLayout frameLayout;
    private TbsReaderView readerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.api_activity_office_reader);
        frameLayout = findViewById(R.id.fl);
        String fileName = getIntent().getStringExtra("fileName");
        final String path = getIntent().getStringExtra("path");

        // 如果不开线程 会报错。
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!TextUtils.isEmpty(path) && new File(path).exists()) {
                            openFile(path);
                        }
                    }
                });
            }
        }).start();
    }

    /**
     * 打开文件
     */
    private void openFile(String path) {
        readerView = new TbsReaderView(this, new TbsReaderView.ReaderCallback() {
            @Override
            public void onCallBackAction(Integer integer, Object o, Object o1) {

            }
        });
        //通过bundle把文件传给x5,打开的事情交由x5处理
        Bundle bundle = new Bundle();
        //传递文件路径
        bundle.putString("filePath", path);
        //加载插件保存的路径
        bundle.putString("tempPath", Environment.getExternalStorageDirectory() + File.separator + "temp");
        //加载文件前的初始化工作,加载支持不同格式的插件
        boolean b = readerView.preOpen(FileUtils.getFilePrefix(path), false);
        if (b) {
            readerView.openFile(bundle);
        }
        frameLayout.addView(readerView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (readerView != null) readerView.onStop();
    }
}