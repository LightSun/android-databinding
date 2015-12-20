package com.heaven7.databinding.util;

import android.content.Context;
import android.view.View;

/**
 * Created by heaven7 on 2015/9/2.
 */
public class ViewUtil {

    public static void obtainFocus(View v){
        v.setFocusable(true);
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.requestFocusFromTouch();
    }
    public static void loseFocus(View v){
        v.setFocusable(false);
        v.setFocusableInTouchMode(false);
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static float getDpSize(Context context, float dpValue){
        return context.getResources().getDisplayMetrics().density * dpValue;
    }

    public static float getSpSize(Context context, float spValue){
        return context.getResources().getDisplayMetrics().scaledDensity * spValue;
    }


}
