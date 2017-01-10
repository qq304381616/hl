package com.hl.utils;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

/**
 * 动画工具类
 */
public class AnimatorUtils {

    /**
     * 透明度动画 透明到不透明
     */
    public static Animation getAlphaAnimation() {
        AlphaAnimation alpha = new AlphaAnimation(0.1f, 1.0f);
        alpha.setDuration(300);
        return alpha;
    }

}
