package com.heaven7.databinding.core;

import android.app.Activity;
import android.view.View;

import com.heaven7.databinding.core.listener.ListenerImplContext;

import org.heaven7.core.viewhelper.ViewHelper;

/**
 * this class help you to create the IDataBinder instance and register the self event listener.
 * Created by heaven7 on 2016/3/28.
 */
public final class DataBindingFactory {

    private DataBindingFactory(){}

    /**
     *  create an instance of IDataBinder.
     * @param activity   the Activity
     * @param bindsRawResId  the raw resource id of data bind.
     * @param cacheXml to cache xml for reuse
     */
    public static IDataBinder createDataBinder(Activity activity,int bindsRawResId,boolean cacheXml){
        return createDataBinder(activity.getWindow().getDecorView(), bindsRawResId, cacheXml);
    }
    /**
     *  create an instance of IDataBinder and not cache the xml.
     * @param activity   the Activity
     * @param bindsRawResId  the raw resource id of data bind.
     */
    public static IDataBinder createDataBinder(Activity activity,int bindsRawResId){
        return createDataBinder(activity.getWindow().getDecorView(), bindsRawResId, false);
    }
    /**
     *  create an instance of IDataBinder.
     * @param root   the root view of activity or fragment or others.
     * @param bindsRawResId  the raw resource id of data bind.
     * @param cacheXml to cache xml for reuse
     */
    public static IDataBinder createDataBinder(View root,int bindsRawResId,boolean cacheXml){
        return createDataBinder(new ViewHelper(root), bindsRawResId, cacheXml);
    }
    /**
     *  create an instance of IDataBinder and not cache the xml.
     * @param root   the root view of activity or fragment or others.
     * @param bindsRawResId  the raw resource id of data bind.
     */
    public static IDataBinder createDataBinder(View root,int bindsRawResId){
        return createDataBinder(new ViewHelper(root), bindsRawResId, false);
    }

    /**
     * create an instance of IDataBinder.
     * @param vp   the view helper
     * @param bindsRawResId  the raw resource id of data bind.
     * @param cacheXml to cache xml for reuse
     */
    public static IDataBinder createDataBinder(ViewHelper vp ,int bindsRawResId,boolean cacheXml){
        return new DataBinder(vp, bindsRawResId,cacheXml);
    }
    /**
     * create an instance of IDataBinder.
     * @param vp   the view helper
     * @param bindsRawResId  the raw resource id of data bind.
     */
    public static IDataBinder createDataBinder(ViewHelper vp ,int bindsRawResId){
        return new DataBinder(vp, bindsRawResId,false);
    }

    /** regist the event listener. ,the class clazz must have empty constructor
     * @param propertyName the propername to map the event,it often configed in the databinding xml.
     * @param clazz  must extends ListenerImplContext
     */
    public static void registEventListener(String propertyName,Class<? extends ListenerImplContext> clazz){
        ListenerFactory.registEventListener(propertyName,clazz);
    }
}
