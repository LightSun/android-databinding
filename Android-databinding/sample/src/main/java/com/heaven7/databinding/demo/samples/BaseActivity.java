package com.heaven7.databinding.demo.samples;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;

import com.heaven7.core.util.Toaster;
import com.heaven7.databinding.core.DataBindingFactory;
import com.heaven7.databinding.core.IDataBinder;

/**
 * Created by heaven7 on 2015/11/30.
 */
public abstract  class BaseActivity extends AppCompatActivity{

    private Toaster mToaster;
    protected IDataBinder mDataBinder;
    //protected VolleyUtil.HttpExecutor mHttpExecute;
  //  private SaveStateHelper mSaveStateHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToaster = new Toaster(this, Gravity.CENTER);
      //  mHttpExecute = new VolleyUtil.HttpExecutor();
     //   mSaveStateHelper = new SaveStateHelper(this);
        setContentView(getlayoutId());
        mDataBinder = DataBindingFactory.createDataBinder(this, getBindRawId(), false);
        onFinalInit(savedInstanceState);
        doBind();
    }

    @Override
    protected void onStop() {
        super.onStop();
     //   mHttpExecute.cancelAll();
    }

    @Override
    protected void onDestroy() {
        mDataBinder.onDestroy();
        super.onDestroy();
    }

   /* @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        mSaveStateHelper.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mSaveStateHelper.onRestoreInstanceState(savedInstanceState);
    }*/

    protected void showToast(String msg){
        mToaster.show(msg);
    }

    protected void showToast(int resID){
        mToaster.show(resID);
    }
    protected Toaster getToaster(){
        return mToaster;
    }


    protected abstract int getlayoutId();
    protected abstract int getBindRawId();

    protected abstract void onFinalInit(Bundle savedInstanceState);
    protected abstract void doBind();


}
