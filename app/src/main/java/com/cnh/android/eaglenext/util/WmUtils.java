package com.cnh.android.eaglenext.util;

import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.cnh.android.eaglenext.R;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import dalvik.system.PathClassLoader;

import static android.content.Context.CONTEXT_IGNORE_SECURITY;
import static android.content.Context.CONTEXT_INCLUDE_CODE;

/**
 * Created by f10210c on 5/15/2017.
 */
public class WmUtils {

    private static final String TAG = WmUtils.class.getSimpleName();

    /**
     * Size of the display in inches
     */
    public static int
            SIZE_HAWK = 8,
            SIZE_PHOENIX = 12;

    /**
     * codename of the display
     */
    public static String
            TYPE_HAWK = "hawk",
            TYPE_PHOENIX = "phoenix",
            TYPE_HAWK_WHEEL_LOADER = "hawkwl",
            TYPE_GALAXY = "espresso10wifi";

    private static WmUtils thisInstance;

    private static Map<String, Context> mCachedContexts = new HashMap<String, Context>();

    private static Map<String, Class<?>> mCachedClasses = new HashMap<String, Class<?>>();

    private Context mContext;

    private WmUtils(Context context) {
        mContext = context;
    }

    /**
     * Get a WmUtils instance.
     */
    public static synchronized WmUtils getInstance(Context context) {
        if (thisInstance == null) {
            // Use the application rather than the context, since it's safer to save
            if (context.getApplicationContext() != null) context = context.getApplicationContext();

            thisInstance = new WmUtils(context);
        }

        return thisInstance;
    }

    /**
     * Get a WmUtils instance.
     */
    public static synchronized WmUtils getInstance() {
        if (thisInstance == null) {
            // Use the application rather than the context, since it's safer to save
            thisInstance = new WmUtils(getCurrentApplication());
        }

        return thisInstance;
    }

    /**
     * Get the current application object.
     */
    public static Application getCurrentApplication() {
        // Try to get a context from current application
        try {
            final Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            return (Application) activityThreadClass.getMethod("currentApplication").invoke(null);
        }
        catch (Exception e) { Log.e(TAG, e.getClass().getSimpleName(), e); }

        return null;
    }

    /**
     * Helper method to load a class from an external APK.
     *
     * @param className The FQDN name of the class.
     * @return The loaded class.
     * @throws PackageManager.NameNotFoundException
     * @throws ClassNotFoundException
     */
    public Class<?> loadExternalClass(String packageName, String className)
            throws PackageManager.NameNotFoundException, ClassNotFoundException {

        String key = packageName+"/"+className;

        if (!mCachedClasses.containsKey(key)) {
            // Dynamic class loading from different apk
            Context apkContext = loadExternalContext(packageName);

            // Alternative: DexClassLoader, cfr. https://github.com/dalinaum/custom-class-loading-sample/blob/master/src/com/example/dex/MainActivity.java
            String apkName = apkContext.getPackageManager().getApplicationInfo(packageName, 0).sourceDir;
            PathClassLoader pathClassLoader = new PathClassLoader(apkName, apkContext.getClassLoader());

            mCachedClasses.put(key, Class.forName(className, true, pathClassLoader));
        }

        return mCachedClasses.get(key);
    }

    /**
     * The LayoutInflater caches the constructors of every view, this can cause problems if the views are
     * loaded via reflection. In case of exceptions loading views call this method to clear the cache.
     * Do not overuse as it can kill the performance.
     * @param context
     */
    public static void clearInflaterCache(Context context) {
        try {
            Field ctorMapField = LayoutInflater.class.getDeclaredField("sConstructorMap");
            ctorMapField.setAccessible(true);
            @SuppressWarnings("unchecked")
            HashMap<String, Constructor<? extends View>> ctorMap = (HashMap<String, Constructor<? extends View>>)
                    ctorMapField.get(LayoutInflater.from(context));
            Iterator<String> i = ctorMap.keySet().iterator();
            while (i.hasNext()) {
                String name = i.next();
                if (name.startsWith("com.cnh")) i.remove();
            }
        }
        catch (Exception e) { e.printStackTrace(); }
    }

    /**
     * Like clearInflaterCache, but it only clears the constructors of the classes inside the "com.cnh.android.widget." package
     * @param context
     */
    public static void clearWidgetCache(Context context) {
        try {
            Field ctorMapField = LayoutInflater.class.getDeclaredField("sConstructorMap");
            ctorMapField.setAccessible(true);
            @SuppressWarnings("unchecked")
            HashMap<String, Constructor<? extends View>> ctorMap = (HashMap<String, Constructor<? extends View>>)
                    ctorMapField.get(LayoutInflater.from(context));
            Iterator<String> i = ctorMap.keySet().iterator();
            while (i.hasNext()) {
                String name = i.next();
                if (name.startsWith("com.cnh.android.widget.")) i.remove();
            }
        }
        catch (Exception e) { e.printStackTrace(); }
    }

    /**
     * Load the android Context from an external APK
     * @param packageName the package of the apk as defined in the manifest
     * @return the external context;
     * @throws PackageManager.NameNotFoundException if the package was not found
     * @throws NullPointerException if the context couldn't be loaded
     */
    public synchronized Context loadExternalContext(String packageName)
            throws PackageManager.NameNotFoundException, NullPointerException {

        if (!mCachedContexts.containsKey(packageName)) {
            Context context = mContext.createPackageContext(packageName, CONTEXT_INCLUDE_CODE | CONTEXT_IGNORE_SECURITY);
            mCachedContexts.put(packageName, context);
        }

        return mCachedContexts.get(packageName);
    }

    /**
     * Loads a String from a resource contained in an external APK
     * @param packageName the package of the external apk as defined in the manifest
     * @param resourceName the name of the resource
     * @return the string value.
     * @throws PackageManager.NameNotFoundException if the resource couldn't be found
     */
    public String loadExternalString(String packageName, String resourceName)
            throws PackageManager.NameNotFoundException {

        if (TextUtils.isEmpty(resourceName)) return null;

        Context context = loadExternalContext(packageName);

        int resId = context.getResources().getIdentifier(resourceName, "string", packageName);

        return resId != 0 ? context.getResources().getString(resId) : null;
    }

    /**
     * Loads a Drawable resource from an external APK
     * @param packageName the package of the external apk as defined in the manifest
     * @param resourceName the name of the resource
     * @return the drawable object
     * @throws PackageManager.NameNotFoundException if the resource couldn't be found
     */
    public Drawable loadExternalDrawable(String packageName, String resourceName)
            throws PackageManager.NameNotFoundException {

        if (TextUtils.isEmpty(resourceName)) return null;

        Context context = loadExternalContext(packageName);

        int resId = context.getResources().getIdentifier(resourceName, "drawable", packageName);

        return resId != 0 ? context.getResources().getDrawable(resId) : null;
    }

    public AssetFileDescriptor loadExternalSoundFileDescriptor(String packageName, String resourceName)
            throws PackageManager.NameNotFoundException {

        Context context = loadExternalContext(packageName);
        int identifier = context.getResources().getIdentifier(
                "raw/" + resourceName, null, packageName);

        return context.getResources().openRawResourceFd(identifier);
    }

    /**
     * Load a UDW from external APK.
     */
    public synchronized View loadExternalView(String packageName, String className)
            throws PackageManager.NameNotFoundException, ClassNotFoundException, ClassCastException, IllegalArgumentException, InstantiationException,
            IllegalAccessException, InvocationTargetException, NoSuchMethodException, NullPointerException {

        // Dynamic class loading from different apk
        Context apkContext = loadExternalContext(packageName);
        Class<?> cls = loadExternalClass(packageName, className);

        // Clear inflater cache to avoid clashes between LayoutInflaters!
        clearInflaterCache(apkContext);

        // The View class is loaded by SystemClassLoader, so a cast to View is safe.
        return (View) cls.getConstructor(Context.class).newInstance(apkContext);
    }

    /**
     * Removes a class from this object's classes cache
     * @param className the name of the class
     */
    public void clearClassesCache(String className) {
        if (className == null) mCachedClasses = new HashMap<String, Class<?>>();
        else if (mCachedClasses.containsKey(className)) mCachedClasses.remove(className);
    }

    /**
     * Removes a context from this object's context cache
     * @param packageName the package name associated with this context
     */
    public void clearContextsCache(String packageName) {
        if (packageName == null) mCachedContexts = new HashMap<String, Context>();
        else if (mCachedContexts.containsKey(packageName)) mCachedContexts.remove(packageName);
    }

    /**
     * @param c the android Context
     * @param resource the resource to be encoded
     * @return a byte array from an android drawable resource
     */
    public static byte[] encodeDrawable(Context c, int resource) {
        return encodeDrawable(BitmapFactory.decodeResource(c.getResources(), resource));
    }

    /**
     * @param drawable a Drawable resource object
     * @return a byte array from an android drawable resource
     */
    public static byte[] encodeDrawable(Drawable drawable) throws IllegalArgumentException {
        try {
            return encodeDrawable(((BitmapDrawable)drawable).getBitmap());
        } catch (ClassCastException cce) {
            throw new IllegalArgumentException("Drawable must be instance of BitmapDrawable");
        }
    }

    /**
     * @param bmp a Bitmap resource object
     * @return a byte array from an android drawable resource
     */
    public static byte[] encodeDrawable(Bitmap bmp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    /**
     * converts a byte array into a drawable resource
     * @param c an Android context
     * @param array the byte array
     * @return a Drawable
     */
    public static Drawable decodeDrawable(Context c, byte[] array) {
        Bitmap b = BitmapFactory.decodeByteArray(array, 0, array.length);
        return new BitmapDrawable(c.getResources(), b);
    }

    /**
     * checks if a package is installed on the system
     * @param targetPackage the name of the package
     * @return true if the package is installed.
     */
    public boolean isPackageInstalled(String targetPackage) {
        List<ApplicationInfo> packages = mContext.getPackageManager().getInstalledApplications(0);
        for (ApplicationInfo packageInfo: packages) if (packageInfo.packageName.equals(targetPackage)) {
            return true;
        }

        return false;
    }
}