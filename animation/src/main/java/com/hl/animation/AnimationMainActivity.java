package com.hl.animation;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hl.animation.adapter.CommonAdapter;
import com.hl.animation.adapter.CommonViewHolder;
import com.hl.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;


public class AnimationMainActivity extends BaseActivity {

    private static final boolean fromXml = true;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation_activity_main);
        initToolbar(true);
        iv =  findViewById(R.id.iv);
    }

    public void onClick(View v) {
        iv.setImageResource(R.mipmap.ic_launcher);
        int id = v.getId();
        if (id == R.id.animation_alpha) {
            if (fromXml) {
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
                iv.startAnimation(animation);
            } else {
                /*
                 * 第一个参数fromAlpha为 动画开始时候透明度
                 *第二个参数toAlpha为 动画结束时候透明度
                 */
                Animation animation = new AlphaAnimation(0, 1);
                animation.setDuration(1000);
                iv.startAnimation(animation);
            }
        } else if (id == R.id.animation_scale) {
            if (fromXml) {
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale);
                iv.startAnimation(animation);
            } else {
                /*
                 * 第一个参数fromX为动画起始时 X坐标上的伸缩尺寸
                 * 第二个参数toX为动画结束时 X坐标上的伸缩尺寸
                 * 第三个参数fromY为动画起始时Y坐标上的伸缩尺寸
                 * 第四个参数toY为动画结束时Y坐标上的伸缩尺寸
                 * 说明: 0.0表示收缩到没有;1.0表示正常无伸缩;值小于1.0表示收缩;值大于1.0表示放大

                 * 第五个参数pivotXType为动画在X轴相对于物件位置类型
                 * 第六个参数pivotXValue为动画相对于物件的X坐标的开始位置
                 * 第七个参数pivotXType为动画在Y轴相对于物件位置类型
                 * 第八个参数pivotYValue为动画相对于物件的Y坐标的开始位置
                 */
                Animation animation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration(1000);
                iv.startAnimation(animation);
            }
        } else if (id == R.id.animation_rotate) {
            if (fromXml) {
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate);
                iv.startAnimation(animation);
            } else {
                /*
                 * 第一个参数fromDegrees为动画起始时角度
                 * 第二个参数toDegrees为动画结束角度
                 * 第三个参数pivotXType为动画在X轴相对于物件位置类型
                 * 第四个参数pivotXValue为动画相对于物件的X坐标的开始位置
                 * 第五个参数pivotXType为动画在Y轴相对于物件位置类型
                 * 第六个参数pivotYValue为动画相对于物件的Y坐标的开始位置
                 */
                Animation animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration(1000);
                iv.startAnimation(animation);
            }
        } else if (id == R.id.animation_translate) {
            if (fromXml) {
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.translate);
                iv.startAnimation(animation);
            } else {
                /*
                 * 第一个参数fromXDelta为动画起始时的x坐标
                 * 第二个参数toXDelta为动画结束时的x坐标
                 * 第三个参数fromYDelta为动画起始时的y坐标
                 * 第四个参数toYDelta为动画结束时的y坐标
                 */
                Animation animation = new TranslateAnimation(0, 500, 0, 0);
                animation.setDuration(2000);
                /*设置插值器：先加速，后减速*/
                animation.setInterpolator(new AccelerateDecelerateInterpolator());
                iv.startAnimation(animation);
            }
        } else if (id == R.id.animation_group1) {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale);
            iv.startAnimation(animation);
            final Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.rotate);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    iv.startAnimation(animation2);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        } else if (id == R.id.animation_group2) {
            Animation animationGroup = AnimationUtils.loadAnimation(this, R.anim.group2);
            iv.startAnimation(animationGroup);
        } else if (id == R.id.animation_group3) {
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
            alphaAnimation.setDuration(1000);
            alphaAnimation.setRepeatCount(10);
            /*倒序重复REVERSE  正序重复RESTART**/
            alphaAnimation.setRepeatMode(Animation.REVERSE);
            iv.startAnimation(alphaAnimation);
        } else if (id == R.id.animation_group4) {
            Animation translateAnimation = new TranslateAnimation(-10, 10, 0, 0);
            translateAnimation.setDuration(100);
            translateAnimation.setRepeatCount(10);
            /*倒序重复REVERSE  正序重复RESTART**/
            translateAnimation.setRepeatMode(Animation.REVERSE);
            iv.startAnimation(translateAnimation);
        } else if (id == R.id.animation_frame) {
            iv.setImageResource(R.drawable.ring_animation);
            AnimationDrawable animationDrawable = (AnimationDrawable) iv.getDrawable();
            animationDrawable.start();
        } else if (id == R.id.animation_layout) {
            commonAdapterTest();
        } else if (id == R.id.animation_activity) {
            startActivity(new Intent(this, SecondActivity.class));
            overridePendingTransition(R.anim.zoom_out, R.anim.zoom_in);
        }
    }

    /**
     * 布局动画
     */
    private void commonAdapterTest() {
        ListView listView =  findViewById(R.id.listview);
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 18; i++) {
            data.add("万能适配器测试" + i);
        }
        LayoutAnimationController layoutAnimationController = new LayoutAnimationController(AnimationUtils.loadAnimation(this, R.anim.zoom_in));
        layoutAnimationController.setOrder(LayoutAnimationController.ORDER_NORMAL);
        listView.setLayoutAnimation(layoutAnimationController);
        listView.setAdapter(new CommonAdapter<String>(this, data, R.layout.animation_item) {

            @Override
            protected void convertView(View item, String s) {
                TextView textView = CommonViewHolder.get(item, R.id.textView);
                textView.setText(s);
            }
        });
    }
}
