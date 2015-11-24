package com.heaven7.databinding.core.listener;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by heaven7 on 2015/11/23.
 */
public class OnItemClickListenerImpl extends ListenerImplContext implements AdapterView.OnItemClickListener{

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //in adapter view params[0] is useless
        int paramCount = mParams.length -1 + 4;
        Object[] ps = new Object[paramCount];
        ps[0] = parent;
        // stand listener param
        ps[1] = view;
        ps[2] = position;
        ps[3] = id;
        //extra data
        if(mParams.length > 1){
            System.arraycopy(mParams,1, ps , 4, mParams.length - 1);
        }
        invokeCallback(ps);
    }
}
