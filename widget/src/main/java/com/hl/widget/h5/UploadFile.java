package com.hl.widget.h5;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 2017/7/19.
 */
public class UploadFile implements Parcelable {
    private List<String> localIds; // ["5153fffb3b331ea97c7432d33c4c8839","1d969232844d694d8db2391a9f849053"]
    private Map<String, String> stringParams;
    private String serverURL; // "http:\/\/localhost:8080\/test\/servlet\/UploadFile",
    private String inputName; // "file"
    private String header; // "{\"Content-type\":\"application\/json;charset=utf-8\"}",
    private int successOpid; // 5011
    private int failOpid; // 5012
    private int statusOpid; // 5013

    public UploadFile(List<String> localIds, Map<String, String> stringParams, String serverURL, String inputName, int successOpid, int failOpid, int statusOpid) {
        this.localIds = localIds;
        this.stringParams = stringParams;
        this.serverURL = serverURL;
        this.inputName = inputName;
        this.successOpid = successOpid;
        this.failOpid = failOpid;
        this.statusOpid = statusOpid;
    }

    public List<String> getLocalIds() {
        return localIds;
    }

    public void setLocalIds(List<String> localIds) {
        this.localIds = localIds;
    }

    public Map<String, String> getStringParams() {
        return stringParams;
    }

    public void setStringParams(Map<String, String> stringParams) {
        this.stringParams = stringParams;
    }

    public String getServerURL() {
        return serverURL;
    }

    public void setServerURL(String serverURL) {
        this.serverURL = serverURL;
    }

    public String getInputName() {
        return inputName;
    }

    public void setInputName(String inputName) {
        this.inputName = inputName;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public int getSuccessOpid() {
        return successOpid;
    }

    public void setSuccessOpid(int successOpid) {
        this.successOpid = successOpid;
    }

    public int getFailOpid() {
        return failOpid;
    }

    public void setFailOpid(int failOpid) {
        this.failOpid = failOpid;
    }

    public int getStatusOpid() {
        return statusOpid;
    }

    public void setStatusOpid(int statusOpid) {
        this.statusOpid = statusOpid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.localIds);
        dest.writeInt(this.stringParams.size());
        for (Map.Entry<String, String> entry : this.stringParams.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }
        dest.writeString(this.serverURL);
        dest.writeString(this.inputName);
        dest.writeString(this.header);
        dest.writeInt(this.successOpid);
        dest.writeInt(this.failOpid);
        dest.writeInt(this.statusOpid);
    }

    protected UploadFile(Parcel in) {
        this.localIds = in.createStringArrayList();
        int stringParamsSize = in.readInt();
        this.stringParams = new HashMap<String, String>(stringParamsSize);
        for (int i = 0; i < stringParamsSize; i++) {
            String key = in.readString();
            String value = in.readString();
            this.stringParams.put(key, value);
        }
        this.serverURL = in.readString();
        this.inputName = in.readString();
        this.header = in.readString();
        this.successOpid = in.readInt();
        this.failOpid = in.readInt();
        this.statusOpid = in.readInt();
    }

    public static final Creator<UploadFile> CREATOR = new Creator<UploadFile>() {
        @Override
        public UploadFile createFromParcel(Parcel source) {
            return new UploadFile(source);
        }

        @Override
        public UploadFile[] newArray(int size) {
            return new UploadFile[size];
        }
    };
}
