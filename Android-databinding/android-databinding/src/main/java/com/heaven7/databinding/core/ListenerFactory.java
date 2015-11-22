package com.heaven7.databinding.core;

import android.util.SparseArray;

import com.heaven7.databinding.core.listener.ListenerImplContext;
import com.heaven7.databinding.core.listener.OnClickListenerImpl;
import com.heaven7.databinding.core.listener.OnLongClickListenerImpl;
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
        registEventListener(PropertyNames.TEXT_WATCHER  , TextWatcherImpl.class);
    }

    public static void registEventListener(String propertyName,String classname){
       sRegistedListenerMap.put(propertyName.hashCode(),classname);
    }
    public static void registEventListener(String propertyName,Class<?> clazz){
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
