package com.ibbhub.album;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * @author ：chezi008 on 2018/8/1 23:15
 * @description ：
 * @email ：chezi008@163.com
 */
 public class AlbumBean implements Parcelable {
//    public String path;
//    public long date;
    public boolean isChecked;

    /**
     * "id": "834",
     "cid": "0",
     "did": "157",
     "file": "http:\/\/robotservice.ai-cas.com\/upload\/captures\/157\/2018\/09\/27\/5bacaa1c6ea3a.jpg",
     "created": "2018-09-27 17:59:56",
     "isdel": "0"
     */

    public String id;
    public String cid;
    public String did;
    public String file;
    public String created;
    public String isdel;

    public AlbumBean() {

    }

    protected AlbumBean(Parcel in) {
        id = in.readString();
        cid = in.readString();
        did = in.readString();
        file = in.readString();
        created = in.readString();
        isdel = in.readString();
//        isChecked = in.readByte() != 0;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getId() {
        return id;
    }

    public String getCid() {
        return cid;
    }

    public String getDid() {
        return did;
    }

    public String getFile() {
        return file;
    }

    public String getCreated() {
        return created;
    }

    public void setDate(long date){
        created = DateUtils.converToString(date);
    }
    public Date getDate(){
        try {
            return DateUtils.convertToDate(created);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Date();
    }
    public String getIsdel() {
        return isdel;
    }

    public static final Creator<AlbumBean> CREATOR = new Creator<AlbumBean>() {
        @Override
        public AlbumBean createFromParcel(Parcel in) {
            return new AlbumBean(in);
        }

        @Override
        public AlbumBean[] newArray(int size) {
            return new AlbumBean[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(cid);
        dest.writeString(did);
        dest.writeString(file);
        dest.writeString(created);
        dest.writeString(isdel);
//        dest.writeByte((byte) (isChecked ? 1 : 0));
    }
}
