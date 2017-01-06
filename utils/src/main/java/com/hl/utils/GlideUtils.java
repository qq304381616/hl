package com.hl.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class GlideUtils {


//    Glide.with(Context context);// 绑定Context
//    Glide.with(Activity activity);// 绑定Activity
//    Glide.with(FragmentActivity activity);// 绑定FragmentActivity
//    Glide.with(Fragment fragment);// 绑定Fragment

    // 简单加载
    public static void show(Context c, String url, ImageView iv) {
        Glide.with(c).load(url).into(iv);
    }

    //设置加载中以及加载失败图片
    public static void show(Context c, String url, int holder, int error, ImageView iv) {
        Glide.with(c).load(url).placeholder(holder).error(error).into(iv);
    }

//  设置跳过内存缓存
// Glide.with(this).load(imageUrl).skipMemoryCache(true).into(imageView);

//    设置下载优先级
//    Glide.with(this).load(imageUrl).priority(Priority.NORMAL).into(imageView);

//    设置缓存策略
//    all:缓存源资源和转换后的资源
//    none:不作任何磁盘缓存
//    source:缓存源资源
//    result：缓存转换后的资源
//    Glide.with(this).load(imageUrl).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);


//    设置加载动画
//    api也提供了几个常用的动画：比如crossFade()
//    <?xml version="1.0" encoding="utf-8"?>
//    <set xmlns:android="http://schemas.android.com/apk/res/android">
//    <alpha
//    android:duration="500"
//    android:fromAlpha="0.0"
//    android:toAlpha="1.0"/>
//    </set>
//    Glide.with(this).load(imageUrl).animate(R.anim.item_alpha_in).into(imageView);

//    设置缩略图支持
//    这样会先加载缩略图 然后在加载全图
//    Glide.with(this).load(imageUrl).thumbnail(0.1f).into(imageView);

//    设置加载尺寸
//    Glide.with(this).load(imageUrl).override(800, 800).into(imageView);

// ----------------------------------------------------------------------------------------------
//    设置动态转换
//    Glide.with(this).load(imageUrl).centerCrop().into(imageView);
//    api提供了比如：centerCrop()、fitCenter()等函数也可以通过自定义Transformation，举例说明：比如一个人圆角转化器

//    public class GlideRoundTransform extends BitmapTransformation {
//        private float radius = 0f;
//        public GlideRoundTransform(Context context) {
//            this(context, 4);
//        }
//
//        public GlideRoundTransform(Context context, int dp) {
//            super(context);
//            this.radius = Resources.getSystem().getDisplayMetrics().density * dp;
//        }
//
//        @Override
//        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
//            return roundCrop(pool, toTransform);
//        }
//
//        private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
//            if (source == null) return null;
//
//            Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
//            if (result == null) {
//                result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
//            }
//            Canvas canvas = new Canvas(result);
//            Paint paint = new Paint();
//            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
//            paint.setAntiAlias(true);
//            RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
//            canvas.drawRoundRect(rectF, radius, radius, paint);
//            return result;
//        }
//
//        @Override
//        public String getId() {
//            return getClass().getName() + Math.round(radius);
//        }
//    }
//
//    Glide.with(this).load(imageUrl).transform(new GlideRoundTransform(this)).into(imageView);

// ----------------------------------------------------------------------------------------------

//    设置要加载的内容
//    项目中有很多需要先下载图片然后再做一些合成的功能，比如项目中出现的图文混排，该如何实现目标下
//    Glide.with(this).load(imageUrl).centerCrop().into(new SimpleTarget<GlideDrawable>() {
//        @Override
//        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
//            imageView.setImageDrawable(resource);
//        }
//    });
// ----------------------------------------------------------------------------------------------

//    设置监听请求接口
//    Glide.with(this).load(imageUrl).listener(new RequestListener<String, GlideDrawable>() {
//        @Override
//        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//            return false;
//        }
//
//        @Override
//        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//            //imageView.setImageDrawable(resource);
//            return false;
//        }
//    }).into(imageView);

// ----------------------------------------------------------------------------------------------

//    设置动态GIF加载方式
//    Glide.with(this).load(imageUrl).asBitmap().into(imageView);//显示gif静态图片
//    Glide.with(this).load(imageUrl).asGif().into(imageView);//显示gif动态图片

//    缓存的动态清理
//    Glide.get(this).clearDiskCache();//清理磁盘缓存 需要在子线程中执行
//    Glide.get(this).clearMemory();//清理内存缓存  可以在UI主线程中进行
}
