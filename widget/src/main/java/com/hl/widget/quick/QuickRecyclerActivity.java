package com.hl.widget.quick;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.hl.widget.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Recycler 右侧字母 快速定位
 */
public class QuickRecyclerActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private QuickAdapter adapter;
    private SideBar sideBar;
    private TextView dialog;
    private List<SortModel> mAllContactsList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_recycler);
        recycler = (RecyclerView) findViewById(R.id.recycler_quick);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new QuickAdapter(this);
        recycler.setAdapter(adapter);
        recycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));

        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);

        // 设置右侧[A-Z]快速导航栏触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    recycler.scrollToPosition(position);
                }
            }
        });

        mAllContactsList = new ArrayList<SortModel>();
        mAllContactsList.add(new SortModel("zhangsan", "z"));
        mAllContactsList.add(new SortModel("lisi", "l"));
        mAllContactsList.add(new SortModel("wangwu", "w"));

        for (int i = 0; i < 20; i++) {
            mAllContactsList.add( new SortModel("wangwu" + i, "w"));
        }

        Collections.sort(mAllContactsList, new PinyinComparator());

        adapter.setData(mAllContactsList);
        adapter.notifyDataSetChanged();
    }

    class PinyinComparator implements Comparator<SortModel> {

        public int compare(SortModel o1, SortModel o2) {
            if (o1.getSortLetters().equals("@") || o2.getSortLetters().equals("#")) {
                return -1;
            } else if (o1.getSortLetters().equals("#") || o2.getSortLetters().equals("@")) {
                return 1;
            } else {
                return o1.getSortLetters().compareTo(o2.getSortLetters());
            }
        }
    }

}
