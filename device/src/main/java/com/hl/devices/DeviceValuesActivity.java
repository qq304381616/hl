package com.hl.devices;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.hl.base.BaseActivity;
import com.hl.utils.SizeUtils;

/**
 * Created on 2017/4/21.
 */
public class DeviceValuesActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_activity_values);

        // string
        TextView tv_string = (TextView) findViewById(R.id.tv_string);
        tv_string.setText(getResources().getString(R.string.string_info));

        // dimens
        TextView tv_dimens = (TextView) findViewById(R.id.tv_dimens);
        float px = getResources().getDimension(R.dimen.dimens_dp);
        float dp = SizeUtils.px2dp(this, px);
        tv_dimens.setText(num2Type(dp) + " - " + dp + " dp" + " == " + px + " px");

        // integer
        TextView tv_integer = (TextView) findViewById(R.id.tv_integer);
        int num = getResources().getInteger(R.integer.integer_num);
        tv_integer.setText(num2Type(num) + " - " + num + " integer");

    }

    private String num2Type(float num) {
        String dimensType = "";
        if (num == 10f) dimensType = "values";
        if (num == 20f) dimensType = "values-xhdpi";
        if (num == 30f) dimensType = "values-xxhdpi";
        if (num == 40f) dimensType = "values-xxxhdpi";
        if (num == 50f) dimensType = "values-land";
        if (num == 60f) dimensType = "values-large";
        if (num == 70f) dimensType = "values-xlarge";
        if (num == 80f) dimensType = "values-xlarge-land";
        if (num == 90f) dimensType = "values-w720dp";
        if (num == 100f) dimensType = "values-h720dp";
        if (num == 110f) dimensType = "values-h320dp";
        if (num == 120f) dimensType = "values-sw720dp";
        if (num == 130f) dimensType = "values-sw720dp-land";
        if (num == 140f) dimensType = "values-sw720dp-port";
        if (num == 150f) dimensType = "values-sw600dp";
        if (num == 160f) dimensType = "values-sw600dp-land";
        if (num == 170f) dimensType = "values-sw600dp-port";
        if (num == 180f) dimensType = "values-sw600dp-w960dp";
        if (num == 190f) dimensType = "values-port";
        if (num == 200f) dimensType = "values-port-mdpi";
        return dimensType;
    }
}
