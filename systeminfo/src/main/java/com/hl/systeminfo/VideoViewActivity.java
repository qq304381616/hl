package com.hl.systeminfo;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.hl.base.BaseActivity;
import com.hl.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 视频 播放器
 */
public class VideoViewActivity extends BaseActivity {

    private VideoView videoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.system_activity_videoview);

        File file = new File(getFilesDir(), "test.mp4");
        FileUtils.checkExistsAndCopy(this, file);

        Uri uri = Uri.parse(file.getAbsolutePath());
        videoView = this.findViewById(R.id.videoview);
        videoView.setMediaController(new MediaController(this));
        videoView.setVideoURI(uri);
        videoView.start();
        videoView.requestFocus();

        TextView tv_pre = findViewById(R.id.tv_pre);
        TextView tv_next = findViewById(R.id.tv_next);
        tv_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.seekTo(videoView.getCurrentPosition() - 33);
            }
        });

        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.seekTo(videoView.getCurrentPosition() + 33);
            }
        });

//         createVideoThumbnail(videoView.getCurrentPosition(), path, "/sdcard/1.png");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView != null) {
            videoView.suspend();
        }
    }

    /**
     * 获取视频指定时间帧图片  保存到手机
     *
     * @param timeMs   指定时间秒
     * @param filePath 视频路径
     * @param savePath 保存图片的路径
     */
    public void createVideoThumbnail(int timeMs, String filePath, String savePath) {
        FileOutputStream fileOutputStream = null;
        Bitmap bitmap = null;
        try {
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(filePath);
            bitmap = mediaMetadataRetriever.getFrameAtTime(timeMs * 1000, MediaMetadataRetriever.OPTION_CLOSEST);
            fileOutputStream = new FileOutputStream(savePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (bitmap != null) {
                    bitmap.recycle();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
