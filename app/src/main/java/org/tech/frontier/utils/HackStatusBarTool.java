package org.tech.frontier.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.widget.RelativeLayout;

import org.tech.frontier.R;


/**
 * Created by kang on 15/4/25-下午9:24.
 * 控制状态栏的高度
 * 从 Fuboo 学来的～
 */
public class HackStatusBarTool {
    public static void hackit(Activity activity) {
        if (Build.VERSION.SDK_INT >= 19) {
            activity.findViewById(R.id.hack_status_bar)
                    .setLayoutParams(new RelativeLayout.LayoutParams(-1, getStatusBarHeight(activity)));
        }
    }

    public static int getStatusBarHeight(Context paramContext)
    {
        int i = paramContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int j = 0;
        if (i > 0)
            j = paramContext.getResources().getDimensionPixelSize(i);
        return j;
    }
}
