package com.hl.widget.h5;

import com.hl.utils.MD5Utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

/**
 * 接口请求工具类
 */
public class HttpUtils {

    private long count;

    public void setCount(long count) {
        this.count = count;
    }

    public long getCount() {
        return count;
    }

    /**
     * 上传文件,android 实现
     */
    public String uploadFile(String inputName, String urlStr, Map<String, String> textMap, Map<String, String> fileMap) {
        String res = "";
        HttpURLConnection conn = null;
        String BOUNDARY = "---------------------------7d4a6d158c9"; // boundary就是request头和上传文件内容的分隔符
        try {
            java.net.URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

            OutputStream out = new DataOutputStream(conn.getOutputStream());
            if (textMap != null && textMap.size() != 0) {
                StringBuffer strBuf = new StringBuffer();
                Iterator iter = textMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String name = (String) entry.getKey();
                    String value = (String) entry.getValue();
                    if (value == null) {
                        continue;
                    }
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\"" + name + "\"\r\n\r\n");
                    strBuf.append(value);
                }
                out.write(strBuf.toString().getBytes());
            }

            // file
            if (fileMap != null && fileMap.size() != 0) {
                Iterator iter = fileMap.entrySet().iterator();
                long currentCount = 0;
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String name = (String) entry.getKey();
                    String value = (String) entry.getValue();
                    if (value == null) {
                        continue;
                    }
                    File file = new File(value);
                    String filename = file.getName();
                    String contentType = "application/octet-stream";

                    StringBuffer strBuf = new StringBuffer();
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename + "\"\r\n");
                    strBuf.append("Content-Type:" + contentType + "\r\n\r\n");

                    out.write(strBuf.toString().getBytes());

                    DataInputStream in = new DataInputStream(new FileInputStream(file));
                    int len = 0;
                    byte[] bufferOut = new byte[8 * 1024];
                    while ((len = in.read(bufferOut)) != -1) {
                        out.write(bufferOut, 0, len);
                        currentCount += len;
                        if (mCallback != null) {
                            mCallback.progress(currentCount);
                        }
                    }
                    in.close();
                }
            }

            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            out.close();

            int code = conn.getResponseCode();
            // 读取返回数据
            StringBuffer strBuf = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                strBuf.append(line).append("\n");
            }
            res = strBuf.toString();
            reader.close();
            reader = null;
        } catch (Exception e) {
            e.printStackTrace();
            res = e.getMessage();
            if (mCallback != null) {
                mCallback.failure();
            }
            return res;
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        if (mCallback != null) {
            mCallback.success(res);
        }
        return res;
    }

    /**
     * 文件下载
     *
     * @param urlStr 路径
     */
    public void downloadFile(String urlStr, String savePath) {
        OutputStream output = null;
        try {
            String filename = MD5Utils.get32MD5(urlStr);
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Accept-Encoding", "identity");
            count = conn.getContentLength();
            InputStream input = conn.getInputStream();

            if (!new File(savePath).exists()) {
                new File(savePath).mkdirs();
            }
            if (new File(savePath, filename).exists()) {
                new File(savePath, filename).delete();
            }
            new File(savePath, filename).createNewFile();
            output = new FileOutputStream(new File(savePath, filename));
            byte[] buffer = new byte[4 * 1024];
            int len;
            long currentCount = 0;
            while ((len = input.read(buffer)) != -1) {
                output.write(buffer, 0, len);
                currentCount += len;
                if (mCallback != null) {
                    mCallback.progress(currentCount);
                }
            }
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
            if (mCallback != null) {
                mCallback.failure();
            }
            return;
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (mCallback != null) {
            mCallback.success("success");
        }
    }

    public interface FileCallback {
        void success(String result);

        void failure();

        void progress(long progress);
    }

    private FileCallback mCallback;

    public void setCallback(FileCallback c) {
        mCallback = c;
    }
}
