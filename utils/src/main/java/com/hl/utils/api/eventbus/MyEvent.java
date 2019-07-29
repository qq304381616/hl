package com.hl.utils.api.eventbus;

public class MyEvent {

    private Integer type;
    private Object message;

    public MyEvent(Integer type, Object message) {
        this.type = type;
        this.message = message;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MyEvent{" +
                "type=" + type +
                ", Message=" + message +
                '}';
    }
}