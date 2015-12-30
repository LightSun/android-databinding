package com.heaven7.databinding.demo.samples;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.heaven7.databinding.anno.DatabindingMethod;
import com.heaven7.databinding.core.IDataBinder;
import com.heaven7.databinding.core.ListenerFactory;
import com.heaven7.databinding.core.listener.ListenerImplContext;
import com.heaven7.databinding.demo.R;
import com.heaven7.databinding.demo.TestEventContext;
import com.heaven7.databinding.demo.bean.User;

import org.heaven7.core.adapter.AdapterManager;
import org.heaven7.core.util.Logger;
import org.heaven7.core.util.Toaster;

import java.util.ArrayList;
import java.util.List;

/**
 * bind自定义事件self-event demo
 * Created by heaven7 on 2015/12/29.
 */
public class SelfEventTest extends BaseActivity {
    @Override
    protected int getlayoutId() {
        return R.layout.activity_self_event;
    }

    @Override
    protected int getBindRawId() {
        return R.raw.db_test_self_event;
    }

    @Override
    protected void onFinalInit(Bundle savedInstanceState) {
        /**
         * it has moved to framework internal. and stand property name is named 'onTouch'.
         *  but here we just override for test bind self-event.
         */
        // self event called by reflect. method name is "view.setXXX/view.addXXX/view.XXX ".
        // 'XXX'is property name. so you know
        ListenerFactory.registEventListener("onTouchListener", OnTouchListenerImpl.class);
    }

    @Override
    protected void doBind() {
        final OnTouchHandler touchHandler = new OnTouchHandler(mDataBinder, getToaster());
        List<User> users = new ArrayList<>();
        for(int i=0 , size = 15; i<size ;i++){
            users.add(new User("name_" + i , false));
        }
        mDataBinder.bind(R.id.bt, false, touchHandler )
                .bindAdapter(R.id.lv, users, touchHandler);
    }

    public static class OnTouchHandler extends TestEventContext{

        private static final String TAG = "OnTouchHandler";

        public OnTouchHandler(IDataBinder binder, Toaster toaster) {
            super(binder, toaster);
        }

        // common params of event in item  is : (View v, Integer position, User user, AdapterManager<?> am)
        // the extra param MotionEvent is comes from OnTouchListenerImpl.
        @DatabindingMethod
        public boolean onTouchOccoured(View v, MotionEvent event , Integer position, User user, AdapterManager<?> am){
            Logger.i(TAG, "onTouchOccoured", " MotionEvent = " + event);
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    return true;

            }
            return false;
        }

        public boolean onTouchSimpleView(View v, MotionEvent event){
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    getToaster().show("onTouchSimpleView ----> down event");
                    return true;

            }
            return false;
        }
    }
    /** just for test(it have moved to framework internal.) */
    public static class OnTouchListenerImpl extends ListenerImplContext implements View.OnTouchListener {
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

}
