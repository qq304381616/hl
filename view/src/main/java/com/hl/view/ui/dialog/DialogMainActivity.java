package com.hl.view.ui.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.hl.base.BaseActivity;
import com.hl.base.dialog.DialogUtils;
import com.hl.base.dialog.NumPickerView;
import com.hl.utils.ToastUtils;
import com.hl.view.R;

public class DialogMainActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity_dialog_main);
        initToolbar(true);

        findViewById(R.id.tv_loading).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtils.getLoading(DialogMainActivity.this, "加载中..").show();
            }
        });
        findViewById(R.id.tv_normal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtils.getDialog(DialogMainActivity.this, "标题", "内容", "是", "否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ToastUtils.showShortToast(getApplicationContext(), "点击 是");
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ToastUtils.showShortToast(getApplicationContext(), "点击 否");
                    }
                }).show();
            }
        });
        findViewById(R.id.tv_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 取得自定义View
                final EditText et_input = new EditText(getApplicationContext());
                et_input.setHint("请输入");
                DialogUtils.getDialog(DialogMainActivity.this, "标题", "内容", et_input,
                        "是", "否", null, null).show();
            }
        });

        findViewById(R.id.tv_num_picker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumPickerView.createDialog(DialogMainActivity.this, "01:01.01", new NumPickerView.PickerListener() {
                    @Override
                    public void ok(String result) {

                    }
                });
            }
        });
    }
}
