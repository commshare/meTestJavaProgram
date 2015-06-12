package com.qingchenglei.toolbardemo.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.WindowManager;

import com.qingchenglei.toolbardemo.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by Zech on 2015/5/6.
 * <p/>
 * tool class
 */
public class Tools {

    /**
     * get the dark color
     * @param color
     * @return
     */
    public static int getDarkerColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.9f;
        return Color.HSVToColor(hsv);
    }

    /**
     * set the status bar color
     * @param activity
     * @param colorRes
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void setStatusBar(Activity activity, int colorRes) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        int color = activity.getResources().getColor(colorRes);
        tintManager.setStatusBarTintDrawable(new ColorDrawable(getDarkerColor(color)));
    }

    /**
     * get the toolbar height
     * @param context
     * @return
     */
    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return toolbarHeight;
    }

    /**
     * get the tab height
     * @param context
     * @return
     */
    public static int getTabsHeight(Context context) {
        return (int) context.getResources().getDimension(R.dimen.tab_height);
    }
}
