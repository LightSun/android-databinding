package com.heaven7.databinding.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;

/**
 * Created by heaven7 on 2016/3/23.
 */
public class ResCompatUtil  {

    public static Drawable getDrawable(Context context,int resId,Resources.Theme theme){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            return context.getResources().getDrawable(resId,theme);
        }else{
            return context.getResources().getDrawable(resId);
        }
    }
   /* public static int getColor(Context context,int resId,Resources.Theme theme){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            return context.getResources().getColor(resId, theme);
        }else{
            return context.getResources().getColor(resId);
        }
    }*/
}
