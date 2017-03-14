package com.hl.skin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by hl on 2017/3/14.
 */
public class SkinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        LayoutInflaterCompat.setFactory(getLayoutInflater(), new SkinInflalteFactory());
        SkinManager.getInstance().init(this);
        SkinManager.getInstance().loadSkin("/sdcard/aa.apk");
    }

}
