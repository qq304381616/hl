package com.hl.widget;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * 控件集合
 */
public class WidgetMainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_activity_widgetmain);

        findViewById(R.id.textureview).setOnClickListener(this);
        findViewById(R.id.tv_swiperefreshlayout).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.textureview) {
            startActivity(new Intent(WidgetMainActivity.this, TextureViewActivity.class));
        }else if( id == R.id.tv_swiperefreshlayout){
            startActivity(new Intent(WidgetMainActivity.this, SwipeRefreshLayoutActivity.class));
        }
    }
}
