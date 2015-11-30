package com.heaven7.databinding.util;

import android.view.View;

import org.heaven7.core.adapter.AdapterManager;

/**
 * Created by heaven7 on 2015/11/6.
 */
public class ArrayUtil {

    public static boolean contains(String [] cons,String target){
        for(int i=0,size = cons.length ; i<size ;i++){
            if(cons[i].equals(target)){
                return true;
            }
        }
        return false;
    }
    public static boolean contains(int [] container,int target){
        for(int i=0,size = container.length ; i<size ;i++){
            if(container[i] == target){
                return true;
            }
        }
        return false;
    }

    public static Class<?>[] getTypes(Object ...objs){
        if(objs ==null || objs.length ==0)
            return null;
        Class<?>[] clss = new Class<?>[objs.length];
        for(int i=0,size = objs.length ;i<size ;i++){
            if(i==0 && objs[i] instanceof View){
                clss[i] = View.class;
            }else if(objs[i] instanceof AdapterManager){
                clss[i] = AdapterManager.class;
            } else {
                clss[i] = objs[i].getClass();
            }
        }
        return clss;
    }
}
