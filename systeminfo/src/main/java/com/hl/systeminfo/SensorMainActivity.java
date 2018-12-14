package com.hl.systeminfo;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.hl.base.BaseActivity;
import com.hl.utils.L;

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
        TextView TYPE_ACCELEROMETER = findViewById(R.id.TYPE_ACCELEROMETER); // 加速度传感器
        TextView TYPE_AMBIENT_TEMPERATURE = findViewById(R.id.TYPE_AMBIENT_TEMPERATURE); // 周围温度传感器
        TextView TYPE_GRAVITY = findViewById(R.id.TYPE_GRAVITY); // 重力传感器
        TextView TYPE_GYROSCOPE = findViewById(R.id.TYPE_GYROSCOPE); // 陀螺仪传感器
        TextView TYPE_LIGHT = findViewById(R.id.TYPE_LIGHT); // 光照传感器
        TextView TYPE_LINEAR_ACCELERATION = findViewById(R.id.TYPE_LINEAR_ACCELERATION); // 线性加速度传感器
        TextView TYPE_MAGNETIC_FIELD = findViewById(R.id.TYPE_MAGNETIC_FIELD); // 磁力传感器
        TextView TYPE_ORIENTATION = findViewById(R.id.TYPE_ORIENTATION); // 方向传感器
        TextView TYPE_PRESSURE = findViewById(R.id.TYPE_PRESSURE); // 压力传感器
        TextView TYPE_PROXIMITY = findViewById(R.id.TYPE_PROXIMITY); // 接近传感器
        TextView TYPE_RELATIVE_HUMIDITY = findViewById(R.id.TYPE_RELATIVE_HUMIDITY); // 相对湿度传感器
        TextView TYPE_ROTATION_VECTOR = findViewById(R.id.TYPE_ROTATION_VECTOR); // 旋转矢量传感器
        TextView TYPE_TEMPERATURE = findViewById(R.id.TYPE_TEMPERATURE); // 温度传感器

        SensorManager mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL); // 获取所有传感器
        L.e(sensorList);
        for (Sensor s : sensorList) {
            switch (s.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    TYPE_ACCELEROMETER.setBackgroundColor(getResources().getColor(R.color.color_orange));
                    break;
                case Sensor.TYPE_AMBIENT_TEMPERATURE:
                    TYPE_AMBIENT_TEMPERATURE.setBackgroundColor(getResources().getColor(R.color.color_orange));
                    break;
                case Sensor.TYPE_GRAVITY:
                    TYPE_GRAVITY.setBackgroundColor(getResources().getColor(R.color.color_orange));
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    TYPE_GYROSCOPE.setBackgroundColor(getResources().getColor(R.color.color_orange));
                    break;
                case Sensor.TYPE_LIGHT:
                    TYPE_LIGHT.setBackgroundColor(getResources().getColor(R.color.color_orange));
                    break;
                case Sensor.TYPE_LINEAR_ACCELERATION:
                    TYPE_LINEAR_ACCELERATION.setBackgroundColor(getResources().getColor(R.color.color_orange));
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    TYPE_MAGNETIC_FIELD.setBackgroundColor(getResources().getColor(R.color.color_orange));
                    break;
                case Sensor.TYPE_ORIENTATION:
                    TYPE_ORIENTATION.setBackgroundColor(getResources().getColor(R.color.color_orange));
                    break;
                case Sensor.TYPE_PRESSURE:
                    TYPE_PRESSURE.setBackgroundColor(getResources().getColor(R.color.color_orange));
                    break;
                case Sensor.TYPE_PROXIMITY:
                    TYPE_PROXIMITY.setBackgroundColor(getResources().getColor(R.color.color_orange));
                    break;
                case Sensor.TYPE_RELATIVE_HUMIDITY:
                    TYPE_RELATIVE_HUMIDITY.setBackgroundColor(getResources().getColor(R.color.color_orange));
                    break;
                case Sensor.TYPE_ROTATION_VECTOR:
                    TYPE_ROTATION_VECTOR.setBackgroundColor(getResources().getColor(R.color.color_orange));
                    break;
                case Sensor.TYPE_TEMPERATURE: // 替换为 TYPE_AMBIENT_TEMPERATURE
                    TYPE_TEMPERATURE.setBackgroundColor(getResources().getColor(R.color.color_orange));
                    break;
            }
        }

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
            mSensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
//            mSensorManager.unregisterListener(listener); // 注销传感器
        }
    }
}