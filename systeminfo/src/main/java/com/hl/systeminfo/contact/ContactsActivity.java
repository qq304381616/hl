package com.hl.systeminfo.contact;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.hl.base.BaseActivity;
import com.hl.base.entity.BaseDataEntity;
import com.hl.base.utils.Utils;
import com.hl.base.view.SearchView;
import com.hl.systeminfo.R;
import com.hl.utils.views.SideBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 通讯录列表
 */
public class ContactsActivity extends BaseActivity {

    private RecyclerView rv_contacts;
    private SearchView search_view;
    private SideBar sideBar;
    private List<BaseDataEntity> mAllContactsList;
    private ContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.system_layout_contacts);
        initToolbar(true);

        sideBar = findViewById(R.id.sidrbar);
        TextView dialog = findViewById(R.id.dialog);
        sideBar.setTextView(dialog);
        search_view = findViewById(R.id.search_view);
        rv_contacts = findViewById(R.id.rv_contacts);

        rv_contacts.setLayoutManager(new LinearLayoutManager(this));
        rv_contacts.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        adapter = new ContactAdapter(this);
        rv_contacts.setAdapter(adapter);

        initListener();

        mAllContactsList = Utils.getContacts(this);
        adapter.setData(mAllContactsList);
        adapter.notifyDataSetChanged();
    }

    private void initListener() {

        search_view.setCallback(new SearchView.Callback() {
            @Override
            public void onQueryTextSubmit(String s) {

            }

            @Override
            public void onQueryTextChange(String s) {
                if (s.length() > 0) {
                    adapter.setData(search(s));
                } else {
                    adapter.setData(mAllContactsList);
                }
                adapter.notifyDataSetChanged();
                rv_contacts.scrollToPosition(0);
            }
        });

        // 设置右侧[A-Z]快速导航栏触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    rv_contacts.scrollToPosition(position);
                }
            }
        });
    }

    /**
     * 模糊查询
     */
    private List<BaseDataEntity> search(String str) {
        List<BaseDataEntity> filterList = new ArrayList<>();// 过滤后的list
        for (BaseDataEntity contact : mAllContactsList) {
            if (contact.getInfo().contains(str) && !filterList.contains(contact)) {
                filterList.add(contact);
            }
        }
        return filterList;
    }
}