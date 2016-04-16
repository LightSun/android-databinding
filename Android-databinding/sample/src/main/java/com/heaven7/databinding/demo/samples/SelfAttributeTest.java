package com.heaven7.databinding.demo.samples;

import android.os.Bundle;
import android.view.View;

import com.heaven7.core.util.Toaster;
import com.heaven7.databinding.anno.DatabindingClass;
import com.heaven7.databinding.anno.DatabindingMethod;
import com.heaven7.databinding.core.IDataBinder;
import com.heaven7.databinding.demo.R;
import com.heaven7.databinding.demo.TestEventContext;
import com.heaven7.databinding.demo.bean.User;
import com.heaven7.databinding.demo.util.Util;


/**
 * self attribute test,自定义属性测试
 * eg: if your self attr is 'newText' , that means your self-view must have one method of
 * setNewText/newText/addNewText . or else exception will be throw.
 * Created by heaven7 on 2016/2/3.
 * @see com.heaven7.databinding.demo.view.SimpleView#setNewText(CharSequence)
 */
public class SelfAttributeTest extends BaseActivity {
    @Override
    protected int getlayoutId() {
        return R.layout.activity_self_attr;
    }

    @Override
    protected int getBindRawId() {
        return R.raw.db_self_attr_test;
    }

    @Override
    protected void onFinalInit(Bundle savedInstanceState) {

    }

    @Override
    protected void doBind() {
        mDataBinder.bind(R.id.simpleView, true,  new User("heaven7", true),
                new ClickHandler(mDataBinder,getToaster()) );
    }

    @DatabindingClass
    public static class ClickHandler extends TestEventContext{

        public ClickHandler(IDataBinder binder, Toaster toaster) {
            super(binder, toaster);
        }

        @DatabindingMethod
        public void onClickNewText(View v, User user){
            Util.changeUserName(user,  "google_"+System.currentTimeMillis()  );
            getDataBinder().notifyDataSetChanged("newText", R.id.simpleView);
            showToast("onClickNewText: user name is changed!");
        }

    }
}
