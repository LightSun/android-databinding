package com.heaven7.databinding.core.listener;

import android.view.View;

/**
 * Created by heaven7 on 2015/12/20.
 */
public class OnFocusChangeListenerImpl extends ListenerImplContext implements View.OnFocusChangeListener {

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        int count = mParams.length;
        Object[] ps = new Object[count + 1];
        ps[0] = mParams[0]; //view
        // stand listener param
        ps[1] = hasFocus;
        //extra data
        if (count > 1) {
            System.arraycopy(mParams, 1, ps, 2, count - 1);
        }
        invokeCallback(ps);
    }
}
