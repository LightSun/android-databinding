package com.heaven7.databinding.core;

import org.heaven7.core.adapter.AdapterManager;
import org.heaven7.core.adapter.ISelectable;

/**
 * Created by heaven7 on 2015/11/29.
 */
public interface IAdapterBindTracker {

    AdapterManager<? extends ISelectable> getAdapterManager();
    void setAdapterManager(AdapterManager< ? extends ISelectable> am);

    void beginBindItem(int position, Object item);

    Object getCurrentItem();
    int getCurrentPosition();

    void endBind();


    /** in adapter data should cache until the activity is destroyed. so  we need long-standing */
    void putLongStandingData(String variable, Object data);


}
