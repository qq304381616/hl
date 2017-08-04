package com.hl.utils.record;

import com.czt.mp3recorder.MP3Recorder;

import java.io.File;
import java.io.IOException;

/**
 * Created on 2017/7/31.
 */

public class Mp3RecordImpl implements IRecord {

    private MP3Recorder mRecorder;

    @Override
    public void startRecording() {
        try {
            mRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stopRecording() {
        mRecorder.stop();
    }

    @Override
    public void setSavePath(String path) {
        mRecorder = new MP3Recorder(new File(path));
    }
}
