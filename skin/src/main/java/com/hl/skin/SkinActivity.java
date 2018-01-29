package com.hl.skin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hl.utils.SDCardUtils;

import java.io.File;
import java.io.IOException;

/**
 * 皮肤测试类
 */
public class SkinActivity extends AppCompatActivity {

    private final static String SKIM_APK_PATH = "/sdcard/aa.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        LayoutInflaterCompat.setFactory(getLayoutInflater(), new SkinInflalteFactory());

        if (!new File(SKIM_APK_PATH).exists()) {
            try {
                SDCardUtils.assetsToSD(this, "aa.apk", SKIM_APK_PATH);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (new File(SKIM_APK_PATH).exists()) {
            SkinManager.getInstance().init(this);
            SkinManager.getInstance().loadSkin(SKIM_APK_PATH);
        }
    }
}
