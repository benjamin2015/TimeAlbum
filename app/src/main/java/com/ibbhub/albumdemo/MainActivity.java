package com.ibbhub.albumdemo;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ibbhub.album.AlbumFragment;
/**
 * @description ：
 * @author ：chezi008 on 2018/8/19 15:12
 * @email ：chezi008@qq.com
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initVariable();
        initView();
    }

    private void initVariable() {

    }

    private MenuItem chooseMenu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        chooseMenu = menu.findItem(R.id.action_choose);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_choose) {
            if (item.getTitle().equals("选择")) {
                albumFragment.enterChoose();
            } else {
                albumFragment.cancelChoose();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private AlbumFragment albumFragment;

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        albumFragment = (AlbumFragment) getSupportFragmentManager().findFragmentByTag("album");
        if (albumFragment == null) {
            albumFragment = new MyAlbumFragment();
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flParent, albumFragment);
        ft.commit();
    }

//    private void requestPermission() {
//        //获取storage权限
//        AndPermission.with(this)
//                .runtime()
//                .permission(Permission.Group.STORAGE)
//                .onGranted(new Action<List<String>>() {
//                    @Override
//                    public void onAction(List<String> data) {
//
//                    }
//                })
//                .onDenied(new Action<List<String>>() {
//                    @Override
//                    public void onAction(List<String> data) {
//
//                    }
//                })
//                .start();
//    }

    public void onChooseModeChange(boolean isChoose) {
        chooseMenu.setTitle(isChoose ? "取消" : "选择");
    }

}
