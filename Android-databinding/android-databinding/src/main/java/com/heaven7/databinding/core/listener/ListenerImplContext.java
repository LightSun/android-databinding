package com.heaven7.databinding.core.listener;

import com.heaven7.databinding.core.DataBindException;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * the event callback context. all listenerimpl must have empty constructor.
 * Created by heaven7 on 2015/11/20.
 */
public abstract class ListenerImplContext {

    static final String DEFAULT_EXCEPTION_INFO = "may be the params type mismatch or count of params is wrong.";

    private static final boolean sDebug = false;

    protected Method mMethod;
    protected Object mHolder;
    protected Object[] mParams;

    public ListenerImplContext(){}

    public final void set(Method method, Object holder, Object[] params){
        this.mMethod = method;
        this.mHolder = holder;
        this.mParams = params;
    }

    /*
     * callback is just call once . don't cache any data  or else may cause memory leak.
     * so after call {@link #invokeCallback()},reset() this.
     */
    // problem : reset will cause NullPointerException because we cache this
   /* protected void reset(){
        set(null,null,null);
    }*/

    /**
     * invoke the callback based on mMethod,mHolder,mParams
     */
    protected  Object invokeCallback(){
        return invokeCallback(mParams,DEFAULT_EXCEPTION_INFO);
    }

    /**
     * invoke the callback based on mMethod,mHolder,mParams
     */
    protected  Object invokeCallback(Object[] params){
        return invokeCallback(params, DEFAULT_EXCEPTION_INFO);
    }

    /**
     * invoke the callback based on mMethod,mHolder and target params.
     * @param params the params to invoke the method
     * @param exceptionNotice  the notice message if invoke called an exception
     */
    protected  Object invokeCallback(Object[] params,String exceptionNotice){
        try {
            if(sDebug) {
               System.out.print("[ invokeCallback() ]: holder = " + mHolder);
               System.out.println(" ,params = " + Arrays.toString(params));
            }
            return mMethod.invoke(mHolder,params);
        } catch (Exception e) {
            if (exceptionNotice == null)
                throw new DataBindException(e);
            else {
                throw new DataBindException(exceptionNotice, e);
            }
        }finally {
            afterCallback();
        }
    }

    /**  this is called at last  */
    protected void afterCallback() {

    }


}
