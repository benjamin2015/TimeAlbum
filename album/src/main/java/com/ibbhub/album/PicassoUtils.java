package com.ibbhub.album;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;

import java.io.File;

/**
 * @author by weiwenbin on 18/9/28.
 */

public class PicassoUtils {

    static Picasso picasso;

    public synchronized static void initPicasso(Context context) {
        if (FileUtils.isSDCardEnable()) {
            String imageCacheDir = Environment.getExternalStorageDirectory().getPath() + "/kidcares/" + "picassoimage";
            File file = new File(imageCacheDir);

            if (!file.exists()) file.mkdirs();
            if (picasso == null) {
                picasso = new Picasso.Builder(context).downloader(
                        new OkHttpDownloader(file)).build();
                Picasso.setSingletonInstance(picasso);
            }
        } else {
            picasso = Picasso.with(context);
        }
    }

    /**
     * * 设置全局默认的
     *
     * @param iv
     * @param url
     * @param defaultId
     */
    public static void setImage(ImageView iv, Object url, int defaultId) {
        setImage(iv, url, defaultId, null);
    }

    /**
     * 设置全局默认的
     * 注意：有居中属性
     *
     * @param iv
     * @param url
     * @param defaultId
     * @param _callback
     */
    public static void setImage(ImageView iv, Object url, int defaultId, Callback _callback) {
        setPicassoDefault(iv, url, defaultId, defaultId, null, _callback);
    }


    private static void setPicassoDefault(final ImageView _imageView, Object url, int holderId, int errorId, Transformation _transformation, Callback _callback) {
        try {
            if (null == url) {
                setImageError(_imageView, errorId);
                return;
            }
            RequestCreator mCreator;
            if (picasso == null) {
                throw new Exception("没有初始化picasso");
            }
            if (url instanceof String) {
                String imageUrl = (String) url;
                if (TextUtils.isEmpty(imageUrl)) {
                    mCreator = picasso.load(errorId);
                } else if (((String) url).startsWith("http")) {
                    mCreator = picasso.load((String) url);
                } else {
                    mCreator = picasso.load(new File((String) url));
                }
            } else if (url instanceof Integer) {
                mCreator = picasso.load((Integer) url);
            } else if (url instanceof File) {
                mCreator = picasso.load((File) url);
            } else if (url instanceof Uri) {
                mCreator = picasso.load((Uri) url);
            } else {
                setImageError(_imageView, errorId);
                return;
            }
            mCreator = mCreator.placeholder(holderId).error(errorId).fit().centerInside().config(Bitmap.Config.RGB_565);
            if (_transformation != null) {
                mCreator.transform(_transformation);
            }
            if (_callback != null) {
                mCreator.into(_imageView, _callback);
            } else {
                mCreator.into(_imageView);
            }
        } catch (Exception e) {
            //这里是很必要的。例如url="" 或是 0 都会走这里
            setImageError(_imageView, errorId);
        }
    }
    /**
     * 设置错误显示
     *
     * @param _imageView
     * @param errorId
     */
    private static void setImageError(ImageView _imageView, int errorId) {
        _imageView.setImageResource(errorId);
    }
}
