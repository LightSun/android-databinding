package com.heaven7.databinding.demo.samples;

import android.os.Bundle;
import android.view.View;

import com.heaven7.databinding.demo.R;
import com.heaven7.databinding.demo.bean.ImageInfo;
import com.heaven7.databinding.demo.test.Test;

import org.heaven7.core.adapter.AdapterManager;
import org.heaven7.core.util.Toaster;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heaven7 on 2015/12/5.
 */
public class DoubleListViewTest extends BaseActivity {
    @Override
    protected int getlayoutId() {
        return R.layout.activity_double_listview;
    }

    @Override
    protected int getBindRawId() {
        return R.raw.db_test_double_listview;
    }

    @Override
    protected void onFinalInit(Bundle savedInstanceState) {

    }

    @Override
    protected void doBind() {

        List<ImageInfo> infos = new ArrayList<>();
        for(int i=0 , size = Test.URLS.length ; i < size  ;i++){
            infos.add(new ImageInfo(Test.URLS[i],"lv1_desc_"+i));
        }
        final ItemHandler itemHandler = new ItemHandler(getToaster());
        mDataBinder.bindAdapter(R.id.lv1,infos, itemHandler);

        //bind lv2
        List<ImageInfo> infos2 = new ArrayList<>();
        for(int i=0 , size = Test.URLS.length ; i < size  ;i++){
            infos2.add(new ImageInfo(Test.URLS[i],"lv2_desc_"+i));
        }
        mDataBinder.bindAdapter(R.id.lv2,infos2, itemHandler);
    }

    public static class ItemHandler {

        private static final String TAG = "double_ItemHandler";
        private final Toaster mToaster;

        public ItemHandler(Toaster mToaster) {
            this.mToaster = mToaster;
        }

        public void onItemClick1(View v, Integer position,ImageInfo item, AdapterManager<?> am){
            mToaster.show("in lv1 --> ItemHandler_onItemClick: position = " + position + " ,item = " + item);
            if(item.isSelected()){
                am.getSelectHelper().setUnselected(position);
            }else{
                am.getSelectHelper().setSelected(position);
            }
           //just for debug :  Logger.i(TAG,"in lv1 --> view = " + v);
        }
        public void onItemClick2(View v, Integer position,ImageInfo item, AdapterManager<?> am){
            mToaster.show("in lv2 --> ItemHandler_onItemClick: position = " + position + " ,item = " + item);
            if(item.isSelected()){
                am.getSelectHelper().setUnselected(position);
            }else{
                am.getSelectHelper().setSelected(position);
            }
           // just for debug :  Logger.i(TAG,"in lv2 --> view = " + v);
        }
        public void onTextClick1(View v, Integer position,ImageInfo item, AdapterManager<?> am){
            mToaster.show("in lv1 --> on text click: position = " + position + " ,item = " + item);
        }
        public void onTextClick2(View v, Integer position,ImageInfo item, AdapterManager<?> am){
            mToaster.show("in lv2--> on text click: position = " + position + " ,item = " + item);
        }
    }
}
