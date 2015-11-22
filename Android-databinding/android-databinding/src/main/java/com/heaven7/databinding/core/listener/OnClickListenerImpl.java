package com.heaven7.databinding.core.listener;

import android.view.View;

/**
 * Created by heaven7 on 2015/11/20.
 */
public class OnClickListenerImpl extends ListenerImplContext implements View.OnClickListener{

    public OnClickListenerImpl(){}

    @Override
    public void onClick(View v) {
        invokeCallback();
    }
}
