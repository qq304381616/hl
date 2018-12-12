package com.hl.skin;

import android.os.Bundle;

import com.hl.base.BaseActivity;
import com.hl.utils.SDCardUtils;

import java.io.File;
import java.io.IOException;

/**
 * 皮肤测试类 父类
 */
public class SkinActivity extends BaseActivity {

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
