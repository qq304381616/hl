package com.hl.utils.record;

import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Created on 2016/6/8.
 */
public class RecordUtils extends android.support.v7.widget.AppCompatButton implements IRecord {
    private final int Volume_What_100 = 100;
    private final int Time_What_101 = 101;
    private final int CancelRecordWhat_102 = 102;
    private String mFilePath = null;
    private OnFinishedRecordListener finishedListener;
    /**
     * 最短录音时间
     **/
    private int MIN_INTERVAL_TIME = 1000;
    /**
     * 最长录音时间
     **/
    private int MAX_INTERVAL_TIME = 1000 * 60;
    private long mStartTime;
    private MediaRecorder mRecorder;
    private ObtainDecibelThread mthread;
    private Handler mVolumeHandler;

    public RecordUtils(Context context) {
        super(context);
        init();
    }

    public RecordUtils(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RecordUtils(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 保存路径，为空取默认值
     */
    public void setSavePath(String path) {
        mFilePath = path;
    }

    /****
     * 设置最大时间。15秒-10分钟
     *
     * @param time 单位秒
     */
    public void setMaxIntervalTime(int time) {
        if (time > 15 && time < 10 * 60) {
            MAX_INTERVAL_TIME = time * 1000;
        }
    }

    /**
     * 录音完成的回调
     *
     * @param listener
     */
    public void setOnFinishedRecordListener(OnFinishedRecordListener listener) {
        finishedListener = listener;
    }


    private void init() {
        mVolumeHandler = new ShowVolumeHandler();
    }

    int startY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) event.getY();
                startRecording();
                break;
            case MotionEvent.ACTION_UP:
                int endY = (int) event.getY();
                if (startY < 0)
                    return true;
//                if (endY - startY < CANCLE_LENGTH) {
//                    cancelRecord();
//                } else {
//                    finishRecord();
//                }
                break;
            case MotionEvent.ACTION_MOVE:
                int tempNowY = (int) event.getY();
                if (startY < 0)
                    return true;
//                if (tempNowY - startY < CANCLE_LENGTH) {
//                    mTitleTv.setText(getContext().getString(R.string.zeffect_recordbutton_releasing_finger_to_cancal_send));
//                } else {
//                    mTitleTv.setText(getContext().getString(R.string.zeffect_recordbutton_finger_up_to_cancal_send));
//                }
                break;
            case MotionEvent.ACTION_CANCEL:
                cancelRecord();
                break;
        }

        return true;
    }

    private void finishRecord() {
        stopRecording();
        long intervalTime = System.currentTimeMillis() - mStartTime;
        if (intervalTime < MIN_INTERVAL_TIME) {
            Toast.makeText(getContext(), "时间太短", Toast.LENGTH_SHORT).show();
            File file = new File(mFilePath);
            if (file.exists())
                file.delete();
            return;
        }

        if (finishedListener != null)
            finishedListener.onFinishedRecord(mFilePath);
    }

    private void cancelRecord() {
        stopRecording();
        File file = new File(mFilePath);
        if (file.exists())
            file.delete();
    }

    public void startRecording() {
        mStartTime = System.currentTimeMillis();
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mRecorder.setOutputFile(mFilePath);
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mRecorder.start();
        mthread = new ObtainDecibelThread();
        mthread.start();

    }

    public void stopRecording() {
        if (mthread != null) {
            mthread.exit();
            mthread = null;
        }
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

    private class ObtainDecibelThread extends Thread {

        private volatile boolean running = true;

        public void exit() {
            running = false;
        }

        @Override
        public void run() {
            while (running) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mRecorder == null || !running) {
                    break;
                }
                if (System.currentTimeMillis() - mStartTime >= MAX_INTERVAL_TIME) {
                    // 如果超过最长录音时间
                    mVolumeHandler.sendEmptyMessage(CancelRecordWhat_102);
                }
                //发送时间
                mVolumeHandler.sendEmptyMessage(Time_What_101);
                //
                int x = mRecorder.getMaxAmplitude();
                if (x != 0) {
                    int f = (int) (20 * Math.log(x) / Math.log(10));
                    Message msg = new Message();
                    msg.obj = f;
                    msg.what = Volume_What_100;
                    mVolumeHandler.sendMessage(msg);
                }

            }
        }

    }

    private DialogInterface.OnDismissListener onDismiss = new DialogInterface.OnDismissListener() {

        @Override
        public void onDismiss(DialogInterface dialog) {
            stopRecording();
        }
    };

    class ShowVolumeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Volume_What_100:
                    int tempVolumeMax = (int) msg.obj;
                    setLevel(tempVolumeMax);
                    break;
                case Time_What_101:
                    long nowTime = System.currentTimeMillis();
                    int time = ((int) (nowTime - mStartTime) / 1000);
                    int second = time % 60;
                    int mil = time / 60;
//                    if (mil < 10) {
//                        if (second < 10)
//                            mTimeTv.setText("0" + mil + ":0" + second);
//                        else
//                            mTimeTv.setText("0" + mil + ":" + second);
//                    } else if (mil >= 10 && mil < 60) {
//                        if (second < 10)
//                            mTimeTv.setText(mil + ":0" + second);
//                        else
//                            mTimeTv.setText(mil + ":" + second);
//                    }
                    break;
                case CancelRecordWhat_102:
                    finishRecord();
                    break;
            }
        }
    }

    private void setLevel(int level) {
//        if (mImageView != null)
//            mImageView.getDrawable().setLevel(3000 + 6000 * level / 100);
    }

    /**
     * 完成录音回调
     */
    public interface OnFinishedRecordListener {
        public void onFinishedRecord(String audioPath);
    }
}
