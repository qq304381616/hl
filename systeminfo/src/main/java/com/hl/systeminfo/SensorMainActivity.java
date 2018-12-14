package com.hl.systeminfo;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.hl.base.BaseActivity;
import com.hl.base.adapter.BaseRecyclerAdapter;
import com.hl.base.entity.BaseDataEntity;
import com.hl.utils.L;

import java.util.ArrayList;
import java.util.List;

/**
 * 传感器列表 有颜色背景是硬件支持的。
 */
public class SensorMainActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.system_activity_sensor_main);
        initToolbar(true);
        TextView tv_info = findViewById(R.id.tv_info); // 信息

        RecyclerView rv_sensor = findViewById(R.id.rv_sensor);

        rv_sensor.setLayoutManager(new LinearLayoutManager(this));
        rv_sensor.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        BaseRecyclerAdapter adapter = new BaseRecyclerAdapter(this);
        rv_sensor.setAdapter(adapter);

        SensorManager mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL); // 获取所有传感器
        tv_info.setText("本设备传感器数量：" + sensorList.size());

        List<BaseDataEntity> list = new ArrayList<>();
        for (Sensor s : sensorList) {
            list.add(new BaseDataEntity(s.getType(), s.getStringType().replace("android.sensor.", "") + " "+getChinaType(s.getType())));
        }
        adapter.setData(list);
        adapter.notifyDataSetChanged();

        SensorEventListener listener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                L.e(event);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                L.e(sensor);
            }
        };

        // 注册一个传感器
        Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (null != sensor) {
            // 1参 监听
            // 2参 传感器类型 TYPE_ACCELEROMETER 加速传感器
            // 3参 更新数据速度，递增
            //SENSOR_DELAY_UI
            //SENSOR_DELAY_NORMAL
            //SENSOR_DELAY_GAME
            //SENSOR_DELAY_FASTEST
//            mSensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL); // 注册传感器
//            mSensorManager.unregisterListener(listener); // 注销传感器
        }
    }

    private String getChinaType(int value) {
        switch (value) {
            case Sensor.TYPE_ACCELEROMETER:
                return "加速度传感器";
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                return "周围温度传感器";
            case Sensor.TYPE_GAME_ROTATION_VECTOR:
                return "游戏动作传感器，不收电磁干扰影响";
            case Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR:
                return "地磁旋转矢量传感器，提供手机的旋转矢量，当手机处于休眠状态时，仍可以记录设备的方位";
            case Sensor.TYPE_GRAVITY:
                return "重力传感器";
            case Sensor.TYPE_GYROSCOPE:
                return "陀螺仪传感器";
            case Sensor.TYPE_GYROSCOPE_UNCALIBRATED:
                return "未校准陀螺仪传感器，提供原始的，未校准、补偿的陀螺仪数据，用于后期处理和融合定位数据";
            case Sensor.TYPE_HEART_RATE:
                return "";
            case Sensor.TYPE_LIGHT:
                return "光照传感器";
            case Sensor.TYPE_LINEAR_ACCELERATION:
                return "线性加速度传感器";
            case Sensor.TYPE_MAGNETIC_FIELD:
                return "磁力传感器";
            case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED:
                return "未校准磁力传感器，提供原始的，未校准的磁场数据";
            case Sensor.TYPE_PRESSURE:
                return "压力传感器";
            case Sensor.TYPE_PROXIMITY:
                return "距离传感器";
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                return "相对湿度传感器";
            case Sensor.TYPE_ROTATION_VECTOR:
                return "旋转矢量传感器";
            case Sensor.TYPE_SIGNIFICANT_MOTION:
                return "特殊动作触发传感器";
            case Sensor.TYPE_STEP_COUNTER:
                return "计步传感器";
            case Sensor.TYPE_STEP_DETECTOR:
                return "步行检测传感器，用户每走一步就触发一次事件";
            case Sensor.TYPE_ORIENTATION:
                return "方向传感器";
            case Sensor.TYPE_TEMPERATURE:
                return "温度传感器 目前已被TYPE_AMBIENT_TEMPERATURE替代";
            case Sensor.TYPE_LOW_LATENCY_OFFBODY_DETECT:
                return "";
            case Sensor.TYPE_ACCELEROMETER_UNCALIBRATED:
                return "";
            default:
                return "";
        }
    }
}