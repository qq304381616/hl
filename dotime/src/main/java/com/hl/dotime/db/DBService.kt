package com.hl.dotime.db

import android.content.Context
import com.hl.dotime.utils.Utils
import java.io.File

class DBService {
    companion object {

        private const val TO_PATH = "/sdcard/do_time/"

        fun backup(c: Context): Boolean {
            val re = c.getDatabasePath(MySQLiteOpenHelper.dbName)
            if (!File(TO_PATH).exists()) {
                File(TO_PATH).mkdirs()
            }
            if (File(TO_PATH, MySQLiteOpenHelper.dbName).exists()) {
                File(TO_PATH, MySQLiteOpenHelper.dbName).delete()
            }
            Utils.copyFile(re.absolutePath, File(TO_PATH, MySQLiteOpenHelper.dbName).absolutePath)
            return true
        }

        fun recover(c: Context): Boolean {
            val re = c.getDatabasePath(MySQLiteOpenHelper.dbName)
            if (!File(TO_PATH, MySQLiteOpenHelper.dbName).exists()) {
                return false
            }
            if (re.exists()) {
                re.delete()
            }
            Utils.copyFile(File(TO_PATH, MySQLiteOpenHelper.dbName).absolutePath, re.absolutePath)
            return true;
        }
    }
}