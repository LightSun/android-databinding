package com.heaven7.databinding.demo.samples;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;

import com.heaven7.databinding.anno.DatabindingClass;
import com.heaven7.databinding.anno.DatabindingMethod;
import com.heaven7.databinding.core.IDataBinder;
import com.heaven7.databinding.demo.R;
import com.heaven7.databinding.demo.TestEventContext;
import com.heaven7.databinding.util.ViewUtil;

import org.heaven7.core.util.Logger;
import org.heaven7.core.util.Toaster;

/**
 * Created by heaven7 on 2015/12/1.
 */
public class TextChangeTest extends BaseActivity {
    @Override
    protected int getlayoutId() {
        return R.layout.activity_text_change;
    }

    @Override
    protected int getBindRawId() {
        return R.raw.db_test_text_change;
    }

    @Override
    protected void doBind() {
        final FocusHandler focusHandler = new FocusHandler(mDataBinder, getToaster());
        mDataBinder.bind(R.id.bt_toogle, false, focusHandler);
        mDataBinder.bind(R.id.et, false, new TextChangeListenerImpl(mDataBinder,getToaster()),
                focusHandler);
    }

    @Override
    protected void onFinalInit(Bundle savedInstanceState) {
    }

    public static class TextChangeListenerImpl extends TestEventContext {

        private static final String TAG = "TextChangeListenerImpl";

        public TextChangeListenerImpl(IDataBinder binder,Toaster toaster) {
            super(binder,toaster);
        }

        public void beforeTextChanged(View v,CharSequence s, int start, int count, int after){
            Logger.w(TAG, "beforeTextChanged", "-------------------");
        }

        public void onTextChanged(View v, CharSequence s, int start, int before, int count) {
            Logger.w(TAG,"onTextChanged" , "-------------------");
        }

        public void afterTextChanged(View v, Editable s) {
            Logger.w(TAG, "afterTextChanged", "-------------------");
        }
    }
    @DatabindingClass
    public static class FocusHandler extends TestEventContext{

        public FocusHandler(IDataBinder binder, Toaster toaster) {
            super(binder, toaster);
        }

        @DatabindingMethod
        public void onFoucusChanged(View v, boolean hasFocus){
            getToaster().show("onFoucusChanged: hasFocus = " + hasFocus);
        }

        @DatabindingMethod
        public void onClickChanageFocus(View v){
            if(v.hasFocus()){
                ViewUtil.loseFocus(v);
            }else {
                ViewUtil.obtainFocus(v);
            }
        }
    }
}
