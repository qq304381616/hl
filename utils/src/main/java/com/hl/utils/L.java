package com.hl.utils;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 日志工具类
 */
public class L {

    private static final int LOG_LENGTH = 2000;  // 单条日志长度限制，超出需要截取多次输出，否则可能丢失
    private static final String format = "---》 %s \n" +
            "╔═════════════════════════════════════════════════════════════════════════════════════════════════════════════════\n" +
            "║%s\n" +
            "╚═════════════════════════════════════════════════════════════════════════════════════════════════════════════════";
    private static boolean logSwitch = BuildConfig.DEBUG;
    private static boolean log2FileSwitch = false;
    private static char logFilter = 'v';
    private static String tag = "<hl>";
    private static String dir = "/sdcard/log/";

    private L() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化函数
     * <p>与{@link #getBuilder()}两者选其一</p>
     *
     * @param logSwitch      日志总开关
     * @param log2FileSwitch 日志写入文件开关，设为true需添加权限 {@code <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>}
     * @param logFilter      输入日志类型有{@code v, d, i, w, e}<br>v代表输出所有信息，w则只输出警告...
     * @param tag            标签
     */
    public static void init(boolean logSwitch, boolean log2FileSwitch, char logFilter, String tag) {
        L.logSwitch = logSwitch;
        L.log2FileSwitch = log2FileSwitch;
        L.logFilter = logFilter;
        L.tag = tag;
    }

    /**
     * 获取LogUtils建造者
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    public static void v(Object msg) {
        log(tag, msg == null ? "" : msg.toString(), null, 'i');
    }

    public static void v(String tag, Object msg) {
        log(tag, msg == null ? "" : msg.toString(), null, 'i');
    }

    public static void v(String tag, Object msg, Throwable tr) {
        log(tag, msg == null ? "" : msg.toString(), tr, 'v');
    }

    public static void d(Object msg) {
        log(tag, msg == null ? "" : msg.toString(), null, 'd');
    }

    public static void d(String tag, Object msg) {
        log(tag, msg == null ? "" : msg.toString(), null, 'd');
    }

    public static void d(String tag, Object msg, Throwable tr) {
        log(tag, msg == null ? "" : msg.toString(), tr, 'd');
    }

    public static void i(Object msg) {
        log(tag, msg == null ? "" : msg.toString(), null, 'i');
    }

    public static void i(String tag, Object msg) {
        log(tag, msg == null ? "" : msg.toString(), null, 'i');
    }

    public static void i(String tag, Object msg, Throwable tr) {
        log(tag, msg == null ? "" : msg.toString(), tr, 'i');
    }

    public static void w(Object msg) {
        log(tag, msg == null ? "" : msg.toString(), null, 'w');
    }

    public static void w(String tag, Object msg) {
        log(tag, msg == null ? "" : msg.toString(), null, 'w');
    }

    public static void w(String tag, Object msg, Throwable tr) {
        log(tag, msg == null ? "" : msg.toString(), tr, 'w');
    }

    public static void e(Object msg) {
        log(tag, msg == null ? "" : msg.toString(), null, 'e');
    }

    public static void e(Throwable tr) {
        log(tag, "", tr, 'e');
    }

    public static void e(String tag, Object msg) {
        log(tag, msg == null ? "" : msg.toString(), null, 'e');
    }

    public static void e(String tag, Object msg, Throwable tr) {
        log(tag, msg == null ? "" : msg.toString(), tr, 'e');
    }

    /**
     * 根据tag, msg和等级，输出日志
     *
     * @param tag  标签
     * @param msg  消息
     * @param tr   异常
     * @param type 日志类型
     */
    private static void log(String tag, String msg, Throwable tr, char type) {
        if (logSwitch) {
            msg = msg.replace("\n", "\n║");
            List<String> list = new ArrayList<>();
            String logInfo = generateTag(tag);
            while (true) {
                if (msg.length() > LOG_LENGTH) {
                    list.add(String.format(Locale.getDefault(), format, logInfo, msg.substring(0, LOG_LENGTH)));
                    msg = msg.substring(LOG_LENGTH);
                } else {
                    list.add(String.format(Locale.getDefault(), format, logInfo, msg));
                    break;
                }
            }
            for (String ss : list) {
                if ('e' == type && ('e' == logFilter || 'v' == logFilter)) {
                    Log.e(tag, ss, tr);
                } else if ('w' == type && ('w' == logFilter || 'v' == logFilter)) {
                    Log.w(tag, ss, tr);
                } else if ('d' == type && ('d' == logFilter || 'v' == logFilter)) {
                    Log.d(tag, ss, tr);
                } else if ('i' == type && ('d' == logFilter || 'v' == logFilter)) {
                    Log.i(tag, ss, tr);
                }
            }
            if (log2FileSwitch) {
                log2File(type, tag, logInfo + '\n' + msg + '\n' + Log.getStackTraceString(tr));
            }
        }
    }

    /**
     * 打开日志文件并写入日志
     *
     * @param type    日志类型
     * @param tag     标签
     * @param content 内容
     **/
    private synchronized static void log2File(final char type, final String tag, final String content) {
        if (content == null) return;
        Date now = new Date();
        String date = new SimpleDateFormat("MM-dd", Locale.getDefault()).format(now);
        final String fullPath = dir + date + ".txt";
        if (!FileUtils.createOrExistsFile(fullPath)) return;
        String time = new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(now);
        final String dateLogContent = time + ":" + type + ":" + tag + ":" + content + '\n';
        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedWriter bw = null;
                try {
                    bw = new BufferedWriter(new FileWriter(fullPath, true));
                    bw.write(dateLogContent);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (bw != null) {
                            bw.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 产生tag
     *
     * @return tag
     */
    private static String generateTag(String tag) {
        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
        StackTraceElement caller = stacks[5];
        String format = "Tag[" + tag + "] %s[%s, %d]";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        return String.format(Locale.getDefault(), format, callerClazzName, caller.getMethodName(), caller.getLineNumber());
    }

    public static class Builder {

        private boolean logSwitch = BuildConfig.DEBUG;
        private boolean log2FileSwitch = false;
        private char logFilter = 'v';
        private String tag = "TAG";

        public Builder setLogSwitch(boolean logSwitch) {
            this.logSwitch = logSwitch;
            return this;
        }

        public Builder setLog2FileSwitch(boolean log2FileSwitch) {
            this.log2FileSwitch = log2FileSwitch;
            return this;
        }

        public Builder setLogFilter(char logFilter) {
            this.logFilter = logFilter;
            return this;
        }

        public Builder setTag(String tag) {
            this.tag = tag;
            return this;
        }

        public void create() {
            L.logSwitch = logSwitch;
            L.log2FileSwitch = log2FileSwitch;
            L.logFilter = logFilter;
            L.tag = tag;
        }
    }
}
