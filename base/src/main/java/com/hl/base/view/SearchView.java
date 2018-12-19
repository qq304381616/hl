package com.hl.base.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hl.base.R;

/**
 * 搜索自定义控件
 */
public class SearchView extends FrameLayout {

    private EditText et_search;
    private ImageView iv_clear;

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.base_layout_search, this);

        et_search = findViewById(R.id.et_search);
        iv_clear = findViewById(R.id.iv_clear);

        /* 清除输入字符 */
        iv_clear.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                et_search.setText("");
            }
        });

        // 键盘搜索事件
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (callback != null) {
                    callback.onQueryTextSubmit(textView.getText().toString());
                }
                return false;
            }
        });

        et_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable e) {
                String content = et_search.getText().toString();
                if ("".equals(content)) {
                    iv_clear.setVisibility(View.INVISIBLE);
                } else {
                    iv_clear.setVisibility(View.VISIBLE);
                }
                if (callback != null) {
                    callback.onQueryTextChange(content);
                }
            }
        });
    }

    public Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        /**
         * 键盘搜索监听
         *
         * @param s
         */
        void onQueryTextSubmit(String s);

        /**
         * 输入变化监听
         *
         * @param s
         */
        void onQueryTextChange(String s);
    }
}
