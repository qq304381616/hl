package com.hl;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hl.animation.AnimationMainActivity;
import com.hl.api.ApiMainActivity;
import com.hl.base.BaseActivity;
import com.hl.devices.DeviceMainActivity;
import com.hl.dotime.HomeActivity;
import com.hl.greendao.example.GreenDaoActivity;
import com.hl.knowledge.KnowledgeMainActivity;
import com.hl.okhttp3.example.OkhttpMainActivity;
import com.hl.skin.SkinMainActivity;
import com.hl.systeminfo.SystemMainActivity;
import com.hl.tool.ToolMainActivity;
import com.hl.utils.ToastUtils;
import com.hl.view.ui.ViewMainActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar(false);

        findViewById(R.id.tv_test).setOnClickListener(this);
        findViewById(R.id.tv_animation).setOnClickListener(this);
        findViewById(R.id.tv_design).setOnClickListener(this);
        findViewById(R.id.tv_knowledge).setOnClickListener(this);
        findViewById(R.id.tv_system).setOnClickListener(this);
        findViewById(R.id.tv_skin).setOnClickListener(this);
        findViewById(R.id.api).setOnClickListener(this);
        findViewById(R.id.device).setOnClickListener(this);
        findViewById(R.id.tv_greendao).setOnClickListener(this);
        findViewById(R.id.tv_okhttp3).setOnClickListener(this);
        findViewById(R.id.tv_tool).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_do_time) {
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_test:
                ToastUtils.showShortToast(getApplicationContext(), "测试");
                break;
            case R.id.tv_animation:
                startActivity(new Intent(MainActivity.this, AnimationMainActivity.class));
                break;
            case R.id.tv_design:
                startActivity(new Intent(MainActivity.this, ViewMainActivity.class));
                break;
            case R.id.tv_knowledge:
                startActivity(new Intent(MainActivity.this, KnowledgeMainActivity.class));
                break;
            case R.id.tv_system:
                startActivity(new Intent(MainActivity.this, SystemMainActivity.class));
                break;
            case R.id.tv_skin:
                startActivity(new Intent(MainActivity.this, SkinMainActivity.class));
                break;
            case R.id.api:
                startActivity(new Intent(MainActivity.this, ApiMainActivity.class));
                break;
            case R.id.device:
                startActivity(new Intent(MainActivity.this, DeviceMainActivity.class));
                break;
            case R.id.tv_greendao:
                startActivity(new Intent(MainActivity.this, GreenDaoActivity.class));
                break;
            case R.id.tv_okhttp3:
                startActivity(new Intent(MainActivity.this, OkhttpMainActivity.class));
                break;
            case R.id.tv_tool:
                startActivity(new Intent(MainActivity.this, ToolMainActivity.class));
                break;
        }
    }
}