package com.cnh.android.eaglenext.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnh.android.eaglenext.R;
import com.cnh.android.eaglenext.udw.UdwView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hai on 4/22/17.
 */
public class SingleUdwViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.view_holder_single_udw)
    public ViewGroup udwViewHolder;

    public enum ITEM_TYPE {
        ITEM_TYPE_SINGLE,
        ITEM_TYPE_DOUBLE
    }

    //TODO: move to presenter
    public static class UdwItem {
        int id;
        public ITEM_TYPE type;

        public UdwView udw;
        public View udwView;

        public UdwItem(Context context, int id, String sId, String packageName, String className) {
            this.id = id;
            type = id % 2 == 0 ? ITEM_TYPE.ITEM_TYPE_SINGLE : ITEM_TYPE.ITEM_TYPE_DOUBLE;

            // TODO: change to lazy initial
            udw = new UdwView(sId,
                    packageName,
                    className);
            udwView = udw.getView(context);
            udw.callOnCreate(context);

            int POSITION_RUNSCREEN = 1;  // hardcode to avoid reference to corelib
            udw.callInit(context, POSITION_RUNSCREEN, false);
            //udw.callOnResume(context);
        }
    }

    public SingleUdwViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}