package com.hl.greendao.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.hl.base.BaseActivity;
import com.hl.base.adapter.BaseRecyclerAdapter;
import com.hl.greendao.R;
import com.hl.greendao.gen.Addresse;
import com.hl.greendao.gen.AddresseDao;
import com.hl.greendao.gen.DaoSession;

import java.text.DateFormat;
import java.util.Date;

public class GreenDaoActivity extends BaseActivity {

    private AddresseDao addresseDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.green_activity);

        final EditText et_name = findViewById(R.id.et_name);

        RecyclerView rv_db = findViewById(R.id.rv_db);
        rv_db.setLayoutManager(new LinearLayoutManager(this));
        rv_db.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        BaseRecyclerAdapter adapter = new BaseRecyclerAdapter(this);
        rv_db.setAdapter(adapter);

        DaoSession daoSession = GreenDaoUtil.getInstance().getDaoSession();
        addresseDao = daoSession.getAddresseDao();

        adapter.setItemClickListener(new BaseRecyclerAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });

        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_name.getText().toString().trim();
                if (!TextUtils.isEmpty(name)) {
                    et_name.setText("");

                    final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
                    String comment = "Added on " + df.format(new Date());

                    Addresse address = new Addresse();
                    address.setName(comment);

//                    addresseDao.insert(address);

                }
            }
        });
    }
}
