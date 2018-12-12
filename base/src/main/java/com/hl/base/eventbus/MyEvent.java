package com.hl.base.eventbus;

public class MyEvent {

    private Integer type;
    private Object Message;

    public MyEvent(Integer type, Object message) {
        this.type = type;
        Message = message;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Object getMessage() {
        return Message;
    }

    public void setMessage(Object message) {
        Message = message;
    }

    @Override
    public String toString() {
        return "MyEvent{" +
                "type=" + type +
                ", Message=" + Message +
                '}';
    }
}
