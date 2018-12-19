package com.hl.base.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class BaseSort implements Parcelable, Comparable<BaseSort> {
    private String first;

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.first);
    }

    public BaseSort() {
    }

    protected BaseSort(Parcel in) {
        this.first = in.readString();
    }

    public static final Creator<BaseSort> CREATOR = new Creator<BaseSort>() {
        @Override
        public BaseSort createFromParcel(Parcel source) {
            return new BaseSort(source);
        }

        @Override
        public BaseSort[] newArray(int size) {
            return new BaseSort[size];
        }
    };

    @Override
    public int compareTo(BaseSort o) {
        return getFirst().compareTo(o.getFirst());
    }
}
