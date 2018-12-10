package com.hl.view.ui;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.TextureView;
import android.widget.FrameLayout;

import com.hl.base.BaseActivity;
import com.hl.view.R;

import java.io.IOException;

public class TextureViewActivity extends BaseActivity implements TextureView.SurfaceTextureListener {

    private TextureView myTexture;
    private Camera mCamera;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity_textureview);
        myTexture = findViewById(R.id.textureView1);
        myTexture.setSurfaceTextureListener(this);

    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        try {
            mCamera = Camera.open();
            Camera.Size previewSize = mCamera.getParameters().getPreviewSize();
            myTexture.setLayoutParams(new FrameLayout.LayoutParams(
                    previewSize.width, previewSize.height, Gravity.CENTER));
            try {
                mCamera.setPreviewTexture(surface);
            } catch (IOException t) {
            }
            mCamera.startPreview();
            myTexture.setAlpha(1.0f);
            myTexture.setRotation(90.0f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 旋转 + 透明度
        // myTexture.setAlpha(0.5f);
        // myTexture.setRotation(180.0f);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        try {
            mCamera.stopPreview();
            mCamera.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}
