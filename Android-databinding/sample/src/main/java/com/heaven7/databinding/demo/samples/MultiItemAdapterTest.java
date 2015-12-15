package com.heaven7.databinding.demo.samples;

import android.os.Bundle;
import android.view.View;

import com.heaven7.databinding.anno.DatabindingClass;
import com.heaven7.databinding.anno.DatabindingMethod;
import com.heaven7.databinding.demo.R;
import com.heaven7.databinding.demo.bean.ImageInfo;
import com.heaven7.databinding.demo.test.Test;

import org.heaven7.core.adapter.AdapterManager;
import org.heaven7.core.util.Toaster;

import java.util.ArrayList;
import java.util.List;

/**
 * test multi item in list view adapter
 * Created by heaven7 on 2015/12/2.
 */
public class MultiItemAdapterTest extends BaseActivity {
    @Override
    protected int getlayoutId() {
        return R.layout.activity_listview;
    }

    @Override
    protected int getBindRawId() {
        return R.raw.db_test_multi_item_listview;
    }

    @Override
    protected void onFinalInit(Bundle savedInstanceState) {

    }

    @Override
    protected void doBind() {
        List<ImageInfo> infos = new ArrayList<>();
        for(int i=0 , size = Test.URLS.length ; i < size  ;i++){
            final ImageInfo info = new ImageInfo(Test.URLS[i], "desc_" + i, "_title_" + i);
            info.setTag(i % 2 == 0 ? 1: 2 );
            infos.add(info);
        }
        mDataBinder.bindAdapter(R.id.lv, infos, new ItemHandler2(getToaster()));
    }
    @DatabindingClass
    public static class ItemHandler2 {

        private final Toaster mToaster;

        public ItemHandler2(Toaster mToaster) {
            this.mToaster = mToaster;
        }
        /** this is bind in item: item_image */
        @DatabindingMethod
        public void onItemClick(View v, Integer position,ImageInfo item, AdapterManager<?> am){
            mToaster.show("ItemHandler_onItemClick: position = " + position + " ,item = " + item);
            if(item.isSelected()){
                am.getSelectHelper().setUnselected(position);
            }else{
                am.getSelectHelper().setSelected(position);
            }
        }
        /** this is bind in item: item_image */
        @DatabindingMethod
        public void onTextClick(View v, Integer position,ImageInfo item, AdapterManager<?> am){
            mToaster.show("on text click: position = " + position + " ,item = " + item);
        }

        /** this is bind in item: item_txt */
        @DatabindingMethod
        public void onTitleClick(View v, Integer position,ImageInfo item, AdapterManager<?> am){
            mToaster.show("[ this is called on item2-> 'item_txt' ] on title click: position = " +
                    position + " ,item = " + item);
        }
    }
}
