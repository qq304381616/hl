package com.hl.utils.net;

/**
 * http 请求参数类。 只支持data为对象。不支持集合。 集合需要把泛型改成Object,后面自行解析
 */
public class HttpBaseEntity {
    private int status;
    private String desc;
    private Object data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
