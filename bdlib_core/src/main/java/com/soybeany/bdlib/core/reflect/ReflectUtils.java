package com.soybeany.bdlib.core.reflect;

import java.lang.reflect.Method;

/**
 * 反射工具类
 * <br>Created by Soybeany on 2017/4/19.
 */
public class ReflectUtils {

    /**
     * 调用方法
     *
     * @param obj      方法的调用对象
     * @param name     方法名
     * @param argTypes 方法参数的类型
     * @param args     方法参数
     * @return 方法是否调用成功
     */
    public static boolean invoke(Object obj, String name, Class[] argTypes, Object[] args) {
        try {
            Method method = getMethod(obj.getClass(), name, argTypes);
            if (null != method) {
                method.setAccessible(true);
                method.invoke(obj, args);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 调用方法
     *
     * @param obj  方法的调用对象
     * @param name 方法名
     * @param args 方法参数
     * @return 方法是否调用成功
     */
    public static boolean invoke(Object obj, String name, Object... args) {
        return invoke(obj, name, getTypes(args), args);
    }

    /**
     * 获得参数列表对应的类型列表
     */
    static Class[] getTypes(Object... params) {
        Class[] paramTypes = new Class[params.length];
        for (int i = 0; i < params.length; i++) {
            paramTypes[i] = params[i].getClass();
        }
        return paramTypes;
    }

    /**
     * 递归查找方法
     *
     * @param clazz    方法所在的类
     * @param name     方法名
     * @param argTypes 参数类型
     */
    private static <T> Method getMethod(Class<T> clazz, String name, Class... argTypes) throws Exception {
        Method method;
        try {
            method = clazz.getDeclaredMethod(name, argTypes);
        } catch (NoSuchMethodException e) {
            try {
                method = clazz.getMethod(name, argTypes);
            } catch (NoSuchMethodException ex) {
                Class superClass = clazz.getSuperclass();
                if (null == superClass) {
                    return null;
                }
                method = getMethod(superClass, name, argTypes);
            }
        }
        return method;
    }

}
