package com.hl.dotime.utils

import java.io.FileWriter
import java.io.IOException
import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.util.*

class CrashUtils private constructor() : Thread.UncaughtExceptionHandler {

    private var mHandler: Thread.UncaughtExceptionHandler? = null
    private var crashDir = Utils.getAppSDCardPath("crash")

    fun init() {
        mHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        val now = SimpleDateFormat("yy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        if (!crashDir.exists()) {
            crashDir.mkdirs()
        }
        Thread(Runnable {
            var pw: PrintWriter? = null
            try {
                pw = PrintWriter(FileWriter(crashDir.absolutePath + now + ".txt", false))
                throwable.printStackTrace(pw)
                var cause: Throwable? = throwable.cause
                while (cause != null) {
                    cause.printStackTrace(pw)
                    cause = cause.cause
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                pw?.close()
            }
        }).start()
        if (mHandler != null) {
            mHandler!!.uncaughtException(thread, throwable)
        }
    }

    companion object {

        @Volatile
        private var mInstance: CrashUtils? = null

        val instance: CrashUtils?
            get() {
                if (mInstance == null) {
                    synchronized(CrashUtils::class.java) {
                        if (mInstance == null) {
                            mInstance = CrashUtils()
                        }
                    }
                }
                return mInstance
            }
    }
}
