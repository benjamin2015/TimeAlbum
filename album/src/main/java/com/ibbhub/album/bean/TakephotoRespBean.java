package com.ibbhub.album.bean;

import com.ibbhub.album.AlbumBean;

import java.io.Serializable;
import java.util.List;

/**
 * @author by weiwenbin on 18/9/28.
 */

public class TakephotoRespBean implements Serializable{
    private String status;
    private List<AlbumBean> takephotoList;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<AlbumBean> getTakephotoList() {
        return takephotoList;
    }

    public void setTakephotoList(List<AlbumBean> takephotoList) {
        this.takephotoList = takephotoList;
    }
}
