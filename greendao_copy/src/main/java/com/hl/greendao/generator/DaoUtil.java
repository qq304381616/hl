package com.hl.greendao.generator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class DaoUtil {

    public static String dbName(String javaName) {
        StringBuilder builder = new StringBuilder(javaName);
        for (int i = 1; i < builder.length(); i++) {
            boolean lastWasUpper = Character.isUpperCase(builder.charAt(i - 1));
            boolean isUpper = Character.isUpperCase(builder.charAt(i));
            if (isUpper && !lastWasUpper) {
                builder.insert(i, '_');
                i++;
            }
        }
        return builder.toString().toUpperCase();
    }

    public static String capFirst(String string) {
        return Character.toUpperCase(string.charAt(0)) + (string.length() > 1 ? string.substring(1) : "");
    }


    public static byte[] readAllBytes(File file) throws IOException {
        FileInputStream is = new FileInputStream(file);
        try {
            return readAllBytes(is);
        } finally {
            is.close();
        }
    }

    private static byte[] readAllBytes(FileInputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        copyAllBytes(in, out);
        return out.toByteArray();
    }

    private static int copyAllBytes(FileInputStream in, ByteArrayOutputStream out) throws IOException {
        int byteCount = 0;
        byte[] buffer = new byte[4096];
        while (true) {
            int read = in.read(buffer);
            if (read == -1) {
                break;
            }
            out.write(buffer, 0, read);
            byteCount += read;
        }
        return byteCount;
    }
}
