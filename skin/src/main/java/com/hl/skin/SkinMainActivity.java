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
        TextView textView = (TextView) findViewById(R.id.tv);
        textView.setTextColor(SkinManager.getInstance().getColor(R.color.colorPrimary));

    }
}
