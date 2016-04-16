package com.heaven7.databinding.demo;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.android.volley.extra.Corner;
import com.android.volley.extra.ExpandNetworkImageView;
import com.android.volley.extra.RoundedBitmapBuilder;
import com.heaven7.databinding.core.DataBindException;
import com.heaven7.databinding.core.DataBindParser;
import com.heaven7.databinding.core.DataBindingFactory;
import com.heaven7.databinding.core.IDataResolver;
import com.heaven7.databinding.core.xml.elements.ImagePropertyElement;

/**
 * Created by heaven7 on 2016/4/16.
 */
public class VolleyImageApplier implements DataBindingFactory.IImagePropertyApplier {
    @Override
    public void apply(ImageView view, IDataResolver dataResolver, DataBindParser.ImagePropertyBindInfo info) {
        if(! (view instanceof ExpandNetworkImageView))
            throw new RuntimeException("<imageProperty> can only apply to ExpandNetworkImageView.");

        final ExpandNetworkImageView eniv = (ExpandNetworkImageView) view;
        final RoundedBitmapBuilder builder = new RoundedBitmapBuilder();
        Object val;
        if(info.defaultExpre != null){
            val = info.defaultExpre.evaluate(dataResolver);
            if( val instanceof Integer){
                eniv.setImageResource(((Integer) val).intValue());
            }else if(val instanceof Bitmap){
                eniv.setImageBitmap((Bitmap) val);
            }else if(val instanceof Drawable){
                eniv.setImageDrawable((Drawable) val);
            }else {
                throw new RuntimeException("can't discern the expression value type ," +
                        " only support resId/bitmap/drawable?");
            }
            //the default expression can only use once , or else must cause  bug when in adapter.
            info.defaultExpre = null;
        }
        if(info.errorExpre!=null){
            builder.error((Integer) info.errorExpre.evaluate(dataResolver));
        }
        if(info.url != null){
            builder.url((String) info.url.evaluate(dataResolver));
        }
        //type
        if(info.type == ImagePropertyElement.TYPE_OVAL){
            builder.oval(true);
        }else if(info.type == ImagePropertyElement.TYPE_CIRCLE){
            builder.circle(true);
        }else if(info.type == ImagePropertyElement.TYPE_ROUND){
            //round or corners
            if (info.roundSizeExpre != null) {
                builder.cornerRadius((Float) info.roundSizeExpre.evaluate(dataResolver));
            } else if (info.cornerInfo != null) {
                //corners --> not support in RoundedDrawable.
                float floatVal;
                if (info.cornerInfo.topLeftExpre != null) {
                    floatVal = (Float) info.cornerInfo.topLeftExpre.evaluate(dataResolver);
                    builder.cornerRadius(Corner.TOP_LEFT, floatVal);
                }
                if (info.cornerInfo.topRightExpre != null) {
                    floatVal = (Float) info.cornerInfo.topRightExpre.evaluate(dataResolver);
                    builder.cornerRadius(Corner.TOP_RIGHT, floatVal);
                }
                if (info.cornerInfo.bottomLeftExpre != null) {
                    floatVal = (Float) info.cornerInfo.bottomLeftExpre.evaluate(dataResolver);
                    builder.cornerRadius(Corner.BOTTOM_LEFT, floatVal);
                }
                if (info.cornerInfo.bottomRightExpre != null) {
                    floatVal = (Float) info.cornerInfo.bottomRightExpre.evaluate(dataResolver);
                    builder.cornerRadius(Corner.BOTTOM_RIGHT, floatVal);
                }
            }
        }
        if(info.borderColorExpre!=null){
            builder.borderColor((Integer) info.borderColorExpre.evaluate(dataResolver));
        }
        if(info.borderWidthExpre !=null){
            builder.borderWidth((Float) info.borderWidthExpre.evaluate(dataResolver));
        }
        builder.into(eniv);
    }

    @Override
    public void apply(ImageView view, String url) {
        if(! (view instanceof ExpandNetworkImageView)) {
            throw new DataBindException("property name: img_url only can be " +
                    "used for ExpandNetworkImageView");
        }
        new RoundedBitmapBuilder().url(url).into((ExpandNetworkImageView) view);
    }
}
