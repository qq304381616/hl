package com.hl.systeminfo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hl.base.BaseActivity;
import com.hl.base.adapter.CheckBoxRecyclerAdapter;
import com.hl.base.entity.BaseDataEntity;
import com.hl.base.utils.OrderComparator;
import com.hl.base.utils.Pinyin;
import com.hl.utils.ConvertUtils;
import com.hl.utils.SDCardUtils;
import com.hl.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 文件管理器
 */
public class ExplorerActivity extends BaseActivity {

    private TextView tv_top;
    private TextView tv_len;
    private TextView tv_size;
    private RecyclerView rv_list;
    private CheckBoxRecyclerAdapter checkBoxRecyclerAdapter;
    private String current = "/"; // 当前目录
    private List<String> roots = new ArrayList<>(); // 顶级目录/ SD卡 和 扩展卡
    private List<String> resultList = new ArrayList<>(); // 选择的文件的列表
    private Pinyin pinyin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.system_activity_explorer);
        initToolbar(true);

        pinyin = new Pinyin();

        tv_top = findViewById(R.id.tv_top);
        tv_len = findViewById(R.id.tv_len);
        tv_size = findViewById(R.id.tv_size);
        rv_list = findViewById(R.id.rv_list);

        tv_size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder sb = new StringBuilder();
                for (String s : resultList) {
                    sb.append(s).append("\n");
                }
                ToastUtils.showShortToast(getApplicationContext(), sb.toString());
            }
        });

        rv_list.setLayoutManager(new LinearLayoutManager(this));
        rv_list.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        checkBoxRecyclerAdapter = new CheckBoxRecyclerAdapter(this);
        checkBoxRecyclerAdapter.setItemClickListener(new CheckBoxRecyclerAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                BaseDataEntity baseDataEntity = checkBoxRecyclerAdapter.getData().get(position);
                File f = new File(baseDataEntity.getDesc());
                if (f.isDirectory()) {
                    current = baseDataEntity.getDesc();
                    updateData();
                }
            }

            @Override
            public void onItemChoose() {
                resultList.clear();
                long len = 0L;
                Map<String, Map<Integer, String>> pathChoose = checkBoxRecyclerAdapter.getPathChoose();
                for (Map.Entry<String, Map<Integer, String>> entry : pathChoose.entrySet()) {
                    Map<Integer, String> value = entry.getValue();
                    if (value != null) {
                        for (Map.Entry<Integer, String> paths : value.entrySet()) {
                            String path = paths.getValue();
                            if (path != null) {
                                resultList.add(path);
                                if (new File(path).exists()) {
                                    len += new File(path).length();
                                }
                            }
                        }
                    }
                }

                tv_size.setText("完成(" + resultList.size() + ")");
                if (resultList.size() > 0) {
                    tv_len.setText("已选 " + ConvertUtils.byte2FitMemorySize(len));
                } else {
                    tv_len.setText("");
                }
            }
        });
        rv_list.setAdapter(checkBoxRecyclerAdapter);

        initData();
    }

    /**
     * 初始化顶级目录
     */
    private void initData() {
        tv_top.setText(current);
        String[] volume = SDCardUtils.getVolumePaths(this);
        List<BaseDataEntity> list = new ArrayList<>();
        if (volume != null) {
            for (int i = 0; i < volume.length; i++) {
                roots.add(volume[i]);
                if (i == 0) {
                    list.add(new BaseDataEntity("SD卡", volume[i]));
                } else if (i == 1) {
                    list.add(new BaseDataEntity("扩展卡内存", volume[i]));
                } else {
                    list.add(new BaseDataEntity("扩展卡内存" + i, volume[i]));
                }
            }
        }
        checkBoxRecyclerAdapter.setDataAndCurrent(list, current);
        checkBoxRecyclerAdapter.notifyDataSetChanged();
    }

    /**
     * 更新进入目录，刷新列表
     */
    private void updateData() {
        tv_top.setText(current);
        File[] files = new File(current).listFiles();
        List<BaseDataEntity> list = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                if (!file.getName().startsWith(".")) {
                    BaseDataEntity s = new BaseDataEntity(file.getName(), file.getAbsolutePath());
                    if (file.isDirectory()) {
                        s.setOrderNo(100);
                    } else {
                        s.setOrderNo(1);
                    }
                    s.setWhole(pinyin.getPinYin(s.getInfo()));
                    list.add(s);
                }
            }
        }
        // 排序。 文件夹 / 文件。再名称排序 。
        Collections.sort(list, new OrderComparator());

        checkBoxRecyclerAdapter.setDataAndCurrent(list, current);
        checkBoxRecyclerAdapter.notifyDataSetChanged();
    }

    /**
     * 返回时判断 是否顶级目录。
     */
    @Override
    protected boolean onBack() {
        if ("/".equals(current)) {
            return super.onBack();
        } else if (roots.contains(current)) { // 顶级目录。返回关闭页面
            current = "/";
            initData();
            return true;
        } else { // 非顶级目录，返回下一级
            current = new File(current).getParent();
            updateData();
            return true;
        }
    }
}
