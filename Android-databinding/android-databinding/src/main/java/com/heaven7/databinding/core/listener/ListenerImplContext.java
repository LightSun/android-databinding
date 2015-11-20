package com.heaven7.databinding.core.listener;

import java.lang.reflect.Method;

/**
 * Created by heaven7 on 2015/11/20.
 */
public abstract class ListenerImplContext {

    protected Method method;
    protected Object holder;
    protected Object[] params;

    public ListenerImplContext(){}

    public ListenerImplContext(Method method, Object holder, Object[] params) {
        set(method, holder, params);
    }

    public void set(Method method, Object holder, Object[] params){
        this.method = method;
        this.holder = holder;
        this.params = params;
    }
}
