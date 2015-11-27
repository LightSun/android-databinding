package com.heaven7.databinding.core;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.View;

import com.heaven7.databinding.core.listener.ListenerImplContext;

import org.heaven7.core.viewhelper.ViewHelper;
import org.heaven7.core.viewhelper.ViewHelperImpl;

import java.lang.reflect.Method;

import static com.heaven7.databinding.core.ListenerFactory.isEventProperty;


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

        if(PropertyNames.TEXT.equals(propertyName)){
            impl.setText((CharSequence) value);
        }else if(PropertyNames.TEXT_RES.equals(propertyName)){
            impl.setText(res.getText((Integer) value));
        }else if(PropertyNames.TEXT_COLOR.equals(propertyName)){
            impl.setTextColor((Integer) value);
        }else if(PropertyNames.TEXT_COLOR_RES.equals(propertyName)){
            impl.setTextColor(res.getColor((Integer) value));
        }else if(PropertyNames.TEXT_COLOR_STATE.equals(propertyName)){
            impl.setTextColor((ColorStateList) value);
        }else if(PropertyNames.TEXT_COLOR_STATE_RES.equals(propertyName)){
            impl.setTextColor(res.getColorStateList((Integer) value));
        }else if(PropertyNames.TEXT_SIZE.equals(propertyName)){
            impl.setTextSizeDp((Float) value);
        }else if(PropertyNames.TEXT_SIZE_RES.equals(propertyName)){
            impl.setTextSize(res.getDimensionPixelSize((Integer) value));
        }else if(PropertyNames.VISIBILITY.equals(propertyName)){
            impl.setVisibility((Integer) value);
        } else if(PropertyNames.ON_CLICK.equals(propertyName)){
            impl.setOnClickListener((View.OnClickListener)
                    mListenerMap.get(getEventKey(id, layoutId, propertyName)) );
        }else if(PropertyNames.ON_LONG_CLICK.equals(propertyName)){
            //helper.setVisibility(viewId, (Integer) value);
            impl.setOnLongClickListener((View.OnLongClickListener)
                    mListenerMap.get(getEventKey(id, layoutId, propertyName)));
        }else if(PropertyNames.TEXT_WATCHER.equals(propertyName)){
            impl.addTextChangedListener((TextWatcher)
                    mListenerMap.get(getEventKey(id, layoutId, propertyName)));
        }
        else if( isEventProperty(propertyName)) {
            //self property indicate is self listener
          //  View v = helper.getView(viewId);
            final Method m = findMethod(v.getClass(), propertyName);
            if(m == null){
                throw new DataBindException("can't find the appropriate method to apply the property ,"+
                        "property name = " +propertyName );
            }
            //onClickListener -> OnClickListener -> setOnClickListener
            Object listener = mListenerMap.get(getEventKey(id, layoutId, propertyName));
            try {
                m.invoke(v, listener);
            } catch (Exception e) {
                throw new DataBindException("apply property( " + propertyName +
                        " ) failed caused by set listener failed, have you register event listener ? ",e);
            }
        }else {
            //self property
           // View v = helper.getView(viewId);
            try {
                v.getClass().getDeclaredMethod(getMethodName(propertyName,"set")).invoke(v, value);
            } catch (Exception e) {
                throw new DataBindException("can't apply the value to the property , " +
                        "because can't find the method (name = "+getMethodName(propertyName,"set")+
                        " )in the class( "+ v.getClass().getName() +" ),property name = " + propertyName,e);
            }
        }
    }

    public static void apply(ViewHelper helper, int viewId, int layoutId,String propertyName,
                             Object value, SparseArray<ListenerImplContext> mListenerMap){
        apply( helper.view(viewId), helper.getView(viewId), viewId,
                layoutId, propertyName, value, mListenerMap);
        /* if(PropertyNames.TEXT.equals(propertyName)){
             helper.setText(viewId, (CharSequence) value);
         }else if(PropertyNames.TEXT_RES.equals(propertyName)){
             helper.setText(viewId, helper.getResources().getText((Integer) value));
         }else if(PropertyNames.TEXT_COLOR.equals(propertyName)){
             helper.setTextColor(viewId, (Integer) value);
         }else if(PropertyNames.TEXT_COLOR_RES.equals(propertyName)){
             helper.setTextColor(viewId, helper.getResources().getColor((Integer) value));
         }else if(PropertyNames.TEXT_COLOR_STATE.equals(propertyName)){
             helper.view(viewId).setTextColor((ColorStateList) value);
         }else if(PropertyNames.TEXT_COLOR_STATE_RES.equals(propertyName)){
             helper.view(viewId).setTextColor(helper.getResources().getColorStateList((Integer) value));
         }else if(PropertyNames.TEXT_SIZE.equals(propertyName)){
             helper.view(viewId).setTextSizeDp((Float) value);
         }else if(PropertyNames.TEXT_SIZE_RES.equals(propertyName)){
             helper.view(viewId).setTextSize(helper.getResources().getDimensionPixelSize((Integer) value));
         }else if(PropertyNames.VISIBILITY.equals(propertyName)){
             helper.setVisibility(viewId, (Integer) value);
         } else if(PropertyNames.ON_CLICK.equals(propertyName)){
             helper.setOnClickListener(viewId,(View.OnClickListener)
                     mListenerMap.get(getEventKey(viewId, layoutId, propertyName)) );
         }else if(PropertyNames.ON_LONG_CLICK.equals(propertyName)){
             //helper.setVisibility(viewId, (Integer) value);
             helper.setOnLongClickListener(viewId,(View.OnLongClickListener)
                     mListenerMap.get(getEventKey(viewId, layoutId, propertyName)));
         }else if(PropertyNames.TEXT_WATCHER.equals(propertyName)){
             helper.addTextChangedListener(viewId,(TextWatcher)
                     mListenerMap.get(getEventKey(viewId, layoutId, propertyName)));
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
             Object listener = mListenerMap.get(getEventKey(viewId, layoutId, propertyName));
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
         }*/
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

    public static int getEventKey(int id, int layoutId, String propertyName) {
        return  (propertyName + "_" + id +"_"+layoutId ).hashCode();
    }

}
