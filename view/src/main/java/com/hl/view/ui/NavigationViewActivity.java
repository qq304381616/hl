package com.hl.view.ui;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.hl.base.BaseActivity;
import com.hl.view.R;

/**
 * 左划菜单栏
 */
public class NavigationViewActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity_navigationview);

        //设置抽屉DrawerLayout
        final DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();//初始化状态
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //设置导航栏NavigationView的点击事件
        NavigationView mNavigationView = findViewById(R.id.navigation);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.menu1) {
                    Toast.makeText(getApplicationContext(), "menu1", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.menu2) {
                    Toast.makeText(getApplicationContext(), "menu2", Toast.LENGTH_SHORT).show();
                }
                menuItem.setChecked(true);//点击了把它设为选中状态
                mDrawerLayout.closeDrawers();//关闭抽屉
                return true;
            }
        });

        mDrawerToggle.setDrawerIndicatorEnabled(false); //// 取消左上角图标，还是可以滑出菜单。 图标是否可以替换为返回
    }
}
