package com.hl.dotime.utils

import com.hl.dotime.entity.FilterCount
import com.hl.utils.L
import java.text.SimpleDateFormat
import java.util.*

class DateUtils {

    companion object {
        fun getTime(time: Long): String {
            val h = time / 1000 / 60 / 60
            val m = time / 1000 / 60 % 60
            val s = time / 1000 % 60
            return Utils.format(h.toInt()) + ":" + Utils.format(m.toInt()) + ":" + Utils.format(s.toInt())
        }

        fun getYMD(time: Long): String {
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return format.format(time)
        }

        fun getYMDHMS(time: Long): String {
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            return format.format(time)
        }

        fun getSeconds(): Long {
            return System.currentTimeMillis() / 1000 * 1000 // 精确到秒，去除毫秒
        }

        fun getFilterTime(type: Int, current: Long, step: Int): FilterCount {
            L.e("type: $type ")
            L.e("current: $current ")
            L.e("step: $step ")
            val result: Array<Long>
            val c = Calendar.getInstance()
            if (current != -1L) {
                c.timeInMillis = current
            }
            c.firstDayOfWeek = Calendar.MONDAY; // 星期一 是0

            val year = c.get(Calendar.YEAR)//获取年份
            val month = c.get(Calendar.MONTH)//获取月份 0-11
            val day = c.get(Calendar.DATE)//获取日

            var start = Calendar.getInstance()
            var end = Calendar.getInstance()

            var text = ""
            when (type) {
                0 -> { // 日
                    c.add(Calendar.DATE, step)
                    start = c.clone() as Calendar
                    end = c.clone() as Calendar

                    text = c.get(Calendar.DAY_OF_MONTH).toString() + "日"
                }
                1 -> { // 周
                    var s = 0
                    if (step == -1) s = -7
                    if (step == 1) s = 7
                    c.add(Calendar.DATE, s)
                    c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                    start = c.clone() as Calendar
                    c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                    end = c.clone() as Calendar

                    text = c.get(Calendar.WEEK_OF_YEAR).toString() + "周"
                }
                2 -> { // 月
                    c.add(Calendar.MONTH, step)
                    c.set(Calendar.DATE, 1)
                    start = c.clone() as Calendar
                    c.set(Calendar.DATE, c.getActualMaximum(Calendar.DAY_OF_MONTH))
                    end = c.clone() as Calendar

                    text = (c.get(Calendar.MONTH) + 1).toString() + "月"
                }
                3 -> { // 季
                    var s = 0
                    if (step == -1) s = -3
                    if (step == 1) s = 3
                    c.set(Calendar.MONTH, month / 3 * 3)
                    c.add(Calendar.MONTH, s)
                    c.set(Calendar.DATE, 1)
                    start = c.clone() as Calendar
                    c.add(Calendar.MONTH, 2)
                    c.set(Calendar.DATE, c.getActualMaximum(Calendar.DAY_OF_MONTH))
                    end = c.clone() as Calendar

                    text = (c.get(Calendar.MONTH) / 3 + 1).toString() + "季"
                }
                4 -> { // 年
                    c.add(Calendar.YEAR, step)
                    c.set(Calendar.MONTH, 0)
                    c.set(Calendar.DATE, 1)
                    start = c.clone() as Calendar
                    c.set(Calendar.MONTH, 11)
                    c.set(Calendar.DATE, 31)
                    end = c.clone() as Calendar

                    text = (c.get(Calendar.YEAR)).toString() + "年"
                }
            }
            start.set(Calendar.HOUR, 0)
            start.set(Calendar.MINUTE, 0)
            start.set(Calendar.SECOND, 0)
            end.set(Calendar.HOUR, 23)
            end.set(Calendar.MINUTE, 59)
            end.set(Calendar.SECOND, 59)
            return FilterCount(start.timeInMillis, end.timeInMillis, text)
        }
    }
}