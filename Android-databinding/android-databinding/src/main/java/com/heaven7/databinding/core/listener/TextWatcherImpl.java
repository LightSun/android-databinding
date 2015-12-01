package com.heaven7.databinding.core.listener;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by heaven7 on 2015/11/21.
 */
public class TextWatcherImpl extends ListenerImplContext implements TextWatcher{

    public static class BeforeTextChangeImpl extends TextWatcherImpl{
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //indicate the method to be called is "xxx(View v,CharSequence s, int start, int count, int after,User...etc)"
            int paramCount = mParams.length + 4;
            Object[] ps = new Object[paramCount];
            ps[0] = mParams[0]; //view
            // stand listener param
            ps[1] = s;
            ps[2] = start;
            ps[3] = count;
            ps[4] = after;
            //extra data
            if (mParams.length > 1) {
                System.arraycopy(mParams, 1, ps, 5, mParams.length - 1);
            }
            invokeCallback(ps);
        }
    }
    public static class OnTextChangeImpl extends TextWatcherImpl{

        private Object[] mTmpParams;
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Object[] ps ;
            if( mTmpParams == null){
                ps = mTmpParams = new Object[mParams.length + 4];
            }else {
                ps = mTmpParams;
            }
            //view
            ps[0] = mParams[0];
            // stand listener param
            ps[1] = s;
            ps[2] = start;
            ps[3] = before;
            ps[4] = count;
            //extra data
            if(mParams.length > 1){
                System.arraycopy(mParams,1,ps , 5, mParams.length - 1);
            }
            invokeCallback(ps);
        }
    }
    public static class AfterTextChangeImpl extends TextWatcherImpl{
        @Override
        public void afterTextChanged(Editable s) {
            int paramCount = mParams.length + 1;
            Object[] ps = new Object[paramCount];
            ps[0] = mParams[0]; //view
            // stand listener param
            ps[1] = s;
            //extra data
            if(mParams.length > 1){
                System.arraycopy(mParams, 1, ps, 2, mParams.length - 1);
            }
            invokeCallback(ps);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
