package com.heaven7.databinding.core.listener;

import android.view.View;

/**
 * Created by heaven7 on 2015/11/20.
 */
public class OnLongClickListenerImpl extends ListenerImplContext implements View.OnLongClickListener{

    public OnLongClickListenerImpl(){}

    @Override
    public boolean onLongClick(View v) {
        Object val = invokeCallback();
        return val == null || (Boolean)val ;
    }
}
