package com.ibbhub.album.bean;

import com.ibbhub.album.AlbumBean;

import java.io.Serializable;
import java.util.List;

/**
 * @author by weiwenbin on 18/9/28.
 */

public class RespBean implements Serializable{
    private String status;
    private List<AlbumBean> captureList;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<AlbumBean> getCaptureList() {
        return captureList;
    }

    public void setCaptureList(List<AlbumBean> captureList) {
        this.captureList = captureList;
    }
}
