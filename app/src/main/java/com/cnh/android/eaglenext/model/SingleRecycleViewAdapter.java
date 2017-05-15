package com.cnh.android.eaglenext.model;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.cnh.android.eaglenext.view.RecyclerItemTouchHelperCallback;

/**
 * Created by f10210c on 5/15/2017.
 */
public abstract class SingleRecycleViewAdapter<S extends RecyclerView.ViewHolder> extends RecyclerView.Adapter
        implements RecyclerItemTouchHelperCallback.ItemTouchHelperAdapter {
    @Override
    public abstract S onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public abstract void onItemMove(int fromPosition, int toPosition);

    @Override
    public abstract void onItemDissmiss(int position);

    @Override
    public abstract void onSelectedChanged(RecyclerView.ViewHolder viewHolder);

    @Override
    public abstract void clearView(RecyclerView.ViewHolder viewHolder);
}
