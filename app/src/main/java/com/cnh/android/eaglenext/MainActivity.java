package com.cnh.android.eaglenext;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.cnh.android.eaglenext.fragment.OverviewFragment;
import com.cnh.android.eaglenext.model.SingleUdwRecyclerViewAdapter;
import com.cnh.android.eaglenext.view.RecyclerItemTouchHelperCallback;
import com.cnh.android.eaglenext.view.SingleUdwViewHolder;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    @BindView(R.id.main_cotainer) View mFrameLayoutMain;
    @BindView(R.id.recyclerview_leftbar) RecyclerView mLeftUdwListView;

    OverviewFragment mFragmentOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // Left Bar list
        SingleUdwRecyclerViewAdapter adapter = new SingleUdwRecyclerViewAdapter(this);
        adapter.setData(generateDummyUdws());
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new RecyclerItemTouchHelperCallback(adapter));
        itemTouchHelper.attachToRecyclerView(mLeftUdwListView);
        mLeftUdwListView.setLayoutManager(new StaggeredGridLayoutManager(1, OrientationHelper.VERTICAL));
        mLeftUdwListView.setItemAnimator(new DefaultItemAnimator());
        mLeftUdwListView.setAdapter(adapter);

        mFragmentOverview = new OverviewFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragments_container, mFragmentOverview);
        transaction.commit();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mFrameLayoutMain.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        mFrameLayoutMain.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private List<SingleUdwViewHolder.UdwItem> generateDummyUdws() {
        List<SingleUdwViewHolder.UdwItem> data = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            SingleUdwViewHolder.UdwItem item = new SingleUdwViewHolder.UdwItem(i);
            item.icon = R.drawable.ic_multiline_chart_black_48dp;
            item.content = "Max: 65 mph \nMin: 12 mph";
            data.add(item);

            item = new SingleUdwViewHolder.UdwItem(i);
            item.icon = R.drawable.ic_local_gas_station_black_48dp;
            item.content = "120 ah";
            data.add(item);

            item = new SingleUdwViewHolder.UdwItem(i);
            item.icon = R.drawable.ic_nfc_black_48dp;
            item.switchVisible = true;
            item.switchChecked = true;
            item.switchDescription = "Overlap";
            data.add(item);

            item = new SingleUdwViewHolder.UdwItem(i);
            item.icon = R.drawable.ic_border_bottom_black_48dp;
            item.caption = "Coverage Area";
            item.content = "1.89 ha";
            data.add(item);

            item = new SingleUdwViewHolder.UdwItem(i);
            item.icon = R.drawable.ic_select_all_black_48dp;
            item.caption = "Area Counter";
            item.content = "0.06 ha";
            item.buttonVisible = true;
            data.add(item);
        }

        return data;
    }
}
