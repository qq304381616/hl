package com.hl.design;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * ToolBar是替代ActionBar的控件, 需要让原来的ActionBar 隐藏起来
 * ToolBar 颜色设置成主题颜色  android:background="?attr/colorPrimary"
 *
 *
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
public class ToolBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher); // 设置icon
        toolbar.setTitle("My Title"); // 大标题
        toolbar.setSubtitle("Sub title"); // 小标题
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher); // 返回图标
        toolbar.setOnMenuItemClickListener(onMenuItemClick); // 设置监听
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

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
    public boolean onOptionsItemSelected(MenuItem item) {
        // 监听返回点击事件
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return true;
    }
}
