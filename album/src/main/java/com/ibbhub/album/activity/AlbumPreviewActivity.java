package com.ibbhub.album.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.ibbhub.album.AlbumBean;
import com.ibbhub.album.AlbumPreviewFragment;
import com.ibbhub.album.R;

import java.util.ArrayList;

/**
 * @author ：chezi008 on 2018/8/9 21:40
 * @description ：
 * @email ：chezi008@qq.com
 */
public class AlbumPreviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_preview);
        AlbumPreviewFragment fragment = (AlbumPreviewFragment) getSupportFragmentManager().findFragmentByTag("preview");
        if (fragment == null) {
            fragment = new AlbumPreviewFragment();
        }

        ArrayList<AlbumBean> data = getIntent().getParcelableArrayListExtra("data");
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("data", data);
        bundle.putInt("pos", getIntent().getIntExtra("pos", 0));
        fragment.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.clParent, fragment, "preview");
        ft.commit();
    }

    public static void start(Context context, ArrayList<AlbumBean> data, int pos) {
        Intent starter = new Intent(context, AlbumPreviewActivity.class);
        starter.putParcelableArrayListExtra("data", data);
        starter.putExtra("pos", pos);
        context.startActivity(starter);
    }


}
