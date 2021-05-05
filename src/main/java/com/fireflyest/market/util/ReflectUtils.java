package com.fireflyest.market.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 反射
 * @author Fireflyest
 */
public class ReflectUtils {

    private ReflectUtils(){
    }

    /**
     * 反射执行Get
     * @param obj 反射对象
     * @param field Get变量
     * @return 执行得到的值
     */
    public static Object invokeGet(Object obj, String field){
        Class<?> clazz = obj.getClass();
        try {
            String name = field.substring(0,1).toUpperCase() + field.substring(1).toLowerCase();
            Method method = getMethod(clazz, "get" + name);
            if (method == null) method = getMethod(clazz, "is" + name);
            return method == null ? null : method.invoke(obj);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 反射执行Set
     * @param obj 反射对象
     * @param field Set变量
     * @param value 设置的值
     */
    public static void invokeSet(Object obj, String field, Object value){
        Class<?> clazz = obj.getClass();
        String name = field.substring(0,1).toUpperCase() + field.substring(1).toLowerCase();
        Method method = getMethod(clazz, "set" + name, getBaseClass(value.getClass()));
        try {
            if (method != null) method.invoke(obj, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static Method getMethod(Class<?> clazz, String name, Class<?>... cs){
        if (clazz.getSuperclass() != null && !clazz.getSuperclass().getTypeName().equals("java.lang.Object")){
            Method method = getMethod(clazz.getSuperclass(), name, cs);
            if (method != null) return method;
        }
        try {
            return clazz.getMethod(name, cs);
        } catch (NoSuchMethodException ignored) {
        }
        return null;
    }

    private static Class<?> getBaseClass(Class<?> clazz){
        switch (clazz.getSimpleName()){
            case "Integer" :
                return int.class;
            case "Long":
                return long.class;
            case "Double":
                return double.class;
            case "Boolean":
                return boolean.class;
            case "Short":
                return short.class;
            case "Float":
                return float.class;
            default:
                return clazz;
        }
    }

}
