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
     * ---streamType常用：
     * STREAM_ALARM 警报
     * STREAM_MUSIC 音乐回放即媒体音量
     * STREAM_NOTIFICATION 窗口顶部状态栏Notification,
     * STREAM_RING 铃声
     * STREAM_SYSTEM 系统
     * STREAM_VOICE_CALL 通话
     * STREAM_DTMF 双音多频,拨号键的声音
     * ----direction,是调整的方向,增加或减少,可以是:
     * ADJUST_LOWER 降低音量
     * ADJUST_RAISE 升高音量
     * ADJUST_SAME 保持不变,这个主要用于向用户展示当前的音量
     * ----flags,是附加参数：
     * FLAG_PLAY_SOUND 调整音量时播放声音
     * FLAG_SHOW_UI 调整时显示音量条,就是按音量键出现的界面
     * FLAG_REMOVE_SOUND_AND_VIBRATE 无振动无声音
     * FLAG_ALLOW_RINGER_MODES Whether to include ringer modes as possible options when changing volume.
     * FLAG_VIBRATE Whether to vibrate if going into the vibrate ringer mode
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
