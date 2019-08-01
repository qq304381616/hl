package com.hl.systeminfo;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.hl.base.BaseActivity;

/**
 * 获取目录
 */
public class PathActivity extends BaseActivity {

    private static final String PATH = "abs";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.system_activity_path);
        initToolbar(true);

        TextView text = findViewById(R.id.text);
        String s = "";

        s += "以下文件夹清除缓存时被清理：" + "\n";
        s += "getCacheDir: " + getCacheDir().getAbsolutePath() + "\n";
        s += "getFilesDir: " + getFilesDir().getAbsolutePath() + "\n";
        s += "getDir: " + getDir(PATH, Context.MODE_PRIVATE).getAbsolutePath() + "\n";
        s += "\n";
        s += "以下文件夹程序卸载清除数据存时被清理：" + "\n";
        s += "getExternalCacheDir: " + getExternalCacheDir().getAbsolutePath() + "\n";
        s += "getExternalFilesDir: " + getExternalFilesDir(PATH).getAbsolutePath() + "\n";
        s += "\n";
        s += "其他：" + "\n";
        s += "getObbDir: " + getObbDir().getAbsolutePath() + "\n";
        s += "getObbDir: " + getCodeCacheDir().getAbsolutePath() + "\n";
        s += "Environment.getExternalStorageDirectory: " + Environment.getExternalStorageDirectory() + "\n";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            s += "getDataDir: " + getDataDir().getAbsolutePath() + "\n";
        }

        text.setText(s);
    }
}
