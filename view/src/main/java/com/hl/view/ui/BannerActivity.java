package com.hl.view.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hl.base.BaseActivity;
import com.hl.view.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by hl on 2017/5/12.
 * banner 轮播图
 */
public class BannerActivity extends BaseActivity {

    private ViewPager mViewPaper;
    private List<ImageView> images;
    private List<View> dots;
    private int currentItem;
    //记录上一次点的位置
    private int oldPosition = 0;
    //存放图片的id
    private int[] imageIds = new int[]{
            R.drawable.banner1,
            R.drawable.banner2,
            R.drawable.banner3,
    };
    //存放图片的标题
    private String[] titles = new String[]{
            "标题1",
            "标题2",
            "标题3",
    };
    private TextView title;
    private ViewPagerAdapter adapter;
    private ScheduledExecutorService scheduledExecutorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_widget_activity_banner);
        mViewPaper = findViewById(R.id.vp);

        //显示的图片
        images = new ArrayList<>();
        for (int imageId : imageIds) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(imageId);
            images.add(imageView);
        }
        //显示的小点
        dots = new ArrayList<>();
        dots.add(findViewById(R.id.dot_0));
        dots.add(findViewById(R.id.dot_1));
        dots.add(findViewById(R.id.dot_2));

        title = findViewById(R.id.title);
        title.setText(titles[0]);

        adapter = new ViewPagerAdapter();
        mViewPaper.setAdapter(adapter);

        mViewPaper.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                title.setText(titles[position]);
                dots.get(position).setBackgroundResource(R.drawable.view_widget_dot_focused);
                dots.get(oldPosition).setBackgroundResource(R.drawable.view_widget_dot_normal);

                oldPosition = position;
                currentItem = position;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    /**
     * 利用线程池定时执行动画轮播
     */
    @Override
    protected void onStart() {
        super.onStart();
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleWithFixedDelay(
                new ViewPageTask(),
                5,
                5,
                TimeUnit.SECONDS);
    }

    /**
     * 自定义Adapter
     *
     * @author liuyazhuang
     */
    private class ViewPagerAdapter extends PagerAdapter {

        @Override
        public void destroyItem(@NonNull ViewGroup view, int position, @NonNull Object object) {
            view.removeView(images.get(position));
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup view, int position) {
            view.addView(images.get(position));
            return images.get(position);
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }
    }

    private class ViewPageTask implements Runnable {

        @Override
        public void run() {
            currentItem = (currentItem + 1) % imageIds.length;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mViewPaper.setCurrentItem(currentItem);
                }
            });
        }
    }
}
