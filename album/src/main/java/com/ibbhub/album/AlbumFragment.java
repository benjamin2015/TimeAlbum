package com.ibbhub.album;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ibbhub.album.activity.AlbumPreviewActivity;
import com.ibbhub.album.bean.RespBean;
import com.ibbhub.album.bean.TakephotoRespBean;
import com.ibbhub.album.net.RequestManager;
import com.ibbhub.album.util.GsonUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * @author ：chezi008 on 2018/8/1 22:34
 * @description ：时间相册显示的主页面
 * @email ：chezi008@163.com
 */
public abstract class AlbumFragment extends Fragment implements TimeAlbumListener {
    public static boolean isChooseMode = false;
    private String TAG = getClass().getSimpleName();

    private List<TimeBean> mData = new ArrayList<>();
    private TimeAdapter mAdapter;
    private RecyclerView rc_list;
    private ProgressBar pb_loading;
    private AlbumBottomMenu album_menu;

    private List<TimeBean> choosedCache = new ArrayList<>();
    private List<String> choosedList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initVariable();
        updateDate(true);

    }

    public void updateDate(boolean isHand) {
        loadData(isHand);
    }

    private void initVariable() {
        TaHelper.getInstance().setAdapterListener(new AdapterListener<AlbumBean>() {
            @Override
            public void onItemClick(AlbumBean albumBean, View v) {
                TimeBean timeBean = new TimeBean();
                timeBean.setDate(albumBean.getDate().getTime());
                if (isChooseMode) {
                    int index = choosedCache.indexOf(timeBean);
                    List<AlbumBean> mbList;
                    if (index < 0) {
                        //被选中
                        mbList = new ArrayList<>();
                        mbList.add(albumBean);
                        timeBean.setItemList(mbList);
                        choosedCache.add(timeBean);
                    } else {
                        mbList = choosedCache.get(index).itemList;

                        //如果被选中，则添加到缓存中
                        if (albumBean.isChecked) {
                            mbList.add(albumBean);
                        } else {
                            mbList.remove(albumBean);
                            if (mbList.size() == 0) {
                                choosedCache.remove(index);
                            }
                        }
                    }
                } else {
                    int index = mData.indexOf(timeBean);
                    TimeBean ab = mData.get(index);
                    index = ab.itemList.indexOf(albumBean);
                    if (index >= 0) {
                        start2Preview((ArrayList<AlbumBean>) ab.itemList, index);
                    }
                }
            }

            @Override
            public void onItemLongClick(AlbumBean albumBean, View v) {
                //进入选择模式
                enterChoose();
            }
        });
        TaHelper.getInstance()
                .setSrcFiles(buildAlbumSrc())
                .setTbDecoration(buildDecoration())
                .setLoadImageListener(this);
    }

    private void initView(View view) {
        pb_loading = view.findViewById(R.id.pb_loading);
        album_menu = view.findViewById(R.id.album_menu);

        rc_list = view.findViewById(R.id.rc_list);
        mAdapter = new TimeAdapter();

        rc_list.setLayoutManager(new LinearLayoutManager(getContext()));
        rc_list.setAdapter(mAdapter);
        mAdapter.setData(mData);

        rc_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //拿到最后一条的position
                LinearLayoutManager llm = (LinearLayoutManager) rc_list.getLayoutManager();
                int endCompletelyPosition = llm.findLastCompletelyVisibleItemPosition();
                if (endCompletelyPosition == rc_list.getAdapter().getItemCount() - 1) {
                    //执行加载更多的方法，无论是用接口还是别的方式都行
                    int a = 5;
                    int b = 5;
                }
            }
        });
        album_menu.setMenuListener(new AlbumBottomMenu.AlubmBottomMenuListener() {
            @Override
            public void onDeleteClick() {
                showConfirmDelete();
            }

            @Override
            public void onShareClick() {
                //分享
                processShare();
            }
        });
    }

    /**
     * 弹出确认删除提示
     */
    private void showConfirmDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("删除照片");
        builder.setMessage("确认是否删除照片？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                processDelete();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private Calendar cal1 = Calendar.getInstance();

    private void initData(List<AlbumBean> fileList) {
        if (fileList == null) return;
        Observable.fromIterable(fileList)
                .flatMapIterable(new Function<AlbumBean, Iterable<AlbumBean>>() {
                    @Override
                    public Iterable<AlbumBean> apply(AlbumBean file) throws Exception {
                        return Arrays.asList(file);
                    }
                })
                .filter(new Predicate<AlbumBean>() {
                    @Override
                    public boolean test(AlbumBean it) throws Exception {
                        return it.getFile().endsWith(".jpg") || it.getFile().endsWith(".mp4");
                    }
                })
//                .map(new Function<File, AlbumBean>() {
//                    @Override
//                    public AlbumBean apply(File file) throws Exception {
//                        Date fileDate = FileUtils.parseDate(file);
//                        cal1.setTime(fileDate);
//                        // 将时分秒,毫秒域清零
//                        cal1.set(Calendar.HOUR_OF_DAY, 0);
//                        cal1.set(Calendar.MINUTE, 0);
//                        cal1.set(Calendar.SECOND, 0);
//                        cal1.set(Calendar.MILLISECOND, 0);
//                        AlbumBean albumBean = new AlbumBean();
//                        albumBean.date = cal1.getTime().getTime();
//                        albumBean.path = file.getAbsolutePath();
//                        return albumBean;
//                    }
//                })
                .collect(new Callable<List<TimeBean>>() {
                    @Override
                    public List<TimeBean> call() throws Exception {
                        return new ArrayList<>();
                    }
                }, new BiConsumer<List<TimeBean>, AlbumBean>() {
                    @Override
                    public void accept(List<TimeBean> timeBeans, AlbumBean albumBean) throws Exception {
                        cal1.setTime(albumBean.getDate());
                        // 将时分秒,毫秒域清零
                        cal1.set(Calendar.HOUR_OF_DAY, 0);
                        cal1.set(Calendar.MINUTE, 0);
                        cal1.set(Calendar.SECOND, 0);
                        cal1.set(Calendar.MILLISECOND, 0);
                        albumBean.setDate(cal1.getTime().getTime());

                        TimeBean timeBean = new TimeBean();
                        timeBean.setDate(DateUtils.convertToDate(albumBean.getCreated()).getTime());
                        int index = timeBeans.indexOf(timeBean);
                        if (index >= 0) {
                            timeBeans.get(index).itemList.add(albumBean);
                        } else {
                            timeBean.itemList.add(albumBean);
                            timeBeans.add(timeBean);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new BiConsumer<List<TimeBean>, Throwable>() {
                    @Override
                    public void accept(List<TimeBean> timeBeans, Throwable throwable) throws Exception {
//                        mData.addAll(timeBeans);
                        mData = timeBeans;
                        sortList();
                    }
                });
    }

    /**
     * 数据根据时间进行排序
     */
    private void sortList() {
        Collections.sort(mData, new Comparator<TimeBean>() {
            @Override
            public int compare(TimeBean o1, TimeBean o2) {
                if (o1.date > o2.date) {
                    return -1;
                } else if (o1.date == o2.date) {
                    return 0;
                }
                return 1;
            }
        });
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pb_loading.setVisibility(View.GONE);
                    rc_list.setVisibility(View.VISIBLE);
                    mAdapter.setData(mData);
                }
            });
        }
    }


    /**
     * 处理分享
     */
    private void processShare() {
        //判断是多张还是单张
        if (choosedCache.size() == 1 && choosedCache.get(0).itemList.size() == 1) {
            //单张
            AlbumBean albumBean = choosedCache.get(0).itemList.get(0);
            TaShareManager.getInstance().openShare(getContext(), albumBean.getFile());
        } else {
            //多张
            ArrayList<Uri> uriList = new ArrayList<>();
            for (int i = 0; i < choosedCache.size(); i++) {
                for (AlbumBean mb :
                        choosedCache.get(i).itemList) {
                    uriList.add(Uri.fromFile(new File(mb.getFile())));
                }
            }
            TaShareManager.getInstance().openShare(getContext(), uriList);
        }
        cancelChoose();
    }

    /**
     * 处理照片删除
     */
    private void processDelete() {

        //判断是多张还是单张
        if (choosedCache.size() == 1 && choosedCache.get(0).itemList.size() == 1) {
            //单张
            AlbumBean albumBean = choosedCache.get(0).itemList.get(0);
            notifyAlbumRemove(albumBean);
        } else {
            //多张
            for (int i = 0; i < choosedCache.size(); i++) {
                for (AlbumBean mb :
                        choosedCache.get(i).itemList) {
                    notifyAlbumRemove(mb);
                }
            }
        }
        choosedCache.clear();

//        removePhoto();
    }

    private void notifyAlbumRemove(AlbumBean albumBean) {
//        FileUtils.delete(albumBean.path);
//        for ()
        int index = mData.indexOf(new TimeBean(albumBean.getDate().getTime()));
        TimeBean tb = mData.get(index);
        tb.itemList.remove(albumBean);
        if (tb.itemList.size() == 0) {
            mData.remove(index);
        }
//        mAdapter.notifyItemRemoved(index);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 取消选择
     */
    public void cancelChoose() {
        for (int i = 0; i < choosedCache.size(); i++) {
            for (AlbumBean mb :
                    choosedCache.get(i).itemList) {
                mb.isChecked = false;
            }
        }
        choosedCache.clear();
        isChooseMode = false;
        TaHelper.getInstance().onChooseModeChange(isChooseMode);
        mAdapter.notifyDataSetChanged();
        album_menu.setVisibility(View.GONE);
    }

    /**
     * 进入选择
     */
    public void enterChoose() {
        isChooseMode = true;
        TaHelper.getInstance().onChooseModeChange(isChooseMode);
        mAdapter.notifyDataSetChanged();
        album_menu.setVisibility(View.VISIBLE);
    }

    /**
     * 设置相册的媒体源
     *
     * @return
     */
    public abstract List<File> buildAlbumSrc();

    /**
     * 设置recyclerView的装饰器
     *
     * @return
     */
    public abstract ITaDecoration buildDecoration();

    /**
     * 跳转至预览界面
     *
     * @param data 预览数据
     * @param pos  当前选择albumBean 的位置
     */
    public void start2Preview(ArrayList<AlbumBean> data, int pos) {
        AlbumPreviewActivity.start(getContext(), data, pos);
    }


    public void loadData(boolean loadHand) {
        pb_loading.setVisibility(View.VISIBLE);
        rc_list.setVisibility(View.GONE);
        if (loadHand) {
            loadHand();
        } else {
            loadSteal();
        }

    }

    public void loadSteal() {
        HashMap<String, String> map = new HashMap();
        map.put("cid", "20");
        map.put("page", "2");
        map.put("version", "20180620");
        RequestManager.getInstance().requestPostByAsyn(RequestManager.url_GetCapture, map,
                new RequestManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        RespBean bean = GsonUtils.fromJson(result.toString(), RespBean.class);
                        if (bean != null)
                            initData(bean.getCaptureList());
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {

                    }
                });
    }

    public void loadHand() {
        HashMap<String, String> map = new HashMap();
        map.put("cid", "20");
        map.put("page", "2");
        map.put("version", "20180620");
        RequestManager.getInstance().requestPostByAsyn(RequestManager.url_GetTakephoto, map,
                new RequestManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        TakephotoRespBean bean = GsonUtils.fromJson(result.toString(), TakephotoRespBean.class);
                        if (bean != null)
                            initData(bean.getTakephotoList());
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {

                    }
                });
    }


    private void removePhoto(List<String> list) {
        HashMap<String, String> map = new HashMap();
        map.put("takephoto_id", list.toString());
        RequestManager.getInstance().requestPostByAsyn(RequestManager.url_Remove, map,
                new RequestManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                        mAdapter.notifyDataSetChanged();
//                        mAdapter.notifyItemRemoved();
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        Toast.makeText(getContext(), "删除失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
