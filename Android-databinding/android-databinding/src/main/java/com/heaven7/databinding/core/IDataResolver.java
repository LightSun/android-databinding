package com.heaven7.databinding.core;

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

    //may burden,mMethod must be public
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
         * @param holder the Method's mHolder
         * @param method  the mMethod to invokeCallback while event is occoured.
         * @param params  the mParams to transfer to mMethod
         */
        void onEvaluateCallback(Object holder,Method method,Object...params);
    }
}
