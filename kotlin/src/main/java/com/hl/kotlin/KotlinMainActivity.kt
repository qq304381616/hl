package com.hl.kotlin

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.hl.base.BaseActivity
import com.hl.utils.L

class KotlinMainActivity :BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kotlin_activity_main)
        initToolbar(true)

        findViewById<TextView>(R.id.tv_global_scope).setOnClickListener {
            L.e("")
        }
    }

}