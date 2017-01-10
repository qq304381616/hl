package com.hl.utils;

import android.content.Context;
import android.media.AudioManager;

/**
 * 控制播放音量的工具类
 */
public class AudioVolumeUtils {

    /**
     * 设置音量
     * 音量的百分比：max:1.0,min:0.0;
     */
    public static void setVolume(Context context, float percent) {
        // 音量控制,初始化定义
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        // 最大音量
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        // 当前音量
        int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (currentVolume < percent * maxVolume) {
            if (percent >= 0 && percent <= 1) {
                float result = maxVolume * percent;
                System.out.println("max=" + maxVolume + "current=" + currentVolume + "set=" + result + "int-set= " + (int) result);
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) result, 0);
            }
        }
    }
}
