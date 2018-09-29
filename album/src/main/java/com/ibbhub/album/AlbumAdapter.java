package com.ibbhub.album;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;


import com.ibbhub.album.adapter.IbbListDelegateAdapter;

import java.util.List;

/**
 * @author ：chezi008 on 2018/8/3 21:09
 * @description ：
 * @email ：chezi008@163.com
 */
 class AlbumAdapter extends IbbListDelegateAdapter<List<AlbumBean>> {
// class AlbumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public AlbumAdapter(List<AlbumBean> data) {
        delegatesManager.addDelegate(new AlbumDelegate());
        setItems(data);
    }
}
