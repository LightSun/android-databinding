package com.heaven7.databinding.core;


import com.heaven7.adapter.AdapterManager;
import com.heaven7.adapter.ISelectable;

/**
 * Created by heaven7 on 2015/11/29.
 */
/*public*/ interface IAdapterBindTracker {

    AdapterManager<? extends ISelectable> getAdapterManager(int adapterHash);
    void putAdapterManager(int adapterHash,AdapterManager< ? extends ISelectable> am);
    void removeAdapterManager(int adapterHash);

    void beginBindItem(int position, Object item);

    Object getCurrentItem();
    int getCurrentPosition();

    void endBind();


    /** in adapter data should cache until the activity is destroyed. so  we need long-standing */
    void putLongStandingData(String variable, Object data);


}
