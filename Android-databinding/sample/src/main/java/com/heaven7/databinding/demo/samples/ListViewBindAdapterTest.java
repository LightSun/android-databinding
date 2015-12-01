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
 * Created by heaven7 on 2015/11/30.
 */
public class ListViewBindAdapterTest extends BaseActivity {

    AdapterManager<ImageInfo> mAM;

    @Override
    protected int getBindRawId() {
        return R.raw.db_test_simple_listview;
    }
    @Override
    protected int getlayoutId() {
        return R.layout.activity_listview;
    }

    @Override
    protected void onFinalInit(Bundle savedInstanceState) {
    }

    @Override
    public void doBind() {
        List<ImageInfo> infos = new ArrayList<>();
        for(int i=0 , size = Test.URLS.length ; i < size  ;i++){
            infos.add(new ImageInfo(Test.URLS[i],"desc_"+i));
        }
        mAM = mDataBinder.bindAdapter(R.id.lv, infos, new ItemHandler(getToaster()));
    }

    public static class ItemHandler {
        private final Toaster mToaster;
        public ItemHandler(Toaster mToaster) {
            this.mToaster = mToaster;
        }

        //every param must not be primitive
        // and in  adapter item's onClick event or onLongClick event , params must like below
        public void onItemClick(View v, Integer position,ImageInfo item, AdapterManager<?> am){
            mToaster.show("ItemHandler_onItemClick: position = " + position + " ,item = " + item);
            if(item.isSelected()){
                am.getSelectHelper().setUnselected(position);
            }else{
                am.getSelectHelper().setSelected(position);
            }
        }
        public void onTextClick(View v, Integer position,ImageInfo item, AdapterManager<?> am){
            mToaster.show("on text click: position = " + position + " ,item = " + item);
        }
    }
}
