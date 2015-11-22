package com.heaven7.databinding.core;

import android.content.res.ColorStateList;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.View;

import com.heaven7.databinding.core.listener.ListenerImplContext;
import com.heaven7.databinding.viewhelper.ViewHelper;

import java.lang.reflect.Method;

import static com.heaven7.databinding.core.ListenerFactory.isEventProperty;


/**
 * Created by heaven7 on 2015/10/29.
 */
/*public*/ class PropertyUtil {

    public static void apply(ViewHelper helper, int viewId, String propertyName,
                             Object value, SparseArray<ListenerImplContext> mListenerMap){
       // System.out.println("apply(): value = " + value);
         if(PropertyNames.TEXT.equals(propertyName)){
             helper.setText(viewId, (CharSequence) value);
         }else if(PropertyNames.TEXT_RES.equals(propertyName)){
             helper.setText(viewId, helper.getResources().getText((Integer) value));
         }else if(PropertyNames.TEXT_COLOR.equals(propertyName)){
             helper.setTextColor(viewId, (Integer) value);
         }else if(PropertyNames.TEXT_COLOR_RES.equals(propertyName)){
             helper.setTextColor(viewId, helper.getResources().getColor((Integer) value));
         }else if(PropertyNames.TEXT_COLOR_STATE.equals(propertyName)){
             helper.setTextColor(viewId, (ColorStateList) value);
         }else if(PropertyNames.TEXT_COLOR_STATE_RES.equals(propertyName)){
             helper.setTextColor(viewId, helper.getResources().getColorStateList((Integer)value));
         }else if(PropertyNames.TEXT_SIZE.equals(propertyName)){
             helper.setTextSizeDp(viewId, (Float) value);
         }else if(PropertyNames.TEXT_SIZE_RES.equals(propertyName)){
             helper.setTextSize(viewId, helper.getResources().getDimensionPixelSize((Integer) value));
         }else if(PropertyNames.VISIBILITY.equals(propertyName)){
             helper.setVisibility(viewId, (Integer) value);
         } else if(PropertyNames.ON_CLICK.equals(propertyName)){
             helper.setOnClickListener(viewId,(View.OnClickListener)
                     mListenerMap.get(getEventKey(viewId,propertyName)) );
         }else if(PropertyNames.ON_LONG_CLICK.equals(propertyName)){
             //helper.setVisibility(viewId, (Integer) value);
             helper.setOnLongClickListener(viewId,(View.OnLongClickListener)
                     mListenerMap.get(getEventKey(viewId,propertyName)));
         }else if(PropertyNames.TEXT_WATCHER.equals(propertyName)){
             helper.addTextChangedListener(viewId,(TextWatcher)
                     mListenerMap.get(getEventKey(viewId,propertyName)));
         }
         else if( isEventProperty(propertyName)) {
             //self property indicate is self listener
             View v = helper.getView(viewId);
             final Method m = findMethod(v.getClass(), propertyName);
             if(m == null){
                 throw new DataBindException("can't find the appropriate method to apply the property ,"+
                         "property name = " +propertyName );
             }
             //onClickListener -> OnClickListener -> setOnClickListener
             Object listener = mListenerMap.get(getEventKey(viewId, propertyName));
             try {
                 m.invoke(v, listener);
             } catch (Exception e) {
                throw new DataBindException("apply property( " + propertyName +
                        " ) failed caused by set listener failed, have you register event listener ? ",e);
             }
         }else {
             //self property
             View v = helper.getView(viewId);
             try {
                 v.getClass().getDeclaredMethod(getMethodName(propertyName,"set")).invoke(v, value);
             } catch (Exception e) {
                 throw new DataBindException("can't apply the value to the property , " +
                         "because can't find the method (name = "+getMethodName(propertyName,"set")+
                         " )in the class( "+ v.getClass().getName() +" ),property name = " + propertyName,e);
             }
         }
    }
    /**  name -> Name-> setName */
    private static String getMethodName(String propertyName,String prefix){
        final char[] chars = propertyName.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return prefix + new String(chars);
    }
    private static Method findMethod(Class<? extends View> clazz, String propName){
        Method m = null;
        try {
            m = clazz.getDeclaredMethod(propName);
        } catch (NoSuchMethodException e) {
        }
        if (m == null) {
            try {
                m = clazz.getDeclaredMethod(getMethodName(propName,"set"));
            } catch (NoSuchMethodException e) {
            }
        }
        if(m == null){
            try {
                m = clazz.getDeclaredMethod(getMethodName(propName,"add"));
            } catch (NoSuchMethodException e) {
            }
        }
        return m;
    }

    public static int getEventKey(int id, String propertyName) {
        return  propertyName.concat("_").concat(id+"").hashCode();
    }

}
