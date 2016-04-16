package com.heaven7.databinding.demo;

import com.heaven7.core.util.Toaster;
import com.heaven7.databinding.core.EventContext;
import com.heaven7.databinding.core.IDataBinder;


/**
 * Created by heaven7 on 2015/12/1.
 */
public class TestEventContext extends EventContext {

    private Toaster mToaster;

    public TestEventContext(IDataBinder binder,Toaster toaster) {
        super(binder);
        this.mToaster = toaster;
    }

    public Toaster getToaster(){
        return mToaster;
    }

    protected void showToast(String msg){
        mToaster.show(msg);
    }
    protected void showToast(int resID){
        mToaster.show(resID);
    }

}
