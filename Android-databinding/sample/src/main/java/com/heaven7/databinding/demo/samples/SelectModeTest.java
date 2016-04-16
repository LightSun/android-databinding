package com.heaven7.databinding.demo.samples;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.heaven7.adapter.AdapterManager;
import com.heaven7.core.util.Logger;
import com.heaven7.core.util.Toaster;
import com.heaven7.databinding.anno.DatabindingClass;
import com.heaven7.databinding.anno.DatabindingMethod;
import com.heaven7.databinding.core.IDataBinder;
import com.heaven7.databinding.demo.R;
import com.heaven7.databinding.demo.TestEventContext;
import com.heaven7.databinding.demo.bean.User;

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

        @DatabindingMethod
        public boolean onLongClickItem(View v, Integer position,User user, AdapterManager<?> am){
            //delete item with animation
            deleteItem(v,position,am);
            return true;
        }
        /**  animate to delete item */
        private void deleteItem(final View view, final int position , final AdapterManager<?> am) {
          //  final int originHeight = view.getMeasuredHeight();
            Animation.AnimationListener al = new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }
                @Override
                public void onAnimationEnd(Animation animation) {
                    //getToaster().show("item will be removed : position = " + position);
                    Logger.i("deleteItem", "item will be removed : position = " + position);
                    am.removeItem(position);
                }
                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            };
            collapse(view, al);
        }

        /** collapse animation while start delete item */
        private void collapse(final View view, Animation.AnimationListener al) {
            final int originHeight = view.getMeasuredHeight();
            Animation animation = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                        view.getLayoutParams().height = originHeight - (int) (originHeight * interpolatedTime);
                        view.requestLayout();
                }
                @Override
                public boolean willChangeBounds() {
                    return true;
                }
            };
            if (al != null) {
                animation.setAnimationListener(al);
            }
            animation.setDuration(300);
            view.startAnimation(animation);
        }
    }
}
