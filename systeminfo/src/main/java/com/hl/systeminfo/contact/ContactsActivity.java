package com.hl.systeminfo.contact;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hl.base.BaseActivity;
import com.hl.base.entity.BaseDataEntity;
import com.hl.base.utils.Utils;
import com.hl.systeminfo.R;
import com.hl.utils.views.SideBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 通讯录列表
 */
public class ContactsActivity extends BaseActivity {

    private RecyclerView rv_contacts;
    private EditText etSearch;
    private ImageView ivClearText;
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
        ivClearText = findViewById(R.id.ivClearText);
        etSearch = findViewById(R.id.et_search);
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

        /*清除输入字符 */
        ivClearText.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                etSearch.setText("");
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable e) {
                String content = etSearch.getText().toString();
                if ("".equals(content)) {
                    ivClearText.setVisibility(View.INVISIBLE);
                } else {
                    ivClearText.setVisibility(View.VISIBLE);
                }
                if (content.length() > 0) {
                    adapter.setData(search(content));
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