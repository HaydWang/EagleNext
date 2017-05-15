package com.cnh.android.eaglenext.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.cnh.android.eaglenext.R;
import com.cnh.android.eaglenext.model.SingleUdwRecyclerViewAdapter;
import com.cnh.android.eaglenext.view.RecyclerItemTouchHelperCallback;
import com.cnh.android.eaglenext.view.SingleUdwViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hai on 14/05/2017.
 */
public class UdwFragment extends Fragment {
    @BindView(R.id.recyclerview_mainscreen)
    RecyclerView mRecyclerView;

    private View mView;
    private SingleUdwRecyclerViewAdapter mAdapter;
    private int dataType = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_udw, container, false);
            ButterKnife.bind(this, mView);

            initRecyclerAdapter();
        }
        return mView;
    }

    private void initRecyclerAdapter() {
        // Init UDW list with PF UDWs as default
        mAdapter = new SingleUdwRecyclerViewAdapter(getContext());
        changeType(dataType);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        //mRecyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,
                OrientationHelper.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(
                new SingleUdwRecyclerViewAdapter.SpaceItemDecoration(2)); //TODO: move 2px to dimen
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new RecyclerItemTouchHelperCallback(mAdapter));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void changeType(int dataType) {
        switch (dataType) {
            case 0:
                mAdapter.setData(getPfUdws(mView.getContext()));
                return;
            case 1:
                mAdapter.setData(getAgUdws(mView.getContext()));
                return;
            case 2:
                mAdapter.setData(getYmUdws(mView.getContext()));
                return;
            default:
                mAdapter.setData(getPfUdws(mView.getContext()));
                break;
        }
    }

    public void setType(int i) {
        dataType = i;
        if (mAdapter != null) changeType(dataType);
    }

    // TODO: so ugly :(  will build a factory for UDWs.
    private List<SingleUdwViewHolder.UdwItem> getPfUdws(Context context) {
        List<SingleUdwViewHolder.UdwItem> data = new ArrayList<>();
        int i = 0;

        SingleUdwViewHolder.UdwItem item = new SingleUdwViewHolder.UdwItem(context, i++,
                "TotalFuelUsedUDW",
                "com.cnh.pf.pfudwservice",
                "com.cnh.pf.pfudwservice.widget.TotalFuelUsedUDW");
        data.add(item);

        // Need set el.param as JSON string
//        item = new SingleUdwViewHolder.UdwItem(context, i++,
//                "GroundSpeedUDW",
//                "com.cnh.pf.pfudwservice",
//                "com.cnh.pf.pfudwservice.widget.GroundSpeedUDW");
//        data.add(item);

        item = new SingleUdwViewHolder.UdwItem(context, i++,
                "FuelUsedUDW",
                "com.cnh.pf.pfudwservice",
                "com.cnh.pf.pfudwservice.widget.FuelUsedUDW");
        data.add(item);

        item = new SingleUdwViewHolder.UdwItem(context, i++,
                "AverageWorkingRateUDW",
                "com.cnh.pf.pfudwservice",
                "com.cnh.pf.pfudwservice.widget.AverageWorkingRateUDW");
        data.add(item);

        item = new SingleUdwViewHolder.UdwItem(context, i++,
                "TimeInWorkUDW",
                "com.cnh.pf.pfudwservice",
                "com.cnh.pf.pfudwservice.widget.TimeInWorkUDW");
        data.add(item);

        item = new SingleUdwViewHolder.UdwItem(context, i++,
                "TimeInTaskUDW",
                "com.cnh.pf.pfudwservice",
                "com.cnh.pf.pfudwservice.widget.TimeInTaskUDW");
        data.add(item);

        item = new SingleUdwViewHolder.UdwItem(context, i++,
                "OverlapControlUDW",
                "com.cnh.pf.rscudwservice",
                "com.cnh.pf.rscudwservice.widget.OverlapControlUDW");
        data.add(item);

        item = new SingleUdwViewHolder.UdwItem(context, i++,
                "BoundaryControlUDW",
                "com.cnh.pf.rscudwservice",
                "com.cnh.pf.rscudwservice.widget.BoundaryControlUDW");
        data.add(item);

        item = new SingleUdwViewHolder.UdwItem(context, i++,
                "OverlapControlUDW",
                "com.cnh.pf.rscudwservice",
                "com.cnh.pf.rscudwservice.widget.OverlapControlUDW");
        data.add(item);

        return data;
    }

    private List<SingleUdwViewHolder.UdwItem> getAgUdws(Context context) {
        List<SingleUdwViewHolder.UdwItem> data = new ArrayList<>();
        int i = 0;

        SingleUdwViewHolder.UdwItem item = new SingleUdwViewHolder.UdwItem(context, i++,
                "CrossTrackErrorStatusUDW",
                "com.cnh.pf.agudwservice",
                "com.cnh.pf.agudwservice.widget.CrossTrackErrorStatusUDW");
        data.add(item);

        item = new SingleUdwViewHolder.UdwItem(context, i++,
                "RowGuideOffsetUDW",
                "com.cnh.pf.agudwservice",
                "com.cnh.pf.agudwservice.widget.RowGuideOffsetUDW");
        data.add(item);

        return data;
    }

    private List<SingleUdwViewHolder.UdwItem> getYmUdws(Context context) {
        List<SingleUdwViewHolder.UdwItem> data = new ArrayList<>();
        int i = 0;

        SingleUdwViewHolder.UdwItem item = new SingleUdwViewHolder.UdwItem(context, i++,
                "AverageYieldUDW",
                "com.cnh.pf.ymudwservice",
                "com.cnh.pf.ymudwservice.widget.AverageYieldUDW");
        data.add(item);

        item = new SingleUdwViewHolder.UdwItem(context, i++,
                "AverageYieldCounterUDW",
                "com.cnh.pf.ymudwservice",
                "com.cnh.pf.ymudwservice.widget.AverageYieldCounterUDW");
        data.add(item);

        item = new SingleUdwViewHolder.UdwItem(context, i++,
                "AverageMoistureUDW",
                "com.cnh.pf.ymudwservice",
                "com.cnh.pf.ymudwservice.widget.AverageMoistureUDW");
        data.add(item);

        item = new SingleUdwViewHolder.UdwItem(context, i++,
                "AverageFlowUDW",
                "com.cnh.pf.ymudwservice",
                "com.cnh.pf.ymudwservice.widget.AverageFlowUDW");
        data.add(item);

        item = new SingleUdwViewHolder.UdwItem(context, i++,
                "CropTemperatureUDW",
                "com.cnh.pf.ymudwservice",
                "com.cnh.pf.ymudwservice.widget.CropTemperatureUDW");
        data.add(item);

        item = new SingleUdwViewHolder.UdwItem(context, i++,
                "AverageFlowCounterUDW",
                "com.cnh.pf.ymudwservice",
                "com.cnh.pf.ymudwservice.widget.AverageFlowCounterUDW");
        data.add(item);

        item = new SingleUdwViewHolder.UdwItem(context, i++,
                "CrossTrackErrorStatusUDW",
                "com.cnh.pf.agudwservice",
                "com.cnh.pf.agudwservice.widget.CrossTrackErrorStatusUDW");
        data.add(item);

        item = new SingleUdwViewHolder.UdwItem(context, i++,
                "RowGuideOffsetUDW",
                "com.cnh.pf.agudwservice",
                "com.cnh.pf.agudwservice.widget.RowGuideOffsetUDW");
        data.add(item);

        return data;
    }
}
