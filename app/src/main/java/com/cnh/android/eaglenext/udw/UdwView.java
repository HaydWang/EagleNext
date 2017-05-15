package com.cnh.android.eaglenext.udw;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.cnh.android.eaglenext.R;
import com.cnh.android.windowmanager.util.WmUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Hai on 4/22/17.
 */
public class UdwView extends GenericUdw<UdwView> {
    boolean mIsEditMode = false;
    boolean mIsMockup = false;

    public UdwView(String id, String packageName, String className) {
        super(id, packageName, className);
    }

    public UdwView(GenericUdw<?> el, boolean isEditMode) {
        super(el);

        mIsEditMode = isEditMode;
        mIsMockup = el instanceof MockupUdw;
    }

    @Override
    public View getUdwView(Context context) {
        // Cached view
        if (mUdwView != null) return mUdwView;

        // Inflate UDW stub
        try {
            // Dynamic class loading from different apk
            mUdwView = loadExternalUdw(context, this);
        }
        catch (Exception e) {
            e.printStackTrace();
            // Error view
            mUdwView = new MockupUdw(this, new Exception("Udw not found"), false).getView(context);

            Toast.makeText(context, "Load UDW error: " + e.toString(), Toast.LENGTH_SHORT).show();
        }

        return mUdwView;
    }

    // Inflater view and add UDW
    @Override
    public View getView(Context context) {
        // Cached view
        if (mView != null) return mView;

        // Inflate layout
        mView = LayoutInflater.from(context).inflate(R.layout.view_display_udw, null);

        // Place real UDW
        View udwStub = mView.findViewById(R.id.udwStub);
        if (udwStub != null) {
            ((FrameLayout) udwStub).addView(getUdwView(context));
        }

        return mView;
    }

    private WmUtils mWmUtils;
    public View loadExternalUdw(Context context, GenericUdw<?> el)
            throws PackageManager.NameNotFoundException, ClassNotFoundException, IllegalArgumentException, InstantiationException,
            IllegalAccessException, InvocationTargetException, NoSuchMethodException, NullPointerException {
        if (mWmUtils == null) mWmUtils = WmUtils.getInstance();

        // The loaded element extends a ViewGroup and implements UDW, but:
        // the UDW class here and in the external apk are considered *different*
        // because they belong to different class loaders. Thus, we cannot cast
        // to UDW here. Nevertheless, the View class is loaded by SystemClassLoader
        // so a cast to View is safe.
        return mWmUtils.loadExternalView(el.packageName, el.className);
    }
}
