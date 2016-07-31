package com.heaven7.databinding.core.internal;

import android.support.v4.util.LruCache;

import com.heaven7.anno.Hide;
import com.heaven7.databinding.util.ReflectUtil;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by heaven7 on 2016/7/31.
 */
@Hide
public class ReflectPool {

    private final LruCache<String, List<Method>> mMap;

    public ReflectPool(int maxSize) {
        this.mMap = new LruCache<>(maxSize);
    }

    public List<Method> getMethods(Class<?> clazz, String methodName){
        final String key = clazz.getName()+"__"+methodName;
        List<Method> list = mMap.get(key);
        if(list == null){
            list = ReflectUtil.getMethods(clazz,methodName);
            mMap.put(key, list);
            return list;
        }
        return list;
    }

}
