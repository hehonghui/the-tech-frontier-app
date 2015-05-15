package org.tech.frontier.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

/**
 * Created by kang on 15/3/16-下午3:05.
 */
public class AppTools {
    private static Context mContext;


    public static void init(Context context) {
        mContext = context;
    }


    public static int getColor(int paramInt) {
        return mContext.getResources().getColor(paramInt);
    }

    public static Context getContext() {
        return mContext;
    }

    public static float getDimension(int paramInt) {
        return mContext.getResources().getDimension(paramInt);
    }

    public static int getDimensionPixelSize(int paramInt) {
        return mContext.getResources().getDimensionPixelSize(paramInt);
    }


    public static Drawable getDrawable(int paramInt) {
        return mContext.getResources().getDrawable(paramInt);
    }

    public static Resources getResources() {
        return mContext.getResources();
    }

    public static String getString(int paramInt) {
        return mContext.getResources().getString(paramInt);
    }

    public static String getString(int paramInt, Object[] paramArrayOfObject) {
        return mContext.getString(paramInt, paramArrayOfObject);
    }

}
