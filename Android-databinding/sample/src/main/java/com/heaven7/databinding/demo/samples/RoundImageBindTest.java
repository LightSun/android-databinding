package com.heaven7.databinding.demo.samples;

import android.os.Bundle;

import com.heaven7.databinding.demo.R;
import com.heaven7.databinding.demo.test.Test;

/**
 * later will support full config in xml
 * Created by heaven7 on 2015/12/2.
 */
public class RoundImageBindTest extends BaseActivity {

    @Override
    protected int getBindRawId() {
        return R.raw.db_round_image_test;
    }
    @Override
    protected int getlayoutId() {
        return R.layout.activity_round_image_test;
    }
    @Override
    protected void onFinalInit(Bundle savedInstanceState) {
    }
    @Override
    protected void doBind() {
        mDataBinder.bind(R.id.eniv,false,new ImageParam(30f, Test.URLS[0]) );
    }

    public static class ImageParam{
        float roundSize;
        String url;
        public ImageParam(float roundSize, String url) {
            this.roundSize = roundSize;
            this.url = url;
        }
    }

}
