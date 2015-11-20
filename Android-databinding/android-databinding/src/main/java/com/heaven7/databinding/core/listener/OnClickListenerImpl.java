package com.heaven7.databinding.core.listener;

import android.view.View;

import com.heaven7.databinding.core.DataBindException;

import java.lang.reflect.Method;

/**
 * Created by heaven7 on 2015/11/20.
 */
public class OnClickListenerImpl extends ListenerImplContext implements View.OnClickListener{

    public OnClickListenerImpl(){}

    public OnClickListenerImpl(Method method, Object holder, Object[] params) {
        super(method, holder, params);
    }

    @Override
    public void onClick(View v) {
        try {
            method.invoke(holder, params);
        } catch (Exception e) {
            throw new DataBindException(e);
        }
    }
}
