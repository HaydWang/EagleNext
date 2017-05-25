package com.cnh.android.eaglenext.model;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.cnh.android.eaglenext.R;
import com.cnh.android.eaglenext.view.SingleNxUdwViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Hai on 4/21/17.
 */
public class SingleNxUdwRecyclerViewAdapter extends SingleRecycleViewAdapter<SingleNxUdwViewHolder> {
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;

    protected List<SingleNxUdwViewHolder.UdwItem> mData;

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mData,fromPosition,toPosition);
        // will notify in TouchCallBack notifyItemMoved(fromPosition,toPosition);
    }

    @Override
    public void onItemDissmiss(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder) {
        // TODO: use corner shape
        viewHolder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent));
    }

    @Override
    public void clearView(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder.getAdapterPosition() < 0 ) return;

        viewHolder.itemView.setBackgroundResource(R.drawable.background_corner_card);
    }

    public static class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        private int space;
        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if(parent.getChildAdapterPosition(view) != 0)
                outRect.top = space;
        }

    }

    public void setData(List<SingleNxUdwViewHolder.UdwItem> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public SingleNxUdwRecyclerViewAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);

        mData = new ArrayList<>();
    }

    @Override
    public SingleNxUdwViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SingleNxUdwViewHolder(mLayoutInflater.inflate(R.layout.view_holder_single_nx_udw, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        mData.get(position).onBindViewHolder((SingleNxUdwViewHolder)holder);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}