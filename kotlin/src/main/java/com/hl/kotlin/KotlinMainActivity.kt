package com.hl.kotlin

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.hl.base.BaseActivity

class KotlinMainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kotlin_activity_main)
        initToolbar(true)

        // 协程
        findViewById<TextView>(R.id.tv_global_scope).setOnClickListener {
            startActivity(Intent(this, GlobalScopeActivity::class.java))
        }
    }

}