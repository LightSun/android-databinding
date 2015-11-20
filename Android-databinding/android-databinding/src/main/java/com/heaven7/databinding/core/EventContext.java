package com.heaven7.databinding.core;

/**
 * the event context of view's onClick or others.
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
}
