package com.hl.dotime.base

import android.annotation.SuppressLint
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.hl.dotime.R

@SuppressLint("Registered")
open class BaseActivity : com.hl.base.BaseActivity() {

    private var toolbar: Toolbar? = null

    fun initToolbar() {
        toolbar = findViewById(R.id.toolbar)
        if (toolbar != null) {
            toolbar?.setTitleTextColor(resources.getColor(R.color.white))
            setSupportActionBar(toolbar)
            supportActionBar?.setHomeButtonEnabled(true) //设置返回键可用
            supportActionBar?.setDisplayHomeAsUpEnabled(true) // 给左上角图标的左边加上一个返回的图标 。对应ActionBar.DISPLAY_HOME_AS_UP
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() == android.R.id.home) {
            onBack()
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        onBack()
    }
}