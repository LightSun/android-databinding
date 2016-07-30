package com.heaven7.databinding.demo.samples;

import android.os.Bundle;
import android.view.View;

import com.heaven7.core.util.Toaster;
import com.heaven7.databinding.anno.DatabindingField;
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
       // sun.misc.Unsafe unsafe = sun.misc.Unsafe.getUnsafe();
        //deprecated   mDataBinder.bind(R.id.eniv, false, new ImageParam(30f, Test.URLS[0]) );
        mDataBinder.bind(R.id.eniv2, false, new ImageParam(Test.URLS[1]),new ClickHandler(getToaster()) );
    }

    public static class ImageParam{
        float roundSize;
        @DatabindingField
        String url;
        String link;
        public ImageParam(float roundSize, String url) {
            this.roundSize = roundSize;
            this.url = url;
        }

        public ImageParam(String link) {
            this.link = link;
        }
    }
    public static class ClickHandler{

        private final Toaster mToaster;

        public ClickHandler(Toaster mToaster) {
            this.mToaster = mToaster;
        }

        public void onClickImage(View v){
             mToaster.show("---------  onClickImage  ------------");
        }
    }

}
