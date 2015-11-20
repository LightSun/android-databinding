package com.heaven7.databinding.core.listener;

import android.view.View;

import com.heaven7.databinding.core.DataBindException;

import java.lang.reflect.Method;

/**
 * Created by heaven7 on 2015/11/20.
 */
public class OnLongClickListenerImpl extends ListenerImplContext implements View.OnLongClickListener{

    public OnLongClickListenerImpl(){}
    public OnLongClickListenerImpl(Method method, Object holder, Object[] params) {
        super(method, holder, params);
    }

    @Override
    public boolean onLongClick(View v) {
        try {
            Object val = method.invoke(holder,params);
            return val == null || (Boolean)val ;
        } catch (Exception e) {
            throw new DataBindException(e);
        }
    }
}
