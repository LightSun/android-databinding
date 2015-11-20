package com.heaven7.databinding.core;

import android.view.View;

import com.heaven7.databinding.util.IResetable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by heaven7 on 2015/8/12.
 */
public interface IDataResolver extends IVariableResolver,IResetable {

	/** field can be private */
    Field getField(Class<?> clazz, String fieldName) throws DataBindException;

    /**@param variable  may be static classname */
    String getClassname(String variable) throws DataBindException;

    //may burden,method must be public
    List<Method> getMethod(Class<?> clazz, String methodname) throws DataBindException;

   // IDataBinder getDataBinder();

    /** set the evaluate callback , this is used for onClick or etc.*/
    void setEventEvaluateCallback(IEventEvaluateCallback callback);

    IEventEvaluateCallback getEventEvaluateCallback();

    /**
     * the event evaluate callback of view
     */
    interface IEventEvaluateCallback{
        /**
         * @param holder the Method's holder
         * @param method  the method to invoke while event is occoured.
         * @param v    the event on which view
         * @param params  the params to transfer to method
         */
        void onEvaluateCallback(Object holder,Method method,View v,Object...params);
    }
}
