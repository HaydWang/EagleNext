package com.cnh.android.eaglenext.fragment;

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
import com.cnh.android.eaglenext.MainActivity;
import com.cnh.android.eaglenext.R;
import com.cnh.android.eaglenext.model.SingleNxUdwRecyclerViewAdapter;
import com.cnh.android.eaglenext.model.SingleUdwRecyclerViewAdapter;
import com.cnh.android.eaglenext.view.RecyclerItemTouchHelperCallback;

/**
 * Created by Hai on 13/05/2017.
 */
public class TestFragment extends Fragment {
    @BindView(R.id.recyclerview_main)
    RecyclerView mMainUdwListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        ButterKnife.bind(this, view);

        // Show dummy UDWs
        SingleNxUdwRecyclerViewAdapter adapter = new SingleNxUdwRecyclerViewAdapter(view.getContext());
        adapter.setData(MainActivity.generateDummyUdws());
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new RecyclerItemTouchHelperCallback(adapter));
        itemTouchHelper.attachToRecyclerView(mMainUdwListView);
        mMainUdwListView.setLayoutManager(
                new StaggeredGridLayoutManager(getResources().getInteger(R.integer.udw_list_grid),
                OrientationHelper.VERTICAL));
        mMainUdwListView.addItemDecoration(
                new SingleUdwRecyclerViewAdapter.SpaceItemDecoration(4)); //TODO: move 2px to dimen
        mMainUdwListView.setItemAnimator(new DefaultItemAnimator());
        mMainUdwListView.setAdapter(adapter);

        return view;
    }
}
