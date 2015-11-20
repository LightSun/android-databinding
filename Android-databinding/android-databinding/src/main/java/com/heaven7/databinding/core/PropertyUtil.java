package com.heaven7.databinding.core;

import android.content.res.ColorStateList;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;

import com.heaven7.databinding.core.listener.ListenerImplContext;
import com.heaven7.databinding.core.listener.OnClickListenerImpl;
import com.heaven7.databinding.core.listener.OnLongClickListenerImpl;
import com.heaven7.databinding.viewhelper.ViewHelper;


/**
 * Created by heaven7 on 2015/10/29.
 */
/*public*/ class PropertyUtil {

    static final int FLAG_ON_CLICK         = 1;
    static final int FLAG_ON_LONG_CLICK    = 2;

    private static final SparseIntArray sRegistedListenerMap;
    static{
        sRegistedListenerMap = new SparseIntArray();
        sRegistedListenerMap.put(PropertyNames.ON_CLICK.hashCode(), FLAG_ON_CLICK);
        sRegistedListenerMap.put(PropertyNames.ON_LONG_CLICK.hashCode(), FLAG_ON_LONG_CLICK);
    }

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
         } else {
             View v = helper.getView(viewId);
             try {
                 v.getClass().getDeclaredMethod(propertyName).invoke(v,value);
             } catch (Exception e) {
                 throw new DataBindException("can't apply the value to the property , " +
                         "because the property is undefined in the class( "+
                         v.getClass().getName() +" ),property name = " + propertyName,e);
             }
         }
    }

    public static boolean isEventProperty(String propertyName){
        return propertyName!=null && sRegistedListenerMap.get(propertyName.hashCode()) != 0;
    }

    public static int getEventKey(int id, String propertyName) {
        return  propertyName.concat("_").concat(id+"").hashCode();
    }

    public static  ListenerImplContext createEventListener(String propName){
        switch (sRegistedListenerMap.get(propName.hashCode())){
            case FLAG_ON_CLICK:
                return new OnClickListenerImpl();

            case FLAG_ON_LONG_CLICK:
                return new OnLongClickListenerImpl();

            default:
               throw new IllegalStateException("the target property is not " +
                       "event property name,propertyName = " +propName);
        }
    }
}
