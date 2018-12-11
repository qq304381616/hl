package com.hl.dotime.utils

import android.content.Context
import android.graphics.Color
import android.support.graphics.drawable.VectorDrawableCompat
import android.widget.ImageView
import java.io.File

class Utils {

    companion object {
        fun format(s: Int): String {
            if (s >= 10) {
                return s.toString()
            }
            return "0$s"
        }

        fun setSVGColor(c: Context, iv: ImageView, id: Int, color: String) {
            val vectorDrawableCompat = VectorDrawableCompat.create(c.getResources(), id, c.getTheme());
            vectorDrawableCompat?.mutate()
            vectorDrawableCompat?.setTint(Color.parseColor(color))
            iv.setImageDrawable(vectorDrawableCompat)
        }

        fun copyFile(reFile: String, toFile: String) {
            val re = File(reFile)
            val to = File(toFile)
            to.appendBytes(re.readBytes())
        }

        /**
         * 返回当前程序版本名
         */
        fun getAppVersionCode(c: Context): String {
            try {
                val pm = c.getPackageManager();
                val pi = pm.getPackageInfo(c.getPackageName(), 0);
                return pi.versionName;
                // versioncode = pi.versionCode;
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return "";
        }
    }
}