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
        mAdapter = new SingleUdwRecyclerViewAdapter(mView.getContext());
        changeType(dataType);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        //mRecyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        mRecyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(getResources().getInteger(R.integer.udw_list_grid),
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
        List<SingleUdwViewHolder.UdwItem> data = new ArrayList<>();
        switch (dataType) {
            case 0:
                generateAgUdws(mView.getContext(), data);
                generatePfUdws(mView.getContext(), data);
                generateYmUdws(mView.getContext(), data);
                generatePfUdws(mView.getContext(), data);
                break;
            case 1:
                generateAgUdws(mView.getContext(), data);
                break;
            case 2:
                generateYmUdws(mView.getContext(), data);
                break;
            case 3:
                generateTestUdws(mView.getContext(), data);
                break;
            default:
                generatePfUdws(mView.getContext(), data);
                break;
        }
        mAdapter.setData(data);
    }

    public void setType(int i) {
        dataType = i;
        if (mAdapter != null) changeType(dataType);
    }

    // TODO: so ugly :(  will build a factory for UDWs.
    private void generatePfUdws(Context context, List<SingleUdwViewHolder.UdwItem> data) {
        int i = data.size();

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
    }

    private void generateAgUdws(Context context, List<SingleUdwViewHolder.UdwItem> data) {
        int i = data.size();

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
    }

    private void generateYmUdws(Context context, List<SingleUdwViewHolder.UdwItem> data) {
        int i = data.size();

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
    }

    private void generateTestUdws(Context context, List<SingleUdwViewHolder.UdwItem> data) {
        int i = data.size();

        SingleUdwViewHolder.UdwItem item = new SingleUdwViewHolder.UdwItem(context, i++,
                "CrossTrackErrorStatusUDW",
                "com.cnh.pf.agudwservice",
                "com.cnh.pf.agudwservice.widget.CrossTrackErrorStatusUDW");
        data.add(item);
    }
}
