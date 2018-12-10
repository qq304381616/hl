package com.hl.knowledge;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.hl.base.BaseActivity;

/**
 * 安卓Map集合
 */
public class AndroidMapActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        setContentView(tv);

        StringBuilder sb = new StringBuilder();
        sb.append("HashMap<String, String>" +"\n" );
        sb.append("arrayMap<String, String>" +"\n" );
        sb.append("supportArrayMap<String, String>" +"\n" );
        sb.append("simpleArrayMap<String, String>" +"\n" );
        sb.append("SparseArray<String>" +"\n" );
        sb.append("LongSparseArray<String>" +"\n" );
        sb.append("SparseLongArray<String>" +"\n" );

        tv.setText(sb.toString());

//        java.util.HashMap<String, String> hashMap = new java.util.HashMap<String, String>(16);
//        hashMap.put("key", "value");
//        hashMap.get("key");
//        hashMap.entrySet().iterator();
//
//        android.util.ArrayMap<String, String> arrayMap = new android.util.ArrayMap<String, String>(16);
//        arrayMap.put("key", "value");
//        arrayMap.get("key");
//        arrayMap.entrySet().iterator();
//
//        android.support.v4.util.ArrayMap<String, String> supportArrayMap =
//                new android.support.v4.util.ArrayMap<String, String>(16);
//        supportArrayMap.put("key", "value");
//        supportArrayMap.get("key");
//        supportArrayMap.entrySet().iterator();
//
//        android.support.v4.util.SimpleArrayMap<String, String> simpleArrayMap =
//                new android.support.v4.util.SimpleArrayMap<String, String>(16);
//        simpleArrayMap.put("key", "value");
//        simpleArrayMap.get("key");
//        //simpleArrayMap.entrySet().iterator();      <- will not compile
//
//        android.util.SparseArray<String> sparseArray = new android.util.SparseArray<String>(16);
//        sparseArray.put(10, "value");
//        sparseArray.get(10);
//
//        android.util.LongSparseArray<String> longSparseArray = new android.util.LongSparseArray<String>(16);
//        longSparseArray.put(10L, "value");
//        longSparseArray.get(10L);
//
//        android.util.SparseLongArray sparseLongArray = new android.util.SparseLongArray(16);
//        sparseLongArray.put(10, 100L);
//        sparseLongArray.get(10);

    }
}
