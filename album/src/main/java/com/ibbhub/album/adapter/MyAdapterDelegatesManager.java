package com.ibbhub.album.adapter;

import android.support.annotation.NonNull;

/**
 * @author by weiwenbin on 18/9/28.
 */

public class MyAdapterDelegatesManager extends AdapterDelegatesManager {

    @Override
    public int getItemViewType(@NonNull Object items, int position) {
        if (position == delegates.size() - 1){
//            return ITEM_TYPE_FOOTER;
        }
        return super.getItemViewType(items, position);
    }

}
