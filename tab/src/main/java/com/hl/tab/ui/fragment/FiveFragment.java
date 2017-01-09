package com.hl.tab.ui.fragment;

import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class FiveFragment extends BaseFragment{

    @Override
    protected View getSuccessView() {
        TextView textView = new TextView(getActivity());
        textView.setText("第五页");
        return textView;
    }

    @Override
    protected Object requestData() {
        SystemClock.sleep(1000);/*模拟请求服务器的延时过程*/
        return new ArrayList<>();/*加载为空*/
    }
}
