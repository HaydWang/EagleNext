package com.cnh.android.panelmanager;

import android.content.Context;
import android.graphics.PixelFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Transformation;

public class PanelManager_MainActivity extends AppCompatActivity {
    WindowManager.LayoutParams mTopLp;
    View mTopView;

    WindowManager.LayoutParams mLeftLp;
    View mLeftView;

    WindowManager.LayoutParams mBottomLp;
    View mBottomView;

    WindowManager mWm;

    static int SCREEN_SIZE_WIDTH = 0;
    static int SCREEN_SIZE_HEIGHT = 0;
    static float RATIO = 3.0f; // dm.density

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_manager_main);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        RATIO = dm.density;
        //SCREEN_SIZE_WIDTH = (int) (360 * RATIO);
        //SCREEN_SIZE_HEIGHT = (int) (640 * RATIO);
        //SCREEN_SIZE_WIDTH = dm.widthPixels;
        //SCREEN_SIZE_HEIGHT = dm.heightPixels;
        SCREEN_SIZE_WIDTH = 1440;
        SCREEN_SIZE_HEIGHT = 2560;

        mWm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);

        //addPanels();
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
        //mTopView.setOnTouchListener(this);

        /* left view param */
        mLeftLp = new WindowManager.LayoutParams(0, 0, type, flags, PixelFormat.TRANSPARENT);
        mLeftLp.gravity = Gravity.LEFT | Gravity.TOP;
        mLeftLp.setTitle("CNH-LeftPanel");

        /* left view */
        mLeftView = View.inflate(getApplicationContext(), R.layout.view_panel_manager_left_panel, null);
        //mTopView.setOnTouchListener(this);

        /* bottom view param */
        mBottomLp = new WindowManager.LayoutParams(0, 0, type, flags, PixelFormat.TRANSPARENT);
        mBottomLp.gravity = Gravity.LEFT | Gravity.TOP;
        mBottomLp.setTitle("CNH-BottomPanel");

        /* top view */
        mBottomView = View.inflate(getApplicationContext(), R.layout.view_panel_manager_bottom_panel, null);
        //mTopView.setOnTouchListener(this);

        updateLayoutParams();

        mWm.addView(mTopView, mTopLp);
        mWm.addView(mLeftView, mLeftLp);
        mWm.addView(mBottomView, mBottomLp);
    }

    private void updateLayoutParams(){
        mTopLp.x = 0;
        mTopLp.y = 0;
        mTopLp.width = SCREEN_SIZE_HEIGHT;
        mTopLp.height = 240;

        mLeftLp.x = 0;
        mLeftLp.y = mTopLp.height;
        mLeftLp.width = 400;
        mLeftLp.height = SCREEN_SIZE_WIDTH - mTopLp.height;

        mBottomLp.x = mLeftLp.width;
        mBottomLp.y = SCREEN_SIZE_WIDTH - 160;
        mBottomLp.width = SCREEN_SIZE_HEIGHT - mBottomLp.x;
        mBottomLp.height = 160;
    }
}
