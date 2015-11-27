package com.heaven7.databinding.core.listener;

import android.view.View;
import android.widget.AdapterView;

import com.heaven7.databinding.core.DataBindException;

/**
 * Created by heaven7 on 2015/11/26.
 */
public class OnItemLongClickListenerImpl extends ListenerImplContext implements AdapterView.OnItemLongClickListener {

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
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
        try {
            Object val = invokeCallback(ps);
            return val == null || (Boolean) val;
        }catch (ClassCastException e){
            throw new DataBindException("the return type of onItemLongClick only support null and boolean");
        }
    }

}
