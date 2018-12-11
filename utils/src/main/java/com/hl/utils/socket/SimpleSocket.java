package com.hl.utils.socket;

import android.content.Context;

import com.hl.utils.FileUtils;
import com.hl.utils.L;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * 建立socket链接
 */
public class SimpleSocket {

    public static final short LOGIN = 1001; // 登陆
    public static final short LINECHECK = 1005; // 心跳
    public final static short FILE_GET_FILE_NAME = 5001; // 获取文件名称
    public final static short FILE_DATA_UPLOAD = 5002; // 文件上传
    public final static short FILE_UPLOAD_INDEX = 5005; // 获取文件已上传进度

    private String llh;
    private String username;
    private String token;
    private String areaId;
    private String userSsiId;
    private String idfId;

    private boolean isLogin = false;// 记录是否登陆成功
    private static Socket socket = null;
    private static final int NO_DADA_WAIT_TIME = 50;
    private static DataOutputStream dos;
    private static DataInputStream dis;
    private static int hearttime = 30000;// 默认心跳30
    private static final String ip = "";
    private static final int port = 6000;
    private SocketListener mSocketListener;
    private static int buffsize = 10240;
    private GoloSocketThread socketThread;
    private static SimpleSocket instance;

    private Context mContext;

    private int bufferSize; // 登录时返回的文件上传包大小

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        if (bufferSize > Short.MAX_VALUE) {
            L.e("TAG", "bufferSize 超过最大限制");
        }
        this.bufferSize = bufferSize;
    }

    private SimpleSocket(Context context, String llh, String username, String token, String areaId, String userSsiId, String idfId) {
        instance = this;
        mContext = context;
        this.llh = llh;
        this.username = username;
        this.token = token;
        this.areaId = areaId;
        this.userSsiId = userSsiId;
        this.idfId = idfId;
    }

    public static SimpleSocket getInstance(Context context, String llh, String username, String token, String areaId, String userSsiId, String idfId) {
        if (instance == null) {
            instance = new SimpleSocket(context, llh, username, token, areaId, userSsiId, idfId);
        }
        return instance;
    }

    public static SimpleSocket getInstance() {
        return instance;
    }

    public synchronized void runThread() {
        socketThread = new GoloSocketThread();
        socketThread.start();
        L.e( "socket 开始连接 ");
    }

    class GoloSocketThread extends Thread {

        final byte[] buffer = new byte[Short.MAX_VALUE];
        public boolean isClose = false, ok = true, newFriends = false, run = true;
        int readNullNum;
        int readNullMaxNum = hearttime / NO_DADA_WAIT_TIME;
        int thisCount = 0;
        int index = 0, packlength = 0;
        int length;

        public GoloSocketThread() {
            isClose = false;
        }

        public void run() {
            while (!isClose) {// 用户退出时用到
                try {
                    isLogin = false;
                    index = 0;
                    packlength = 0;
                    readNullMaxNum = hearttime / NO_DADA_WAIT_TIME;
                    thisCount = 0;

                    closeCurrentSocket();
                    socket = new Socket();
                    socket.connect(new InetSocketAddress(ip, port), 5000);
                    socket.setSoTimeout(8000);
                    dos = new DataOutputStream(socket.getOutputStream());
                    dis = new DataInputStream(socket.getInputStream());
                    readNullNum = 0;
                    run = true;
                    login(llh, username, token);
                    while (run) {// 判断链路正常的时候
                        if (isClose) {
                            run = false;
                            break;
                        }
                        length = dis.available();
                        if (length == 0) {
                            ++readNullNum;
                            Thread.sleep(NO_DADA_WAIT_TIME);
                        } else if (index < 2) {
                            thisCount = dis.read(buffer, index, 2 - index);
                            if (thisCount > 0) {
                                index += thisCount;
                                readNullNum = 0;
                                // LogG.e( "readNullNum = 0 ； index < 2 时");
                            } else {
                                run = false;
                                break;
                            }
                        } else {
                            thisCount = dis.read(buffer, index, packlength - index);
                            if (thisCount > 0) {
                                index += thisCount;
                                readNullNum = 0;
                                // LogG.e( "readNullNum = 0 ； else 时");
                            } else {
                                run = false;
                                break;
                            }
                            if (index == packlength) {
                                ByteBuffer packbuffer = ByteBuffer.allocate(buffsize);
                                packbuffer.put(buffer, 0, packlength);
                                index = packlength = 0;
                                onGetInfo(packbuffer);
                            }
                        }

                        if (readNullNum == readNullMaxNum) {
                            sendInfo(LINECHECK, 0, null);// 链路检测
                        } else if (readNullNum >= readNullMaxNum + 200) {
                            run = false;
                            break;
                        }

                        if (packlength == 0 && index >= 2) {
                            packlength = getShort(buffer, 0);
                            if (packlength < 8 || packlength > buffsize) {// 非法请求,重新建链
                                run = false;
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    run = false;
                    // LogG.toE( "Socket 链接异常", e);
                } finally {

                    try {
                        if (dis != null)
                            dis.close();
                        dis = null;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        if (dos != null)
                            dos.close();
                        dos = null;
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    try {
                        if (socket != null && !socket.isClosed() && socket.isConnected()) {
                            socket.close();
                        }
                        socket = null;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        Thread.sleep(2000);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }

                }
            }
        }

        private void mSLeep(long time) {
            try {
                sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public short getShort(byte[] b, int index) {
        return (short) (((b[index] << 8) | b[index + 1] & 0xff));
    }

    // 传数据
    private void onGetInfo(final ByteBuffer buffer) throws Exception {

        buffer.position(0);
        short len = buffer.getShort();// 包长
        short command = buffer.getShort(); // 命令
        int extra = buffer.getInt(); // extra
        byte[] b = new byte[len - 8];
        buffer.get(b); // 返回数据

        SocketResult result = new SocketResult();
        result.setLen(len);
        result.setCommand(command);
        result.setExtra(extra);
        result.setResult(new String(b));

        L.e( result.toString());

        if (mSocketListener != null) {
            mSocketListener.onGetInfo(result);
        }
    }

    /**
     * 登录
     *
     * @param username
     * @param password
     * @throws IOException
     */
    public void login(String llh, String username, String password) throws IOException {

        try {
            JSONObject json = new JSONObject();
            json.put("llh", llh);
            json.put("mob", username);
            json.put("token", "123456"); // TODO
            json.put("areaId", "999999"); // TODO

            String url = json.toString();

            // 发送长连接登录动作
            sendInfo(LOGIN, 0, url.getBytes());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public synchronized void closeAndStart() {
        close();
        getInstance(mContext, llh, username, token, areaId, userSsiId, idfId);
        runThread();
    }

    public synchronized void reConnect() {
        L.e( "socket reConnect()");
        if (socketThread != null && isLogin)
            socketThread.run = false;
    }

    public synchronized void close() {
        L.e( "socket 关闭连接 ");
        if (socketThread != null) {
            socketThread.run = false;
            socketThread.isClose = true;
        }
        instance = null;

        try {
            closeCurrentSocket();
            if (socket != null)
                socket.close();
            socket = null;
            if (socketThread != null)
                socketThread.interrupt();
            socketThread = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeCurrentSocket() {
        try {
            if (dis != null)
                dis.close();
            dis = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (dos != null)
                dos.close();
            dos = null;
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        if (socket != null) {
            try {
                socket.close();
                socket = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isClose() {
        if (socket == null) {
            return true;
        }
        return socket.isClosed();
    }

    public boolean isConnected() {
        if (socket == null) {
            return false;
        }
        return socket.isConnected();
    }

    /**
     * 发送信息
     */
    public boolean sendInfo(short com, int extra, byte[] bt) {
        try {
            short packlen = 8;
            if (bt != null)
                packlen += bt.length;
            dos.writeShort(packlen);
            dos.writeShort(com);
            dos.writeInt(extra);
            if (bt != null && bt.length > 0)
                dos.write(bt);
            dos.flush();

            String s = "";
            if (bt != null) {
                s = new String(bt);
            }
            L.e( "Socket sendInfo : " + com + " | " + s);
        } catch (IOException e) {
            e.printStackTrace();
            reConnect();
            return false;
        }
        return true;
    }

    int i = 1;

    public void sendFile(FileEntity f, int uploadType) {
        String path = f.path;
        byte[] b = FileUtils.getBuffer(path);

        L.e( "文件长度: " + b.length + " | " + "bufferSize: " + bufferSize);

        try {
            for (int i = f.getIndex(); i < b.length; i += bufferSize - 50) {
                byte[] data; // 需要截取的部分文件

                byte[] type = new byte[252];
                JSONObject json = new JSONObject();
                json.put("llh", llh);
                json.put("userSsiId", userSsiId);
                json.put("idfId", idfId);
                json.put("filetype", f.getType());
                json.put("uploadType", uploadType);

                json.put("startIndex", f.getIndex());
                json.put("uuid", f.getUuid());

                if ((i + (bufferSize - 50)) < b.length) {
                    json.put("endTag", "0");
                    data = new byte[bufferSize - 50];
                } else {
                    json.put("endTag", "1");
                    data = new byte[b.length - i];
                }

                byte[] s = json.toString().getBytes();
                System.arraycopy(s, 0, type, 0, s.length);
                System.arraycopy(b, i, data, 0, data.length);
//                byte[] data5 = CommonUtils.add(type, data);

//                sendInfo(SimpleSocket.FILE_DATA_UPLOAD, 0, data5);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setSocketListener(SocketListener mSocketListener) {
        this.mSocketListener = mSocketListener;
    }

    public interface SocketListener {
        public void onGetInfo(SocketResult result) throws Exception;
    }

    public class SocketResult {
        short len;
        short command;
        int extra;
        String result;

        public short getLen() {
            return len;
        }

        public void setLen(short len) {
            this.len = len;
        }

        public short getCommand() {
            return command;
        }

        public void setCommand(short command) {
            this.command = command;
        }

        public int getExtra() {
            return extra;
        }

        public void setExtra(int extra) {
            this.extra = extra;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        @Override
        public String toString() {
            return "SocketResult [len=" + len + ", command=" + command + ", extra=" + extra + ", result=" + result + "]";
        }
    }

    public static class FileEntity {

        public FileEntity(String path, String uuid, int type, int index) {
            this.index = index;
            this.path = path;
            this.uuid = uuid;
            this.type = type;
        }

        private String path;
        private String uuid;
        private int type;
        private int index;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

    }
}