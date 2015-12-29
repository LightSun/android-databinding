package com.heaven7.databinding.core.listener;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by heaven7 on 2015/12/29.
 */
public class OnTouchListenerImpl extends ListenerImplContext implements View.OnTouchListener {

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        /**
         * 在adapter中。。事件的参数默认4个 (View v, Integer position, User user, AdapterManager<?> am)
         * 那么 onTouch事件在adapter中的参数为 (View v,  //ps[0]
         *            MotionEvent event ,               //ps[1]
         *            Integer position, User user, AdapterManager<?> am) //剩下的通过, System.arraycopy从mParams拷贝过来的
         */
        int count = mParams.length;
        Object[] ps = new Object[count + 1];
        ps[0] = mParams[0]; //view
        // stand listener param
        ps[1] = event;
        //extra data
        if (count > 1) {
            System.arraycopy(mParams, 1, ps, 2, count - 1);
        }
        Object val = invokeCallback(ps);
        return val == null || (boolean)val;
    }
}
