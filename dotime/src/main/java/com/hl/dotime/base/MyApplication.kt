package com.hl.dotime.base

import android.app.Application
import com.hl.dotime.utils.CrashUtils

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        CrashUtils.instance?.init()
    }
}