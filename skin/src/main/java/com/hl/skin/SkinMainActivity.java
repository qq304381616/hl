package com.hl.skin;

import android.os.Bundle;
import android.widget.TextView;

/**
 * 换肤主界面
 */
public class SkinMainActivity extends SkinActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.skin_activity_main);
        initToolbar(true);

        TextView tv_self = findViewById(R.id.tv_self);
        TextView tv_skin = findViewById(R.id.tv_skin);

        tv_self.setTextColor(getResources().getColor(R.color.colorPrimary));
        tv_skin.setTextColor(SkinManager.getInstance().getColor(R.color.colorPrimary));
    }
}
