package com.cnh.android.eaglenext.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.cnh.android.eaglenext.R;

/**
 * Created by Hai on 4/22/17.
 */
public class SingleNexUdwViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.udw_icon) ImageView iconView;
    @BindView(R.id.udw_caption) TextView captionView;
    @BindView(R.id.udw_content) TextView contentView;
    @BindView(R.id.udw_switch) SwitchCompat switchView;
    @BindView(R.id.udw_button) ImageButton buttonView;

    public enum ITEM_TYPE {
        ITEM_TYPE_SINGLE,
        ITEM_TYPE_DOUBLE
    }

    //TODO: move to presenter
    public static class UdwItem {
        int id;
        public ITEM_TYPE type;

        // Dummy UDW info
        public int icon;
        public String caption;
        public String content;
        public boolean switchVisible;
        public boolean switchChecked;
        public String switchDescription;
        public boolean buttonVisible;
        public UdwItem (int id) {
            this.id = id;
        }

        public void onBindViewHolder(SingleNexUdwViewHolder holder) {
            holder.iconView.setImageResource(icon);

            holder.captionView.setText(caption);
            holder.captionView.setVisibility(caption != null ? View.VISIBLE : View.GONE);

            holder.contentView.setText(content);
            holder.contentView.setVisibility(content != null ? View.VISIBLE : View.GONE);

            holder.switchView.setText(switchDescription);
            holder.switchView.setChecked(switchChecked);
            holder.switchView.setVisibility(switchVisible ? View.VISIBLE : View.GONE);

            holder.buttonView.setVisibility(buttonVisible ? View.VISIBLE : View.GONE);

            holder.itemView.setBackgroundResource(R.drawable.background_corner_card);
        }
    }

    public SingleNexUdwViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}