package com.hl.skin;

import android.os.Bundle;

import com.hl.base.BaseActivity;
import com.hl.utils.FileUtils;

import java.io.File;

/**
 * 皮肤测试类 父类
 */
public class SkinActivity extends BaseActivity {

    private final static String SKIM_APK_PATH = "/sdcard/aa.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        LayoutInflaterCompat.setFactory(getLayoutInflater(), new SkinInflalteFactory());

        FileUtils.checkExistsAndCopy(this, new File(SKIM_APK_PATH));
        if (new File(SKIM_APK_PATH).exists()) {
            SkinManager.getInstance().loadSkin(this, SKIM_APK_PATH);
        }
    }
}
