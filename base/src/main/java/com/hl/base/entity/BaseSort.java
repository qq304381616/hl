package com.hl.base.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class BaseSort implements Parcelable {

    private String first;
    private Integer orderNo;

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public BaseSort() {
    }

    protected BaseSort(Parcel in) {
        first = in.readString();
        orderNo = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(first);
        dest.writeInt(orderNo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BaseSort> CREATOR = new Creator<BaseSort>() {
        @Override
        public BaseSort createFromParcel(Parcel in) {
            return new BaseSort(in);
        }

        @Override
        public BaseSort[] newArray(int size) {
            return new BaseSort[size];
        }
    };
}
