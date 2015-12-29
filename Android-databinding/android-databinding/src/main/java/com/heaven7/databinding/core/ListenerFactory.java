package com.heaven7.databinding.core;

import android.util.SparseArray;

import com.heaven7.databinding.core.listener.ListenerImplContext;
import com.heaven7.databinding.core.listener.OnClickListenerImpl;
import com.heaven7.databinding.core.listener.OnFocusChangeListenerImpl;
import com.heaven7.databinding.core.listener.OnLongClickListenerImpl;
import com.heaven7.databinding.core.listener.OnTouchListenerImpl;
import com.heaven7.databinding.core.listener.TextWatcherImpl;

/**
 * the ListenerFactory of view's listener, you can resgiter your self-listener so that it can be used by data-binding.
 * Created by heaven7 on 2015/11/21.
 */
public final class ListenerFactory {

    private static final SparseArray<String> sRegistedListenerMap;

    static{
        sRegistedListenerMap = new SparseArray<String>();

        registEventListener(PropertyNames.ON_CLICK      , OnClickListenerImpl.class);
        registEventListener(PropertyNames.ON_LONG_CLICK , OnLongClickListenerImpl.class);
        registEventListener(PropertyNames.TEXT_CHANGE  , TextWatcherImpl.OnTextChangeImpl.class);
        registEventListener(PropertyNames.TEXT_CHANGE_AFTER  , TextWatcherImpl.AfterTextChangeImpl.class);
        registEventListener(PropertyNames.TEXT_CHANGE_BEFORE, TextWatcherImpl.BeforeTextChangeImpl.class);
        registEventListener(PropertyNames.ON_FOCUS_CHANGE, OnFocusChangeListenerImpl.class);
        registEventListener(PropertyNames.ON_TOUCH, OnTouchListenerImpl.class);
    }

    /** the class clazz must have empty constructor
     * @param clazz  must extends ListenerImplContext */
    public static void registEventListener(String propertyName,Class<? extends ListenerImplContext> clazz){
       sRegistedListenerMap.put(propertyName.hashCode(),clazz.getName());
    }

    /*public*/ static ListenerImplContext createEventListener(String propName){
        String className = sRegistedListenerMap.get(propName.hashCode());
        if(className ==null){
            throw new IllegalStateException("the target property is not " +
                    "event property name,propertyName = " +propName);
        }else{
            try {
                return (ListenerImplContext) Class.forName(className).newInstance();
            } catch (ClassCastException e) {
                throw new DataBindException("the all listener must extends ListenerImplContext ");
            }catch (Exception e) {
                throw new DataBindException(e);
            }
        }
    }

    /*public*/ static boolean isEventProperty(String propertyName){
        return propertyName!=null && sRegistedListenerMap.get(propertyName.hashCode()) != null;
    }
}
