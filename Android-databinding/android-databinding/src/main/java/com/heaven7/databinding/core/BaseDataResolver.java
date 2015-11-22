package com.heaven7.databinding.core;

import android.content.Context;
import android.view.View;

import com.heaven7.xml.ObjectMap;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heaven7 on 2015/8/25.
 */
/*public*/ abstract class BaseDataResolver implements IDataResolver{

    private ObjectMap<String, String> mClassnameMap;
    private ObjectMap<String, Object> mObjectMap;

    private ObjectMap<String, List<Method>> mMethodsMap; //key = mMethod name
    private ObjectMap<String, Field> mFiledMap;          //key = field name

    private List<String> mEventHandleVariables;

    private boolean mEnableReflectCache = true;
    private WeakReference<Object> mWrf_CurrentBindingView;
    private Context mAppContext;

    private IEventEvaluateCallback mEvaluateCallback;

    public BaseDataResolver() {
        mClassnameMap = new ObjectMap<>();
        mObjectMap = new ObjectMap<>();
        mMethodsMap = new ObjectMap<>();
        mFiledMap =new ObjectMap<>();
        mEventHandleVariables = new ArrayList<>();
    }
    public void clearCache(){
        mMethodsMap.clear();
        mFiledMap.clear();
    }
    public void addEventHandlerVariable(String variable){
        mEventHandleVariables.add(variable);
    }

    public void setEnableReflectCache(boolean enable){
        this.mEnableReflectCache = enable;
    }
    public boolean isEnableReflectCache(){
        return mEnableReflectCache;
    }

    public void putClassname(String alias,String classname){
        mClassnameMap.put(alias, classname);
    }
    /**the bean and event handler often  be called in this */
    public void putObject(String variable,Object obj){
        mObjectMap.put(variable, obj);
    }
    public void removeObject(String variable){
        mObjectMap.remove(variable);
    }
    public void clearObjects(){
        mObjectMap.clear();
    }

    @Override
    public Object resolveVariable(String pName) throws DataBindException {
        pName = pName.trim();

        //check android resource reference
        if(ResourceResolver.isResourceReference(pName)){
            return ResourceResolver.getResValue(mAppContext,pName);
        }

        Object val = mObjectMap.get(pName);
        if(val != null)
            return val;
        throw new DataBindException("can't resolve the variable , name = " + pName);
    }

    @Override
    public boolean isEventHandlerOfView(String variable) {
        return mEventHandleVariables.contains(variable.trim());
    }

    @Override
    public Object getCurrentBindingView() {
        return mWrf_CurrentBindingView!=null? mWrf_CurrentBindingView.get() :null;
    }

    @Override
    public IEventEvaluateCallback getEventEvaluateCallback() {
        return mEvaluateCallback;
    }

    @Override
    public void setEventEvaluateCallback(IEventEvaluateCallback callback) {
         this.mEvaluateCallback = callback;
    }

    @Override
    public void setCurrentBindingView(View view) {
        mWrf_CurrentBindingView = new WeakReference<Object>(view);
        if(mAppContext == null) {
            mAppContext = view.getContext().getApplicationContext();
        }
    }

    @Override
    public Field getField(Class<?> clazz, String fieldName) throws DataBindException {
        fieldName = fieldName.trim();

        final boolean enableReflectCache = isEnableReflectCache();
        Field f ;
        if(enableReflectCache) {
            f = mFiledMap.get(fieldName);
            if (f != null) return f;
        }
        try {
            f = clazz.getDeclaredField(fieldName.trim());
            f.setAccessible(true);
            if(enableReflectCache) {
                mFiledMap.put(fieldName, f);
            }
        }catch (Exception e){
            throw new DataBindException("can't find the field, field name = "+ fieldName , e);
        }
        return f;
    }

    @Override
    public String getClassname(String variable) {
        variable = variable.trim();
        if(variable.contains(".")){
            return variable;
        }
       String classname = mClassnameMap.get(variable);
        if(classname == null){
            throw new DataBindException("can't find the class name of "+variable);
        }
        return classname;
    }

    @Override
    public List<Method> getMethod(Class<?> clazz, String methodname) {
        methodname = methodname.trim();
        final boolean enableReflectCache = isEnableReflectCache();
        String key = null;
        List<Method> list;

        if(enableReflectCache) {
            key = generateKey(clazz, methodname);
            list = mMethodsMap.get(key);
            if (list != null)
                return list;
        }

        list = new ArrayList<Method>();

        Method[] ms = clazz.getMethods();
        for(int size = ms.length , i = size - 1; i >= 0 ; i--){
            if(ms[i].getName().equals(methodname) ){
                list.add(ms[i]);
            }
        }
        if(enableReflectCache)
              mMethodsMap.put(key , list);
        return list;
    }

    private static String  generateKey(Class<?> clazz, String methodname){
        return clazz.getName() + "_"+methodname;
    }

    @Override
    public void reset() {
        mObjectMap.clear();
        mWrf_CurrentBindingView = null;
        mEnableReflectCache = true;
        mEventHandleVariables.clear();
        mClassnameMap.clear();
    }
}
