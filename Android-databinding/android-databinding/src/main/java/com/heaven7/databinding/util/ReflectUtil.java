package com.heaven7.databinding.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heaven7 on 2015/12/4.
 */
public class ReflectUtil {

    public static Field getFieldRecursiveLy(Class<?> clazz , String fieldName){
        Field f = null;
        try {
            f = clazz.getDeclaredField(fieldName);
        }catch (NoSuchFieldException e){
            Class<?> superClazz;
            while( ( superClazz = clazz.getSuperclass() ) != null && superClazz != Object.class ){
                try {
                    f = superClazz.getDeclaredField(fieldName);
                    break;
                }catch (Exception e2){
                    //ignore
                }
            }
        }
        if( f == null ){
            throw new RuntimeException("can't find the field , class = "+ clazz.getName() +
                    " , fieldName = " + fieldName);
        }
        f.setAccessible(true);
        return f;
    }

    public static List<Method> getMethods(Class<?> clazz,String methodName){
        List<Method> list = new ArrayList<>();
        Method[] ms = clazz.getMethods();
        if(ms !=null) {
            for (int size = ms.length, i = size - 1; i >= 0; i--) {
                if (ms[i].getName().equals(methodName)) {
                    list.add(ms[i]);
                }
            }
        }
        return list;
    }

    /**
     *  find the Appropriate method
     *  @throws NoSuchMethodException if not find
     */
    public static Method getAppropriateMethod(Class<?> clazz,String methodName,Class<?>...paramTypes) throws
            NoSuchMethodException{
        try {
           return clazz.getMethod(methodName, paramTypes);
        } catch (NoSuchMethodException e) {
            final List<Method> ms = getMethods(clazz, methodName);
            final int size = ms.size();
           switch (size){
               case 0:
                   throw new NoSuchMethodException("can't find the method , methodName = " + methodName +
                           " ,classname = " + clazz.getName());
               case 1:
                    return ms.get(0);
               default:
                   throw new NoSuchMethodException("You should not have multi methods with the same name of " +
                           methodName + "in class " + clazz.getName() + "( that means don't burden method )!");
           }
        }
    }

}
