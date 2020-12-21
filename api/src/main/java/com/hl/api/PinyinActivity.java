package com.hl.api;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hl.base.BaseActivity;
import com.hl.base.dialog.DialogUtils;
import com.hl.base.utils.Pinyin;
import com.hl.utils.L;

public class PinyinActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.api_activity_pinyin);

        final EditText et_input = findViewById(R.id.et_input);

        TextView tv_info = findViewById(R.id.tv_info);
        String info = "setToneType(): \n" + "  WITHOUT_TONE: 无声调。 \n  WITH_TONE_NUMBER 数字声调。 \n  WITH_TONE_MARK 符号声调"
                + "\n\n setVCharType() ‘驴’表示方式：\n " + "WITH_U_UNICODE ü. WITH_V v. WITH_U_AND_COLON u:";
        tv_info.setText(info);

        findViewById(R.id.tv_py1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new Pinyin().setSimple(true).getPinYin(et_input.getText().toString());// 简拼
                String pinYin = new Pinyin().getPinYin(et_input.getText().toString()); // 全拼
                L.e(pinYin);
                DialogUtils.getDialog(PinyinActivity.this, pinYin, "确定", null, null, null).show();
            }
        });
    }
}
