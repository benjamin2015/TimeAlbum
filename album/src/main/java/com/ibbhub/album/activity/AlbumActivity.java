package com.ibbhub.album.activity;

import android.content.pm.ActivityInfo;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ibbhub.album.AlbumFragment;
import com.ibbhub.album.R;

public class AlbumActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initVariable();
        initView();
    }


    private void initVariable() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_choose) {
            if (item.getTitle().equals("编辑")) {
                albumFragment.enterChoose();
            } else {
                albumFragment.cancelChoose();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private AlbumFragment albumFragment;
    Button btChoose;
    private void initView() {
        btChoose = findViewById(R.id.bt_head_right);
        btChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btChoose.getText().equals("编辑")) {
                    albumFragment.enterChoose();
                } else {
                    albumFragment.cancelChoose();
                }
            }
        });

        RadioGroup radio_group = (RadioGroup) findViewById(R.id.radio_group);
        ((RadioButton) (radio_group.getChildAt(0))).setChecked(true);
        radio_group.setOnCheckedChangeListener(this);

        albumFragment = (AlbumFragment) getSupportFragmentManager().findFragmentByTag("album");
        if (albumFragment == null) {
            albumFragment = new MyAlbumFragment();
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flParent, albumFragment);
        ft.commit();
    }

    public void onChooseModeChange(boolean isChoose) {
        btChoose.setText(isChoose ? "取消" : "编辑");
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        if (checkedId == R.id.rb_time_school) {
            albumFragment.updateDate(true);
        } else {
            albumFragment.updateDate(false);
        }
    }
}
