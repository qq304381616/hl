package com.hl.utils;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * gzip压缩工具类 需要导入 ant.jar
 * 两种方式，分隔线分隔
 */
public class GzipUtil {
    private int bufferSize = 32768;// default,32KB

    public GzipUtil() {
    }

    public GzipUtil(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public byte[] gzip(byte[] byteArray) throws IOException {
        return execGzip(byteArray);
    }

    public byte[] gunzip(byte[] byteArrayCompressed) throws IOException {
        return execGunzip(byteArrayCompressed, bufferSize);
    }

    /**
     * gzip压缩
     *
     * @param byteArray 待压缩的数据（字节数组形式）
     * @return 压缩后的数据（字节数组形式）
     * @throws IOException
     */
    public static byte[] execGzip(byte[] byteArray) throws IOException {
        GZIPOutputStream gzipOutputStream = null;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(byteArray.length);
            gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gzipOutputStream.write(byteArray);
            gzipOutputStream.flush();
            gzipOutputStream.finish();

            byte[] byteArrayCompressed = byteArrayOutputStream.toByteArray();
            return byteArrayCompressed;
        } finally {
            if (gzipOutputStream != null) {
                gzipOutputStream.close();
            }
        }
    }

    /**
     * 解压缩
     *
     * @param byteArrayCompressed 被压缩的数据（字节数组形式）
     * @param bufferSize          解压缩过程的buffer大小
     * @return 还原的数据（字节数组形式）
     * @throws IOException
     */
    public static byte[] execGunzip(byte[] byteArrayCompressed, int bufferSize) throws IOException {
        GZIPInputStream gzipInputStream = null;
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayCompressed);
            gzipInputStream = new GZIPInputStream(byteArrayInputStream);
            byte[] buffer = new byte[bufferSize];
            int readInSize = 0;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(byteArrayCompressed.length);
            while ((readInSize = gzipInputStream.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, readInSize);
            }
            byte[] byteArrayUncompressed = byteArrayOutputStream.toByteArray();
            return byteArrayUncompressed;
        } finally {
            if (gzipInputStream != null) {
                gzipInputStream.close();
            }
        }

    }

    /***********************************************************************************/

    /**
     * 压缩文件
     */
    public static void zipFile(String srcPathName, String zipFile) {
        File file = new File(srcPathName);
        if (!file.exists())
            throw new RuntimeException(srcPathName + "不存在！");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream, new CRC32()); // 不加CRC32，一样可以生成文件。关于数据如何校验，请高手指点
            ZipOutputStream out = new ZipOutputStream(cos);
            out.setEncoding("gbk"); // 如果不加此句，压缩文件依然可以生成，只是在打开和解压的时候，会显示乱码，但是还是会解压出来
            String basedir = "";
            compress(file, out, basedir);
            out.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void unzipFile(final String zipedFilePath, final String unzipDestDirPath, final ZipCallback callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    ZipFile zf = new ZipFile(zipedFilePath, "GBK");
                    Enumeration e = zf.getEntries();
                    ZipEntry zipEntry = null;

                    byte[] buff = new byte[1024 * 4];
                    int readed = 0;
                    while (e.hasMoreElements()) {
                        zipEntry = (ZipEntry) e.nextElement();
                        String entryName = zipEntry.getName();
                        if (!new File(unzipDestDirPath).isDirectory()) {
                            new File(unzipDestDirPath).mkdirs();
                        }
                        File f = new File(unzipDestDirPath + "/" + entryName);
                        if (!f.exists()) {
                            if (!(f.getParentFile().exists())) {
                                f.getParentFile().mkdirs();
                            }
                            f.createNewFile();
                        }
                        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(f));
                        BufferedInputStream bin = new BufferedInputStream(zf.getInputStream(zipEntry));
                        while ((readed = bin.read(buff)) > 0) {
                            out.write(buff, 0, readed);
                        }
                        out.close();
                        bin.close();
                    }
                    zf.close();
                    System.out.println("解压缩完成！");
                    if (callback != null) {
                        callback.onSuccessCallback();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("解压缩失败！");
                    new File(unzipDestDirPath).delete(); // 如果解压缩失败，删除空目录
                    if (callback != null) {
                        callback.onFailureCallback();
                    }
                }
            }
        }).start();
    }

    public interface ZipCallback {
        public void onSuccessCallback();

        public void onFailureCallback();
    }

    private static void compress(File file, ZipOutputStream out, String basedir) {
        /* 判断是目录还是文件 */
        if (file.isDirectory()) {
            compressDirectory(file, out, basedir);
        } else {
            compressFile(file, out, basedir);
        }
    }

    /**
     * 压缩一个目录
     */
    private static void compressDirectory(File dir, ZipOutputStream out, String basedir) {
        if (!dir.exists())
            return;

        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
			/* 递归 */
            compress(files[i], out, basedir + dir.getName() + "/");
        }
    }

    /**
     * 压缩一个文件
     */
    private static void compressFile(File file, ZipOutputStream out, String basedir) {
        if (!file.exists()) {
            return;
        }
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            ZipEntry entry = new ZipEntry(basedir + file.getName());
            out.putNextEntry(entry);
            int count;
            byte data[] = new byte[1024 * 8];
            while ((count = bis.read(data, 0, 1024 * 8)) != -1) {
                out.write(data, 0, count);
            }
            bis.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
