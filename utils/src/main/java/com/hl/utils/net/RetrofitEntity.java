package com.hl.utils.net;

/**
 * http 请求参数类。 只支持data为对象。不支持集合。 集合需要把泛型改成Object,后面自行解析
 */
public class RetrofitEntity<T> {
    private int code;
    private String message;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
