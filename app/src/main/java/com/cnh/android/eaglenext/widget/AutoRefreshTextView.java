package com.cnh.android.eaglenext.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;
import com.cnh.android.eaglenext.R;


/**
 * Created by Hai on 14/05/2017.
 */
public class AutoRefreshTextView extends TextView {
    private static final int REFRESH_DELAY = 1000;

    private final Handler mHandler = new Handler();
    private final Runnable mTimeRefresher = new Runnable() {
        @Override
        public void run() {
            String str = getLatestText();
            AutoRefreshTextView.this.setText(str);
            mHandler.postDelayed(this, REFRESH_DELAY);
        }
    };

    // Generate dummy data
    private String getLatestText() {
        switch (dataType) {
            case 0:
                return dummyMph();
            case 1:
                return dummyMiles();
            case 2:
                return dummyGal();
            default:
                return dummyMph();
        }
    }

    // Hack way to set dummy data, remove it in production
    private int dataType = 0;
    private float mph = 34.5f;
    private String dummyMph() {
        // MPH from 34.5 ~ 48
        mph +=  Math.random();
        if (mph > 48) mph = 34.5f;
        return String.format("%.2f", mph);
    }

    private float miles = 75;
    private String dummyMiles() {
        // miles from 75 ~ 560
        miles +=  Math.random() * 10;
        if (miles > 560) miles = 75;
        return String.format("%.2f", miles);
    }

    private float gal = 60;
    private String dummyGal() {
        // miles from 60 ~ 4.5
        gal -=  Math.random();
        if (gal < 4.5) gal = 60;
        return String.format("%.1f", gal);
    }

    public AutoRefreshTextView(Context context) {
        super(context);
    }

    public AutoRefreshTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupAttributes(attrs);
    }

    public AutoRefreshTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setupAttributes(attrs);
    }

    private void setupAttributes(AttributeSet attrs) {
        // Obtain a typed array of attributes
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs,
                R.styleable.AutoRefreshTextView, 0, 0);
        try {
            dataType = a.getInteger(R.styleable.AutoRefreshTextView_dataType, 0);
        } finally {
            // TypedArray objects are shared and must be recycled.
            a.recycle();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        mHandler.post(mTimeRefresher);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        mHandler.removeCallbacks(mTimeRefresher);
    }
}