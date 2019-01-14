package com.hl.okhttp3.core;

public abstract class EventListener {

    public interface Factory {
        EventListener create(Call call);
    }
}
