package com.hl.view.ui.toolbar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.hl.base.BaseActivity;
import com.hl.view.R;

/**
 * ToolBar是替代ActionBar的控件, 需要让原来的ActionBar 隐藏起来
 * ToolBar 颜色设置成主题颜色  android:background="?attr/colorPrimary"
 * <p>
 * <p>
 * <p>
 * <style name="AppTheme.Base" parent="Theme.AppCompat">
 * <item name="windowActionBar">false</item>  // 隐藏ActionBar
 * <item name="android:windowNoTitle">true</item> // 隐藏状态栏
 * <!-- Actionbar color ToolBar颜色 -->
 * <item name="colorPrimary">@color/accent_material_dark</item>
 * <!--Status bar color 状态栏颜色-->
 * <item name="colorPrimaryDark">@color/accent_material_light</item>
 * <!--Window color 中间内容区域颜色-->
 * <item name="android:windowBackground">@color/dim_foreground_material_dark</item>
 * <!-- menu 图标 三个圆点的颜色 -->
 * <item name="android:textColorSecondary">#ffffff</item>
 * </style>
 */
public class ToolBarActivity extends BaseActivity {

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            int id = menuItem.getItemId();
            String msg = "";
            if (id == R.id.action_edit) {
                msg += "Click edit";
            } else if (id == R.id.action_share) {
                msg += "Click share";
            } else if (id == R.id.action_settings) {
                msg += "Click setting";
            }

            Toast.makeText(ToolBarActivity.this, msg, Toast.LENGTH_SHORT).show();
            return true;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity_toolbar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher); // 设置icon
        toolbar.setTitle("大标题"); // 大标题
        toolbar.setSubtitle("小标题"); // 小标题
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 给左上角图标的左边加上一个返回的图标 。对应ActionBar.DISPLAY_HOME_AS_UP
        toolbar.setNavigationIcon(R.mipmap.ic_launcher); // 返回图标
        getSupportActionBar().setDisplayShowHomeEnabled(true); // 使左上角图标是否显示，如果设成false，则没有程序图标，仅仅就个标题，否则，显示应用程序图标，对应id为 android.R.id.home，对应ActionBar.DISPLAY_SHOW_HOME
        toolbar.setOnMenuItemClickListener(onMenuItemClick); // 设置监听
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 监听返回点击事件
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
