package org.tech.frontier.app;

import android.app.Application;
import android.content.Context;

import org.tech.frontier.utils.AppTools;

/**
 * Created by kang on 15/5/14-下午6:01.
 */
public class TTFApp extends Application {
    private static final String TAG = "com.github.tiiime.citybox.app.CityBoxApp";
    private static Context mContext = null;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        AppTools.init(mContext);

    }
}
