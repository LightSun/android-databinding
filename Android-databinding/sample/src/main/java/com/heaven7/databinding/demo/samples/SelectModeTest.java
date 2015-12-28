package com.heaven7.databinding.demo.samples;

import android.os.Bundle;
import android.view.View;

import com.heaven7.databinding.anno.DatabindingClass;
import com.heaven7.databinding.anno.DatabindingMethod;
import com.heaven7.databinding.core.IDataBinder;
import com.heaven7.databinding.demo.R;
import com.heaven7.databinding.demo.TestEventContext;
import com.heaven7.databinding.demo.bean.User;

import org.heaven7.core.adapter.AdapterManager;
import org.heaven7.core.util.Toaster;

import java.util.ArrayList;
import java.util.List;

/**
 * this demo show how to use select mode , single or multi mode .
 * relative class is SelectHelper .  can get from AdapterManager.getSelectHelper().
 * Created by heaven7 on 2015/12/28.
 */
public class SelectModeTest extends BaseActivity {
    @Override
    protected int getlayoutId() {
        return R.layout.activity_listview;
    }

    @Override
    protected int getBindRawId() {
        return R.raw.db_test_select_mode;
    }

    @Override
    protected void onFinalInit(Bundle savedInstanceState) {

    }

    @Override
    protected void doBind() {
        List<User> users = new ArrayList<>();
         for(int i=0 , size = 15; i<size ;i++){
             users.add(new User("name_" + i , false));
         }
        mDataBinder.bindAdapter(R.id.lv, users,new ClickHandler(mDataBinder,getToaster()));
    }

    @DatabindingClass
    public static class ClickHandler extends TestEventContext{

        public ClickHandler(IDataBinder binder, Toaster toaster) {
            super(binder, toaster);
        }
        @DatabindingMethod
        public void onClickItem(View v, Integer position,User user, AdapterManager<?> am){
              // toogle the select state of the position item, it automatic  notify data changed.
              am.getSelectHelper().toogleSelected(position);
        }
    }
}
