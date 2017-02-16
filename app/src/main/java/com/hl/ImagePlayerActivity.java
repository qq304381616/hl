package com.hl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片播放器
 */
public class ImagePlayerActivity extends AppCompatActivity {

    private ImageView imageView;

    private List<Bitmap> list = new ArrayList<Bitmap>();
    @Override
     protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageplayer);
        imageView = (ImageView) findViewById(R.id.imageview);

//        final Bitmap b = BitmapFactory.decodeFile("/sdcard/8/" + 1 + ".jpg");
//        imageView.setImageBitmap(b);

         start();
    }

    private void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 1;
                while (i < 15) {

                    long l = System.currentTimeMillis();
                    final Bitmap b = BitmapFactory.decodeFile("/sdcard/8/" + i + ".jpg");
                    Log.e("TAG", "time" + (System.currentTimeMillis() - l));
                    list.add(b);
                    i++;
                }

                while (j < 14) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(list.get(j-1));

                        }
                    });
                    j++;
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    int j= 1;
}
