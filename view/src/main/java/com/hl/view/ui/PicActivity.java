package com.hl.view.ui;

import android.media.ExifInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hl.base.BaseActivity;
import com.hl.utils.BitmapUtils;
import com.hl.utils.ToastUtils;
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
            ToastUtils.showLongToast(this, "menu");

            try {
                ExifInterface mExifInterface = new ExifInterface(pic);
                String a3 = mExifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
                String a5 = mExifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH);
                int a6 = BitmapUtils.getBitmapDegree(pic);

                tv_info.setVisibility(View.VISIBLE);
                String info = a3 + "\n";
                info += a5 + "\n";
                info += a6 + "\n";
                tv_info.setText(info);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return super.onOptionsItemSelected(item);
    }
}

