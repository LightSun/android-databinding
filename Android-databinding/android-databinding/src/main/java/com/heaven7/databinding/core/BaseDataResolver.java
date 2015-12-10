package com.heaven7.databinding.core;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseArray;
import android.view.View;

import com.heaven7.databinding.util.ReflectUtil;
import com.heaven7.databinding.util.ViewUtil;
import com.heaven7.xml.ObjectMap;

import org.heaven7.core.adapter.AdapterManager;
import org.heaven7.core.adapter.ISelectable;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heaven7 on 2015/8/25.
 */
/*public*/ class BaseDataResolver implements IDataResolver{

    private ObjectMap<String, String> mClassnameMap;
    /** tmp object cache will be clear after bind by call {@link #clearObjects()} */
    private ObjectMap<String, Object> mObjectMap;

    private ObjectMap<String, List<Method>> mMethodsMap; //key = clazz+mMethod name
    private ObjectMap<String, Field> mFieldMap;          //key = vlazz+field name

    private List<String> mEventHandleVariables;

    private boolean mEnableReflectCache = true;
    private WeakReference<Object> mWrf_CurrentBindingView;
    private Context mAppContext;

    private IEventEvaluateCallback mEvaluateCallback;

    // --------------------- below is used by adapter -------------------------------//
    private ObjectMap<String,Object> mLongStandingObjs;
    private int mPosition = ISelectable.INVALID_POSITION;
    private Object mItem ;

    private SparseArray<WeakReference<AdapterManager<? extends ISelectable>>> mAdapterManagerMap;

    public BaseDataResolver() {
        mClassnameMap = new ObjectMap<>(10);
        mMethodsMap = new ObjectMap<>(16);
        mFieldMap = new ObjectMap<>(16);

        mEventHandleVariables = new ArrayList<>(4);

        mObjectMap = new ObjectMap<>(6);
        mLongStandingObjs = new ObjectMap<>(3);
    }
    public void clearCache(){
        mMethodsMap.clear();
        mFieldMap.clear();
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

        //color
        if(pName.charAt(0)=='#'){
             return Color.parseColor(pName);// throws IllegalArgumentException
        }
        // 12dp or  sp
        else if(Character.isDigit(pName.charAt(0)) ){
            if( pName.endsWith("dp")) {
                return ViewUtil.getDpSize(mAppContext,
                        Float.parseFloat(pName.substring(0, pName.length() - 2)));
            }else if(pName.endsWith("sp")){
                return ViewUtil.getSpSize(mAppContext,
                        Float.parseFloat(pName.substring(0, pName.length() - 2)));
            }
        }
        else if(ResourceResolver.isResourceReference(pName)){
            //check android resource reference
            return ResourceResolver.getResValue(mAppContext,pName);
        }

        Object val = mObjectMap.get(pName);
        if(val != null)
            return val;
        val = mLongStandingObjs.get(pName);
        if(val != null)
            return val;
        throw new DataBindException("can't resolve the variable , name = " + pName +
                " , current data map = " + mObjectMap.toString());
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
    public Context getApplicationContext() {
        return mAppContext;
    }

    @Override
    public Field getField(Class<?> clazz, String fieldName) throws DataBindException {
        fieldName = fieldName.trim();

        final boolean enableReflectCache = isEnableReflectCache();
        final String key = generateKey(clazz, fieldName);
        Field f ;
        if(enableReflectCache) {
            f = mFieldMap.get(key);
            if (f != null) return f;
        }
        try {
            f = clazz.getDeclaredField(fieldName);
            f.setAccessible(true);
            if(enableReflectCache) {
                mFieldMap.put(key, f);
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

        list = ReflectUtil.getMethods(clazz,methodname);
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
        endBind();
        mLongStandingObjs.clear();
        if(mAdapterManagerMap!=null){
            mAdapterManagerMap.clear();
        }
    }

    @Override
    public AdapterManager<? extends ISelectable> getAdapterManager(int adapterHash) {
        WeakReference<AdapterManager<? extends ISelectable>> ref = mAdapterManagerMap.get(adapterHash);
        if(ref != null){
            return ref.get();
        }
        return null;
    }

    @Override
    public void putAdapterManager(int adapterHash, AdapterManager<? extends ISelectable> am) {
         if(mAdapterManagerMap == null)
             mAdapterManagerMap = new SparseArray<>(3);
        mAdapterManagerMap.put(adapterHash, new WeakReference<AdapterManager<? extends ISelectable>>(am));
    }

    @Override
    public  void beginBindItem(int position, Object item) {
        this.mPosition = position;
        this.mItem = item;
    }

    @Override
    public void endBind() {
        this.mPosition = ISelectable.INVALID_POSITION;
        this.mItem = null;
    }

    @Override
    public void putLongStandingData(String variable, Object data) {
        mLongStandingObjs.put(variable,data);
    }

    @Override
    public Object getCurrentItem(){
        return mItem;
    }
    @Override
    public int getCurrentPosition(){
        return mPosition;
    }
}
