package com.hl.base.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import com.hl.base.R;

/**
 * 计时选择器
 * Created on 2018/2/7.
 */
public class NumPickerView {

    public static void createDialog(Activity activity, String time, final PickerListener listener) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle("修改");

        // 取得自定义View
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        final View view = layoutInflater.inflate(R.layout.base_layout_picker, null);
        final NumberPicker p1 = view.findViewById(R.id.numberPicker1);
        final NumberPicker p2 = view.findViewById(R.id.numberPicker2);
        final NumberPicker p3 = view.findViewById(R.id.numberPicker3);
        dialog.setView(view);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (listener != null) {
                    listener.ok(String.format("%02d", p1.getValue()) + ":" + String.format("%02d", p2.getValue()) + "." + String.format("%02d", p3.getValue()));
                }
            }
        });
        dialog.setNegativeButton("取消", null);

        p1.setMaxValue(59);
        p1.setMinValue(0);
        p2.setMaxValue(59);
        p2.setMinValue(0);
        p3.setMaxValue(99);
        p3.setMinValue(0);

        String m = time.split(":")[0];
        String s = time.split(":")[1].split("\\.")[0];
        String ss = time.split(":")[1].split("\\.")[1];

        p1.setValue(Integer.valueOf(m));
        p2.setValue(Integer.valueOf(s));
        p3.setValue(Integer.valueOf(ss));

        dialog.show();
    }

    public interface PickerListener {
        void ok(String result);
    }
}
