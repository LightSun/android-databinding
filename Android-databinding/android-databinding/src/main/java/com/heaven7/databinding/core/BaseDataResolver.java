package com.heaven7.databinding.core;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseArray;
import android.view.View;

import com.heaven7.adapter.AdapterManager;
import com.heaven7.adapter.ISelectable;
import com.heaven7.anno.Hide;
import com.heaven7.databinding.core.internal.ReflectPool;
import com.heaven7.databinding.util.ReflectUtil;
import com.heaven7.databinding.util.ViewUtil;
import com.heaven7.xml.ObjectMap;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heaven7 on 2015/12/20.
 */
@Hide
public class BaseDataResolver implements IDataResolver {

    private final ReflectPool mReflectPool;

    private IEventEvaluateCallback mEvaluateCallback;
    private WeakReference<Object> mWrf_CurrentBindingView;
    private Context mAppContext;

    /** key-value is alias-classname */
    private ObjectMap<String, String> mClassnameMap;
    /** event variables */
    private List<String> mEventHandleVars;
    /** tmp object cache will be clear after bind by call {@link #clearObjects()} */
    private ObjectMap<String, Object> mObjectMap;

    // --------------------- below is used by adapter -------------------------------//
    private ObjectMap<String,Object> mLongStandingObjs;
    private int mPosition = ISelectable.INVALID_POSITION;
    private Object mItem ;

    private SparseArray<WeakReference<AdapterManager<? extends ISelectable>>> mAdapterManagerMap;
    //here use WeakSparseArray must cause NullPointerException . why?

    public BaseDataResolver() {
        mReflectPool = new ReflectPool(DataBindingConfig.sMaxReflectMethodSize);

        mEventHandleVars = new ArrayList<>(4);
        mClassnameMap = new ObjectMap<>(10);
        mObjectMap = new ObjectMap<>(6);

        mLongStandingObjs = new ObjectMap<>(3);
    }

    @Override
    public void putLongStandingData(String variable , Object data) {
        mLongStandingObjs.put(variable , data);
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
    public void removeAdapterManager(int adapterHash) {
        mAdapterManagerMap.remove(adapterHash);
    }

    @Override
    public  void beginBindItem(int position, Object item) {
        this.mPosition = position;
        this.mItem = item;
    }

    @Override
    public Object getCurrentItem(){
        return mItem;
    }
    @Override
    public int getCurrentPosition(){
        return mPosition;
    }

    @Override
    public void endBind() {
        this.mPosition = ISelectable.INVALID_POSITION;
        this.mItem = null;
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
            //check android resource reference , eg: '@color/red'
            return ResourceResolver.getResValue(mAppContext, pName);
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

    /**the bean and event handler often  be called in this */
    @Override
    public void putObject(String variable, Object obj){
        mObjectMap.put(variable, obj);
    }
    @Override
    public void removeObject(String variable){
        mObjectMap.remove(variable);
    }
    @Override
    public void clearObjects(){
        mObjectMap.clear();
    }
    @Override
    public void addEventHandlerVariable(String variable){
        mEventHandleVars.add(variable);
    }
    @Override
    public boolean isEventHandlerOfView(String variable) {
        return mEventHandleVars.contains(variable.trim());
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
    public void putClassname(String alias, String classname){
        mClassnameMap.put(alias, classname);
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
    public Field getField(Class<?> clazz, String fieldName){
        return  ReflectUtil.getFieldRecursiveLy(clazz, fieldName.trim());
    }
    @Override
    public List<Method> getMethod(Class<?> clazz, String methodname){
        return mReflectPool.getMethods(clazz, methodname.trim());
    }

    @Override
    public void reset() {
        mObjectMap.clear();
        mWrf_CurrentBindingView = null;
        mEventHandleVars.clear();
        mClassnameMap.clear();
        endBind();
        mLongStandingObjs.clear();
        if(mAdapterManagerMap!=null){
            mAdapterManagerMap.clear();
        }
    }

    //================================= static methods =========================================//

}
