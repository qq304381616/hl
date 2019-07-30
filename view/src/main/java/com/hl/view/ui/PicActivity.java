package com.hl.view.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hl.base.BaseActivity;
import com.hl.utils.PhotoAlbumUtils;
import com.hl.utils.api.GlideUtils;
import com.hl.view.R;

import java.io.IOException;

public class PicActivity extends BaseActivity {

    private String pic;
    private TextView tv_info;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_pic);
        initToolbar(true);

        ImageView iv_pic = findViewById(R.id.iv_pic);
        pic = getIntent().getStringExtra("pic");
        GlideUtils.show(this, pic, iv_pic);

        tv_info = findViewById(R.id.tv_info);
        tv_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_info.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_menu_pic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item_info) {
            try {
                String info = PhotoAlbumUtils.getPhotoInfo(pic);
                tv_info.setText(info);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}

