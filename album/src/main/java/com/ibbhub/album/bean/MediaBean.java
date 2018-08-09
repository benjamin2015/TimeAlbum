package com.ibbhub.album.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author ：chezi008 on 2018/8/1 23:15
 * @description ：
 * @email ：chezi008@163.com
 */
public class MediaBean implements Parcelable {
    public String path;
    public long date;
    public boolean isChecked;

    public MediaBean() {

    }

    protected MediaBean(Parcel in) {
        path = in.readString();
        date = in.readLong();
        isChecked = in.readByte() != 0;
    }

    public static final Creator<MediaBean> CREATOR = new Creator<MediaBean>() {
        @Override
        public MediaBean createFromParcel(Parcel in) {
            return new MediaBean(in);
        }

        @Override
        public MediaBean[] newArray(int size) {
            return new MediaBean[size];
        }
    };

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeLong(date);
        dest.writeByte((byte) (isChecked ? 1 : 0));
    }
}
