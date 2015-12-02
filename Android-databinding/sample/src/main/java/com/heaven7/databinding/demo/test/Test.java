package com.heaven7.databinding.demo.test;

import android.graphics.Color;

import com.android.volley.extra.RoundedBitmapBuilder;
import com.heaven7.databinding.demo.R;

/**
 * Created by heaven7 on 2015/8/27.
 */
public class Test {

   public static final String [] URLS = {
            "http://img1.imgtn.bdimg.com/it/u=3351576580,2426190115&fm=21&gp=0.jpg" ,
            "http://img5.imgtn.bdimg.com/it/u=860944204,3685287977&fm=21&gp=0.jpg" ,
            "http://image.tianjimedia.com/uploadImages/2012/236/VBG8009Y8A27_1000x500.jpg",
            "http://img8.zol.com.cn/postbbs/225/a224909_s.jpg",
            "http://b.zol-img.com.cn/sjbizhi/images/6/320x510/1394523258256.jpg" ,
            "http://img1.imgtn.bdimg.com/it/u=3882844624,1404148837&fm=21&gp=0.jpg"
    };


    public static RoundedBitmapBuilder createRoundBuilder(float radius,String url){
        return new RoundedBitmapBuilder().url(url).cornerRadius(radius)
                .borderWidth(2f)
                .borderColor(Color.RED)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher);
    }

}
