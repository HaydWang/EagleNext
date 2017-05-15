package com.cnh.android.eaglenext.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.cnh.android.eaglenext.R;

/**
 * Created by Hai on 14/05/2017.
 */
public class UdwFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_udw, container, false);
        ButterKnife.bind(this, view);

        return view;
    }
}
