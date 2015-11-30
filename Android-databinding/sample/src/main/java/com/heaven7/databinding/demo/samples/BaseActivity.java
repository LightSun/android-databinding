package com.heaven7.databinding.demo.samples;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.heaven7.databinding.core.DataBinder;

import org.heaven7.core.util.Toaster;

/**
 * Created by heaven7 on 2015/11/30.
 */
public abstract  class BaseActivity extends AppCompatActivity{

    private Toaster mToaster;
    protected DataBinder mDataBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToaster = new Toaster(this);
        setContentView(getlayoutId());
        mDataBinder = new DataBinder(this,getBindRawId());
        doBind();
        onFinalInit(savedInstanceState);
    }

    protected abstract void doBind();

    protected abstract void onFinalInit(Bundle savedInstanceState);

    protected abstract int getlayoutId();
    protected abstract int getBindRawId();

    protected void showToast(String msg){
        mToaster.show(msg);
    }

    protected void showToast(int resID){
        mToaster.show(resID);
    }
    protected Toaster getToaster(){
        return mToaster;
    }
}
