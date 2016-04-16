package com.heaven7.databinding.core;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;

import com.heaven7.core.util.Logger;
import com.heaven7.core.util.ViewCompatUtil;
import com.heaven7.core.util.ViewHelper;
import com.heaven7.core.util.ViewHelperImpl;
import com.heaven7.databinding.core.listener.ListenerImplContext;
import com.heaven7.databinding.util.ReflectUtil;

import java.lang.reflect.Method;

import static com.heaven7.databinding.core.ListenerFactory.isEventProperty;

//import android.support.v4.graphics.drawable.RoundedBitmapDrawable;


/**
 * Created by heaven7 on 2015/10/29.
 */
/*public*/ class PropertyUtil {

    /**
     * apply the value to the view
     * @param v  the view
     * @param id  the id of v , often is the v.hashCode().
     * @param layoutId  the layout id , often is 0, or else is the item layout id of adapter view
     * @param propertyName  the property name
     * @param value   the value to apply
     * @param mListenerMap the listener map
     */
    public static void apply(ViewHelperImpl impl ,View v,int id , int layoutId, String propertyName,
                             Object value, SparseArray<ListenerImplContext> mListenerMap){
        if(impl ==null){
            impl = new ViewHelperImpl(v);
        }
        final Resources res = impl.getContext().getResources();
        //background
        if(PropertyNames.BACKGROUND.equals(propertyName)){
            ViewCompatUtil.setBackgroundCompatible(v, (Drawable) value);
        }else if(PropertyNames.BACKGROUND_COLOR.equals(propertyName)){
            v.setBackgroundColor((Integer) value);
        }else if(PropertyNames.BACKGROUND_RES.equals(propertyName)){
            v.setBackgroundResource((Integer) value);
        }
        else if(PropertyNames.TEXT.equals(propertyName)){
            impl.setText((CharSequence) value);
        }else if(PropertyNames.TEXT_RES.equals(propertyName)){
            impl.setText(res.getText((Integer) value));
        }else if(PropertyNames.TEXT_COLOR.equals(propertyName)){
            if(value instanceof String){
                impl.setTextColor(Color.parseColor((String) value));
            }else {
                impl.setTextColor((Integer) value);
            }
        }else if(PropertyNames.TEXT_COLOR_RES.equals(propertyName)){
            impl.setTextColor(res.getColor((Integer) value));
        }else if(PropertyNames.TEXT_COLOR_STATE.equals(propertyName)){
            impl.setTextColor((ColorStateList) value);
        }else if(PropertyNames.TEXT_COLOR_STATE_RES.equals(propertyName)){
            impl.setTextColor(res.getColorStateList((Integer) value));
        }else if(PropertyNames.TEXT_SIZE.equals(propertyName)){
            impl.setTextSize((Float) value);
        }else if(PropertyNames.TEXT_SIZE_RES.equals(propertyName)){
            impl.setTextSize(res.getDimensionPixelSize((Integer) value));
        }else if(PropertyNames.VISIBILITY.equals(propertyName)){
            impl.setVisibility((Integer) value);
        } else if(PropertyNames.ON_CLICK.equals(propertyName)){
            impl.setOnClickListener((View.OnClickListener)
                    mListenerMap.get(getEventKey(id, layoutId, propertyName)));
        }else if(PropertyNames.ON_LONG_CLICK.equals(propertyName)){
            //helper.setVisibility(viewId, (Integer) value);
            impl.setOnLongClickListener((View.OnLongClickListener)
                    mListenerMap.get(getEventKey(id, layoutId, propertyName)));
        }else if(PropertyNames.TEXT_CHANGE.equals(propertyName)
                || PropertyNames.TEXT_CHANGE_BEFORE.equals(propertyName)
                || PropertyNames.TEXT_CHANGE_AFTER.equals(propertyName)){
            impl.addTextChangedListener((TextWatcher)
                    mListenerMap.get(getEventKey(id, layoutId, propertyName)));
        }else if(PropertyNames.ON_FOCUS_CHANGE.equals(propertyName)){
            v.setOnFocusChangeListener((View.OnFocusChangeListener) mListenerMap.get(
                    getEventKey(id, layoutId, propertyName)));
        }else if(PropertyNames.ON_TOUCH.equals(propertyName)){
            v.setOnTouchListener((View.OnTouchListener) mListenerMap.get(
                    getEventKey(id, layoutId, propertyName)));
        }
        //apply image
        else if(PropertyNames.IMGAE_BITMAP.equals(propertyName)){
            if(value instanceof Integer){
                //refer res
                impl.setImageResource((Integer)value);
            }else{
                impl.setImageBitmap((Bitmap) value);
            }
        } else if(PropertyNames.IMGAE_DRAWABLE.equals(propertyName)){
            if(value instanceof Integer){
                //refer res
                impl.setImageResource((Integer)value);
            }else{
                impl.setImageDrawable((Drawable) value);
            }
        } else if(PropertyNames.IMGAE_URL.equals(propertyName)){
            checkAndGetImageApplier().apply((ImageView) v, (String) value);

        } else if(PropertyNames.IMGAE_ROUND_BUILDER.equals(propertyName)){
            Logger.w("databind","apply property failed, by unsupported old image builder property now.");
        }
        else if( isEventProperty(propertyName)) {
            //self property indicate is self listener

            //onClickListener -> OnClickListener -> setOnClickListener
            Object listener = mListenerMap.get(getEventKey(id, layoutId, propertyName));
            final Method m = findMethod(v.getClass(), propertyName, listener.getClass());
            if(m == null){
                throw new DataBindException("can't find the appropriate method to apply the property ,"+
                        "property name = " +propertyName );
            }
            try {
                m.invoke(v, listener);
            } catch (Exception e) {
                throw new DataBindException("apply property( " + propertyName +
                        " ) failed caused by set listener failed, have you register event listener  by calling "+
                        "ListenerFactory.registEventListener(String propertyName,Class<?> clazz) ? ",e);
            }
        }else {
            //self property
            try {
                ReflectUtil.getAppropriateMethod(v.getClass(), getMethodName(propertyName, "set")
                        , value.getClass()) .invoke(v, value);
               // v.getClass().getDeclaredMethod(getMethodName(propertyName,"set"),value.getClass()).invoke(v, value);
            } catch (Exception e) {
                throw new DataBindException("can't apply the value to the property , " +
                        "because can't find the method (name = "+getMethodName(propertyName,"set")+
                        " )in the class( "+ v.getClass().getName() +" ),property name = " + propertyName+
                        " , if the  property is event property , you should regist it by calling " +
                        "ListenerFactory.registEventListener(String propertyName,Class<?> clazz)",e);
            }
        }
    }

    public static void apply(ViewHelper helper, int viewId, int layoutId,String propertyName,
                             Object value, SparseArray<ListenerImplContext> mListenerMap){
        apply( helper.view(viewId), helper.getView(viewId), viewId,
                layoutId, propertyName, value, mListenerMap);
    }

   /* public static void applyImageProperty(View imageView, IDataResolver dataResolver,
                             DataBindParser.ImagePropertyBindInfo info){
        if(! (imageView instanceof ExpandNetworkImageView))
            throw new RuntimeException("<imageProperty> can only apply to ExpandNetworkImageView.");

        final ExpandNetworkImageView eniv = (ExpandNetworkImageView) imageView;
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
            *//**
             * the default expression can only use once , or else must cause  bug when in adapter.
             *//*
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
*/

    public static DataBindingFactory.IImagePropertyApplier checkAndGetImageApplier(){
        DataBindingFactory.IImagePropertyApplier applier = DataBindingFactory.getImagePropertyApplier();
        if(applier == null){
            throw new NullPointerException("you must call " +
                    "DataBindingFactory#setImagePropertyApplier first.");
        }
        return applier;
    }

    /**  name -> Name-> setName */
    private static String getMethodName(String propertyName,String prefix){
        final char[] chars = propertyName.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return prefix + new String(chars);
    }
    private static Method findMethod(Class<? extends View> clazz, String propName, Class<?>...paramTypes){
        Method  m = null;
        try {
            m = ReflectUtil.getAppropriateMethod(clazz, propName, paramTypes);
        }catch (NoSuchMethodException e){
           // ignore
        }

        if (m == null) {
            try {
                m = ReflectUtil.getAppropriateMethod(clazz, getMethodName(propName, "set"), paramTypes);
            } catch (NoSuchMethodException e) {
                // ignore
            }
        }
        if(m == null){
            try {
                m = ReflectUtil.getAppropriateMethod(clazz, getMethodName(propName, "add"), paramTypes);
            } catch (NoSuchMethodException e) {
                // ignore
            }
        }
        return m;
    }

    public static int getEventKey(int id, int layoutId, String propertyName) {
        return  (propertyName + "_" + id +"_"+layoutId ).hashCode();
    }

}
