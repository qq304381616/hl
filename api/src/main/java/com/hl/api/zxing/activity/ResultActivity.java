package com.hl.api.zxing.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hl.api.R;
import com.hl.api.zxing.utils.QRUtils;
import com.hl.base.BaseActivity;
import com.hl.utils.BitmapUtils;

/**
 * 生成二维码
 */
public class ResultActivity extends BaseActivity {

    private ImageView iv_1;
    private ImageView iv_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.api_activity_zxing_result);
        initToolbar(true);

        final EditText et_text = findViewById(R.id.et_text);
        iv_1 = findViewById(R.id.iv_1);
        iv_2 = findViewById(R.id.iv_2);
        TextView tv_make = findViewById(R.id.tv_make);

        tv_make.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(et_text.getText().toString())) {
                    Bitmap b = QRUtils.generateBitmap(et_text.getText().toString(), 640, 640); // 普通二维码
                    Bitmap myQRBitmap = BitmapUtils.centerBitmap(b, BitmapFactory.decodeResource(getResources(), R.drawable.base_ic_launcher), 6f); // 中间加图片
                    iv_1.setImageBitmap(b);
                    iv_2.setImageBitmap(myQRBitmap);
                }
            }
        });
    }
}
