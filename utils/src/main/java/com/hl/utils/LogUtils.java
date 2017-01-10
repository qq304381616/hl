package com.hl.utils;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 日志工具类
 */
public class LogUtils {

    /**
     * 是否显示日志
     */
    public static final boolean isShowLog = true;

    /**
     * 是否保存日志
     */
    public static final boolean isSaveLog = false;

    /**
     * 是否给log结尾加统一标志 方便信息过滤
     */
    private static final boolean isExpandMsg = true;

    /**
     * log标记
     */
    private static final String mark = "<HL>  ";

    private static final File logFileName;

    static {
        logFileName = new File("/sdcard/hllog.txt");
        if (!logFileName.getParentFile().exists()) {
            logFileName.getParentFile().mkdirs();
        }
    }

    /**
     * 给log信息加标志。方便过滤无用信息
     *
     * @param msg 原信息
     * @return 加标志后信息
     */
    private static String getMsg(String msg) {
        if (isExpandMsg)
            msg = mark + msg;
        return msg;
    }

    public static void i(String tag, String msg) {
        if (isShowLog) {
            android.util.Log.i(tag, getMsg(msg));
        }
        if (isSaveLog) {
            log2File(tag, getMsg(msg));
        }
    }

    public static void i(String tag, String msg, Throwable t) {
        if (isShowLog) {
            android.util.Log.i(tag, getMsg(msg), t);
        }
        if (isSaveLog) {
            log2File(tag, getMsg(msg));
        }
    }

    public static void e(String tag, String msg) {
        if (isShowLog) {
            android.util.Log.e(tag, getMsg(msg));
        }
        if (isSaveLog) {
            log2File(tag, getMsg(msg));
        }
    }

    public static void e(String tag, boolean msg) {
        if (isShowLog) {
            android.util.Log.e(tag, getMsg(String.valueOf(msg)));
        }
        if (isSaveLog) {
            log2File(tag, getMsg(String.valueOf(msg)));
        }
    }

    public static void e(String tag, String msg, Throwable t) {
        if (isShowLog) {
            android.util.Log.e(tag, getMsg(msg), t);
        }
        if (isSaveLog) {
            log2File(tag, getMsg(msg));
        }
    }

    private static void log2File(String tag, String text) {
        if (text.length() > 1024 * 2) {
            text = text.substring(0, 1024 * 2);
        }
        String msg = DateUtils.FormatDateYMDHMSS() + "	" + tag + "	" + text;
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(logFileName, true);
            bw = new BufferedWriter(fw);
            bw.write(msg);
            bw.write("\r\n");
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
                if (fw != null) {
                    fw.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
