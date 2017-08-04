package com.hl.utils.record;

/**
 * Created on 2017/7/31.
 */
public interface IRecord {
    void startRecording();

    void stopRecording();

    void setSavePath(String path);
}
