package com.hl.devices;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.hl.utils.DeviceUtils;
import com.hl.utils.ScreenUtils;
import com.hl.utils.SizeUtils;

/**
 * Created on 2017/4/21.
 */
public class DeviceMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_activity_main);

        StringBuffer sb = new StringBuffer();
        boolean deviceRoot = DeviceUtils.isDeviceRoot();
        sb.append("设置是否root : " + deviceRoot + "\n");
        int screenWidth = ScreenUtils.getScreenWidth(this);
        int screenHeight = ScreenUtils.getScreenHeight(this);
        sb.append("分辨率 宽 X 高 : " + screenWidth + " x " + screenHeight + "\n");
        sb.append("宽度px2dp : " + SizeUtils.px2dp(this, screenWidth) + "\n");
        sb.append("高度px2dp : " + SizeUtils.px2dp(this, screenHeight) + "\n");
        sb.append("系统版本号 : " + DeviceUtils.getSDKVersion() + "\n");
        sb.append("设备ID : " + DeviceUtils.getAndroidID(this) + "\n");
        sb.append("MAC : " + DeviceUtils.getMacAddress(this) + "\n");
        sb.append("设备厂商 : " + DeviceUtils.getManufacturer() + "\n");
        sb.append("设备型号 : " + DeviceUtils.getModel() + "\n");

        TextView device_info = (TextView) findViewById(R.id.device_info);
        device_info.setText(sb.toString());

        findViewById(R.id.layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DeviceMainActivity.this, DeviceLayoutActivity.class));
            }
        });
        findViewById(R.id.drawable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DeviceMainActivity.this, DeviceDrawableActivity.class));
            }
        });
        findViewById(R.id.values).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DeviceMainActivity.this, DeviceValuesActivity.class));
            }
        });

        StringBuffer s = new StringBuffer();
        s.append("land横屏，port竖屏. \n" );
        s.append("layout < layout-port < layout-h600dp // 默认优先级 低于 横竖屏 低于 具体dp. \n" );
        s.append("640dp的手机。h600dp > h320dp > 其他. \n" );
        s.append("w600dp   宽或高大小600dp 依据当前屏幕方向的宽度. \n" );
        s.append("sw600dp  宽或高均大于600dp 不依据当前屏幕方向. \n" );
        s.append("xlarge 960dp x 720dp | large  640dp x 480dp | normal 470dp x 320dp | small 426dp x 320dp. \n" );
        s.append("dimen 中设置 match_parent <dimen name=\"max_width\">-1dp</dimen> . \n" );
        s.append("dimen 中设置 wrap_content <dimen name=\"max_width\">-2dp</dimen> . \n" );

        TextView device_text = (TextView) findViewById(R.id.device_text);
        device_text.setText(s.toString());
    }
}
