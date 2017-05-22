package com.cnh.android.eaglenext;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.widget.TextView;

import butterknife.*;

import com.cnh.android.eaglenext.fragment.HomeFragment;
import com.cnh.android.eaglenext.fragment.OverviewFragment;
import com.cnh.android.eaglenext.fragment.TestFragment;
import com.cnh.android.eaglenext.fragment.UdwFragment;
import com.cnh.android.eaglenext.fragment.UserFragment;
import com.cnh.android.eaglenext.fragment.VehicleFragment;
import com.cnh.android.eaglenext.model.SingleNexUdwRecyclerViewAdapter;
import com.cnh.android.eaglenext.view.RecyclerItemTouchHelperCallback;
import com.cnh.android.eaglenext.view.SingleNexUdwViewHolder;
import com.cnh.android.eaglenext.widget.LedTextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    @BindView(R.id.main_cotainer) View mFrameLayoutMain;
    @BindView(R.id.viewstub_topbar_container) ViewGroup mFrameTopbar;
    @BindView(R.id.viewstub_leftbar_container) ViewGroup mFrameLeftbar;
    @BindView(R.id.viewstub_bottombar_container) ViewGroup mFrameBottombar;

    @BindView(R.id.bubbleview_popup_info) View viewPopupInfo;
    @BindView(R.id.bubbleview_apps) View viewApps;
    @BindView(R.id.bubbleview_connectivity_view) View viewConnectivity;

    @BindView(R.id.textview_connectivity) TextView tvConnectivity;

    OverviewFragment mFragmentOverview;
    UdwFragment mFragmentUdw;
    TestFragment mFragmentTest;
    VehicleFragment mVehicleFragment;
    HomeFragment mHomeFragment;
    UserFragment mUserFragment;

    WindowManager.LayoutParams mTopLp;
    View mTopView;

    WindowManager.LayoutParams mLeftLp;
    View mLeftView;

    WindowManager.LayoutParams mBottomLp;
    View mBottomView;

    WindowManager mWm;
    boolean mSinglePanel = false;

    static int SCREEN_SIZE_WIDTH = 0;
    static int SCREEN_SIZE_HEIGHT = 0;
    static float RATIO = 3.0f; // dm.density

    TopViews topViews;
    class TopViews {
        @BindView(R.id.tv_clock_time) LedTextView mClockView;

        @OnClick(R.id.clock_area)
        public void onClockClicked(View view) {
            if (mSinglePanel) {
                showOverviewFragment();
            } else {
                if (mPaused) {
                    showOverviewFragment();
                } else {
                    MainActivity.this.finish();
                }
            }
        }

        @OnClick(R.id.button_card_manager)
        public void fragmentCardManager(View view) {
            showFragment(R.id.button_card_manager);
        }

        @OnClick(R.id.button_home)
        public void fragmentHome(View view) {
            showFragment(R.id.button_home);
        }

        @OnClick(R.id.button_user)
        public void fragmentUser(View view) {
            showFragment(R.id.button_user);
        }

        // TODO: so ugly, fix me!!!
        boolean showingPopupInfo = false;
        @OnClick(R.id.button_popup_info)
        public void onPopupInfoButton(View view) {
            showingPopupInfo = !showingPopupInfo;
            viewPopupInfo.setVisibility(showingPopupInfo ? View.VISIBLE : View.GONE);

            showingConnectivity = false;
            viewConnectivity.setVisibility(View.GONE);

            showingApps = false;
            viewApps.setVisibility(View.GONE);
        }

        boolean showingApps = false;
        @OnClick(R.id.button_apps)
        public void onAppsButton(View view) {
            showingApps = !showingApps;
            viewApps.setVisibility(showingApps ? View.VISIBLE : View.GONE);

            showingConnectivity = false;
            viewConnectivity.setVisibility(View.GONE);

            showingPopupInfo = false;
            viewPopupInfo.setVisibility(View.GONE);
        }

        boolean showingConnectivity = false;
        @OnClick({R.id.button_gnss, R.id.button_help, R.id.button_signal})
        public void onConnectivityButtons(View view) {
            showingConnectivity = !showingConnectivity;
            String content = "GNSS";
            if(view != null) {
                switch (view.getId()) {
                    case R.id.button_help:
                        content = "Help";
                        break;
                    case R.id.button_signal:
                        content = "Telematics";
                        break;
                }
            }
            tvConnectivity.setText(content);
            viewConnectivity.setVisibility(showingConnectivity ? View.VISIBLE : View.GONE);

            showingPopupInfo = false;
            viewPopupInfo.setVisibility(View.GONE);

            showingApps = false;
            viewApps.setVisibility(View.GONE);
        }

        @OnClick(R.id.linearlayout_status_info)
        public void onStatusInfoClicked(View view) {
            showingConnectivity = !showingConnectivity;
            tvConnectivity.setText("Status Information");
            viewConnectivity.setVisibility(showingConnectivity ? View.VISIBLE : View.GONE);

            showingPopupInfo = false;
            viewPopupInfo.setVisibility(View.GONE);

            showingApps = false;
            viewApps.setVisibility(View.GONE);
        }

        public TopViews(View view) {
            ButterKnife.bind(this, view);
            mClockView.startClock();
        }

        public void hidePopup() {
            showingConnectivity = false;
            viewConnectivity.setVisibility(View.GONE);
            showingPopupInfo = false;
            viewPopupInfo.setVisibility(View.GONE);
            showingApps = false;
            viewApps.setVisibility(View.GONE);
        }
    }

    LeftViews leftViews;
    class LeftViews {
        @BindView(R.id.recyclerview_leftbar) RecyclerView mLeftUdwListView;

        public LeftViews(View view) {
            ButterKnife.bind(this, view);

            // Left Bar list
            SingleNexUdwRecyclerViewAdapter adapter =
                    new SingleNexUdwRecyclerViewAdapter(MainActivity.this);
            adapter.setData(generateDummyUdws());
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                    new RecyclerItemTouchHelperCallback(adapter));
            itemTouchHelper.attachToRecyclerView(mLeftUdwListView);
            mLeftUdwListView.setLayoutManager(
                    new StaggeredGridLayoutManager(1, OrientationHelper.VERTICAL));
            mLeftUdwListView.setItemAnimator(new DefaultItemAnimator());
            mLeftUdwListView.setAdapter(adapter);
        }
    }

    BottomViews bottomViews;
    class BottomViews {
        @OnClick(R.id.navigation_button_overview)
        public void onOverviewFragmentClicked(View view) {
            showFragment(R.id.navigation_button_overview);
        }

        @OnClick({R.id.navigation_button_udw_1,
                R.id.navigation_button_udw_2,
                R.id.navigation_button_udw_3,
                R.id.navigation_button_udw_4,
                R.id.navigation_button_udw_5,
                R.id.navigation_button_udw_6,
                R.id.navigation_button_udw_test})
        public void onUdwFragmentClicked(View view) {
            int id = R.id.navigation_button_udw_1;
            if (view != null) {
                id = view.getId();
            }
            showUdwFragment(id);
        }

        public BottomViews(View view) {
            ButterKnife.bind(this, view);
        }
    }

    protected void showUdwFragment(int id) {
        if (mFragmentUdw == null) mFragmentUdw = new UdwFragment();

        switch (id) {
            case R.id.navigation_button_udw_test:
                showTestUdwFragment();
                return;
            case R.id.navigation_button_udw_1:
                mFragmentUdw.setType(0);
                break;
            case R.id.navigation_button_udw_2:
                mFragmentUdw.setType(1);
                break;
            case R.id.navigation_button_udw_3:
                mFragmentUdw.setType(2);
                break;
            default:
                mFragmentUdw.setType(0);
                break;

        }
        transactFragment(mFragmentUdw, id);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        ButterKnife.bind(this);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        RATIO = dm.density;
        //SCREEN_SIZE_WIDTH = (int) (360 * RATIO);
        //SCREEN_SIZE_HEIGHT = (int) (640 * RATIO);
        //SCREEN_SIZE_WIDTH = dm.widthPixels;
        //SCREEN_SIZE_HEIGHT = dm.heightPixels;
        SCREEN_SIZE_WIDTH = 1440;
        SCREEN_SIZE_HEIGHT = 2560;
        mWm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);

        addPanels();

        if (mSinglePanel) {
            mFrameTopbar.addView(mTopView);
            mFrameTopbar.setVisibility(View.VISIBLE);

            mFrameLeftbar.addView(mLeftView);
            mFrameLeftbar.setVisibility(View.VISIBLE);

            mFrameBottombar.addView(mBottomView);
            mFrameBottombar.setVisibility(View.VISIBLE);
        }

        if (getIntent() != null) {
            int fragment = getIntent().getIntExtra("fragment_id", 0);
            showFragment(fragment);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Log.d("f10210c", "onNewIntent");
    }

    public void showFragment(int id) {
        switch (id) {
            case 0:
            case R.id.navigation_button_overview:
                showOverviewFragment();
                break;
            case R.id.button_card_manager:
                showCardManagerFragment();
                break;
            case R.id.button_home:
                showHomeFragment();
                break;
            case R.id.button_user:
                showUserFragment();
                break;
            default:
                showUdwFragment(id);
                break;
        }
    }

    private void showOverviewFragment() {
        if (mFragmentOverview == null) mFragmentOverview = new OverviewFragment();
        transactFragment(mFragmentOverview, R.id.navigation_button_overview);
    }

    private void showTestUdwFragment() {
        if (mFragmentTest == null) mFragmentTest = new TestFragment();
        transactFragment(mFragmentTest, R.id.navigation_button_udw_test);
    }

    private void showCardManagerFragment() {
        // TODO: shall call CardManagerFragment instead of VehicleFragment
        if (mVehicleFragment == null) mVehicleFragment = new VehicleFragment();
        transactFragment(mVehicleFragment, R.id.button_card_manager);
    }

    private void showHomeFragment() {
        if (mHomeFragment == null) mHomeFragment = new HomeFragment();
        transactFragment(mHomeFragment, R.id.button_home);
    }

    private void showUserFragment() {
        if (mUserFragment == null) mUserFragment = new UserFragment();
        transactFragment(mUserFragment, R.id.button_user);
    }

    private void transactFragment(Fragment fragment, int id) {
        if (mPaused) {
            Intent i = new Intent(this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("fragment_id", id);
            startActivity(i);
        } else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragments_container, fragment);
            transaction.commit();
        }
    }


    @OnClick(R.id.main_cotainer)
    public void onWholeActivityClocked(View view) {
        if (topViews != null) topViews.hidePopup();
    }

    private boolean mPaused = false;
    @Override
    public void onPause() {
        super.onPause();

        mPaused = true;
    }

    @Override
    public void onResume() {
        super.onResume();

        mPaused = false;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        // Not work on Nexus
        if (mSinglePanel) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LOW_PROFILE
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }

        // Work on Nexus
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public static List<SingleNexUdwViewHolder.UdwItem> generateDummyUdws() {
        List<SingleNexUdwViewHolder.UdwItem> data = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            SingleNexUdwViewHolder.UdwItem item = new SingleNexUdwViewHolder.UdwItem(i);
            item.icon = R.drawable.ic_multiline_chart_black_48dp;
            item.content = "Max: 65 mph \nMin: 12 mph";
            data.add(item);

            item = new SingleNexUdwViewHolder.UdwItem(i);
            item.icon = R.drawable.ic_local_gas_station_black_48dp;
            item.content = "120 ah";
            data.add(item);

            item = new SingleNexUdwViewHolder.UdwItem(i);
            item.icon = R.drawable.ic_nfc_black_48dp;
            item.switchVisible = true;
            item.switchChecked = true;
            item.switchDescription = "Overlap";
            data.add(item);

            item = new SingleNexUdwViewHolder.UdwItem(i);
            item.icon = R.drawable.ic_border_bottom_black_48dp;
            item.caption = "Coverage Area";
            item.content = "1.89 ha";
            data.add(item);

            item = new SingleNexUdwViewHolder.UdwItem(i);
            item.icon = R.drawable.ic_select_all_black_48dp;
            item.caption = "Area Counter";
            item.content = "0.06 ha";
            item.buttonVisible = true;
            data.add(item);
        }

        return data;
    }

    private void addPanels() {
        /* top view param */
        int flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        int type = 2300; //TYPE_CNH_PANEL = FIRST_SYSTEM_WINDOW + 300;
        //int type = WindowManager.LayoutParams.TYPE_TOAST; //TYPE_CNH_PANEL = FIRST_SYSTEM_WINDOW + 300;

        mTopLp = new WindowManager.LayoutParams(0, 0, type, flags, PixelFormat.TRANSPARENT);
        mTopLp.gravity = Gravity.LEFT | Gravity.TOP;
        mTopLp.setTitle("CNH-TopPanel");

        /* top view */
        mTopView = View.inflate(getApplicationContext(), R.layout.view_panel_manager_top_panel, null);

        /* left view param */
        mLeftLp = new WindowManager.LayoutParams(0, 0, type, flags, PixelFormat.TRANSPARENT);
        mLeftLp.gravity = Gravity.LEFT | Gravity.TOP;
        mLeftLp.setTitle("CNH-LeftPanel");

        /* left view */
        mLeftView = View.inflate(getApplicationContext(), R.layout.view_panel_manager_left_panel, null);

        /* bottom view param */
        mBottomLp = new WindowManager.LayoutParams(0, 0, type, flags, PixelFormat.TRANSPARENT);
        mBottomLp.gravity = Gravity.LEFT | Gravity.TOP;
        mBottomLp.setTitle("CNH-BottomPanel");

        /* top view */
        mBottomView = View.inflate(getApplicationContext(), R.layout.view_panel_manager_bottom_panel, null);

        updateLayoutParams();

        try {
            mWm.addView(mTopView, mTopLp);
            mWm.addView(mLeftView, mLeftLp);
            mWm.addView(mBottomView, mBottomLp);
        } catch (Exception e){
            e.printStackTrace();

            mSinglePanel = true;
        }

        topViews = new TopViews(mTopView);
        leftViews = new LeftViews(mLeftView);
        bottomViews = new BottomViews(mBottomView);
    }

    @BindDimen(R.dimen.topbar_height) int topBarHeight;
    @BindDimen(R.dimen.leftbar_width) int leftBarWidth;
    @BindDimen(R.dimen.bottom_bar_height) int bottomBarHeight;
    private void updateLayoutParams(){
        mTopLp.x = 0;
        mTopLp.y = 0;
        mTopLp.width = SCREEN_SIZE_HEIGHT;
        mTopLp.height = topBarHeight;

        mLeftLp.x = 0;
        mLeftLp.y = mTopLp.height;
        mLeftLp.width = leftBarWidth;
        mLeftLp.height = SCREEN_SIZE_WIDTH - mTopLp.height;

        mBottomLp.x = mLeftLp.width;
        mBottomLp.y = SCREEN_SIZE_WIDTH -  bottomBarHeight;
        mBottomLp.width = SCREEN_SIZE_HEIGHT - mBottomLp.x;
        mBottomLp.height = bottomBarHeight;
    }
}
