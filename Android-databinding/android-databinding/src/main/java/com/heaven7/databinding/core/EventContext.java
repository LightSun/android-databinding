package com.heaven7.databinding.core;


import com.heaven7.core.util.ViewHelper;

/**
 * the abstract event context of view's onClick ,onLongClick, textChangeListener or others.
 *  and i suggest the event handler shouldn't have burden method. or else may cause bug.
 * Created by heaven7 on 2015/11/19.
 */
public abstract class EventContext {

     private final IDataBinder mBinder;

    public EventContext(IDataBinder binder) {
        this.mBinder = binder;
    }

    public IDataBinder getDataBinder() {
        return mBinder;
    }

    public ViewHelper getViewHelper(){
        return getDataBinder().getViewHelper();
    }
}
