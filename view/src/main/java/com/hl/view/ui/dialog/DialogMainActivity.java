package com.hl.view.ui.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hl.base.BaseActivity;
import com.hl.base.dialog.NumPickerView;
import com.hl.view.R;

public class DialogMainActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity_dialog_main);

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
