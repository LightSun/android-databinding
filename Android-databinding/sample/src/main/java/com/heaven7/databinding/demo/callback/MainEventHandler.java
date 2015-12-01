package com.heaven7.databinding.demo.callback;

import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.heaven7.databinding.core.EventContext;
import com.heaven7.databinding.core.IDataBinder;
import com.heaven7.databinding.core.PropertyNames;
import com.heaven7.databinding.demo.R;
import com.heaven7.databinding.demo.bean.User;
import com.heaven7.databinding.demo.util.Util;

/**
 * used for BaseBehaviourActivity
 * Created by heaven7 on 2015/11/18.
 */
public class MainEventHandler extends EventContext{

    public MainEventHandler(IDataBinder binder) {
        super(binder);
    }

    public void onClickChangeUsername(View v,User user){
        Util.changeUserName(user,"by_MainEventHandler_OnClick");

        //change male
        user.setMale(!user.isMale());
        getDataBinder().notifyDataSetChanged(R.id.bt);
    }

    public void onLongClickChangeUsername(View v,User user){
        Toast t =  Toast.makeText(v.getContext(), "------------ onLongClick ---------", Toast.LENGTH_SHORT);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.show();
        Util.changeUserName(user, "by_MainEventHandler_OnLongClick");

        getDataBinder().notifyDataSetChanged(R.id.bt, PropertyNames.TEXT);
    }
}
