package com.heaven7.databinding.demo.samples;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.heaven7.databinding.demo.R;
import com.heaven7.databinding.demo.bean.ImageInfo;
import com.heaven7.databinding.demo.test.Test;

import org.heaven7.core.adapter.AdapterManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heaven7 on 2015/11/30.
 */
public class RecycleViewBindAdapterTest extends BaseActivity {

    AdapterManager<ImageInfo> mAM;

    @Override
    protected int getBindRawId() {
        return R.raw.db_test_simple_recycle_view;
    }
    @Override
    protected int getlayoutId() {
        return R.layout.activity_recycle_view;
    }

    @Override
    protected void onFinalInit(Bundle savedInstanceState) {
        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
    }

    @Override
    public void doBind() {
        List<ImageInfo> infos = new ArrayList<>();
        for(int i=0 , size = Test.URLS.length ; i < size  ;i++){
            infos.add(new ImageInfo(Test.URLS[i],"desc_"+i));
        }
        mAM = mDataBinder.bindAdapter(R.id.rv, infos, new ListViewBindAdapterTest.ItemHandler(getToaster()));
    }

}
