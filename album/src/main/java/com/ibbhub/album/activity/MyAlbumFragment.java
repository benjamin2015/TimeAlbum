package com.ibbhub.album.activity;

import android.os.Environment;
import android.widget.ImageView;

import com.ibbhub.album.AlbumFragment;
import com.ibbhub.album.ITaDecoration;
import com.ibbhub.album.PicassoUtils;
import com.ibbhub.album.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ：chezi008 on 2018/8/19 14:57
 * @description ：
 * @email ：chezi008@163.com
 */
public class MyAlbumFragment extends AlbumFragment {
    @Override
    public List<File> buildAlbumSrc() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/DCIM/Camera";
        List<File> fileList = new ArrayList<>();
        fileList.add(new File(path));
        return fileList;
    }

    @Override
    public ITaDecoration buildDecoration() {
        return null;
    }

    @Override
    public void loadOverrideImage(String path, ImageView iv) {
        PicassoUtils.setImage(iv, path, R.drawable.ic_album);
    }

    @Override
    public void loadImage(String path, ImageView iv) {
        PicassoUtils.setImage(iv, path, R.drawable.ic_album);
    }

    @Override
    public void onChooseModeChange(boolean isChoose) {
        ((AlbumActivity)getActivity()).onChooseModeChange(isChoose);
    }
}
