package com.soybeany.bdlib.core.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 泛型工具类
 * <br>Created by Soybeany on 2017/1/26.
 */
public class GenericUtils {

    /**
     * 获得内部类实例（非静态内部类）
     */
    public static <T> T getInnerClassInstance(Object outerClass, Class<T> innerClass, Object... params) {
        Object[] newParams = new Object[params.length + 1];
        newParams[0] = outerClass;
        System.arraycopy(params, 0, newParams, 1, params.length);
        return getInstance(innerClass, newParams);
    }

    /**
     * 获得一般类实例（文件类、静态内部类）
     */
    @SuppressWarnings("unchecked")
    public static <T> T getInstance(Class<T> clazz, Object... params) {
        try {
            Class<?>[] cTypes; // 构造器参数类型
            Class[] pTypes = ReflectUtils.getTypes(params); // 参数类型
            Constructor<T> matchConstructor = null; // 匹配的构造器
            boolean hasMatch; // 是否有匹配
            for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
                cTypes = constructor.getParameterTypes();
                // 检测参数长度是否一致
                if (cTypes.length != pTypes.length) {
                    continue;
                }
                // 检测参数类型是否匹配
                hasMatch = true;
                for (int i = 0; i < cTypes.length; i++) {
                    if (!cTypes[i].isAssignableFrom(pTypes[i])) {
                        hasMatch = false;
                        break;
                    }
                }
                // 确定构造器
                if (hasMatch) {
                    matchConstructor = (Constructor<T>) constructor;
                    break;
                }
            }
            // 创建对象
            if (null != matchConstructor) {
                matchConstructor.setAccessible(true);
                return matchConstructor.newInstance(params);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得指定父类对应的泛型类
     */
    public static Class getClassGeneric(Object object, Class specifiedSuperclass) {
        return getGeneric(object, specifiedSuperclass, 0);
    }

    /**
     * 获得指定接口对应的泛型类
     */
    public static Class getInterfaceGeneric(Object object, Class specifiedSuperclass) {
        return getGeneric(object, specifiedSuperclass, 0);
    }

    /**
     * 获得泛型对应的类
     *
     * @param object         对象
     * @param specifiedClass 指定的父类/接口
     * @param index          第几个泛型
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getGeneric(Object object, Class specifiedClass, int index) {
        boolean isClass = specifiedClass.isInterface();
        Type type = isClass ?
                getSpecifiedInterfaceRec(object, specifiedClass) :
                getSpecifiedSuperclass(object, specifiedClass);
        try {
            String className = getGeneric(type)[index].toString().split(" ")[1];
            return (Class<T>) Class.forName(className);
        } catch (ArrayIndexOutOfBoundsException e) {
            String className = "(" + object.getClass().toString() + ")";
            String spClassName = "(" + specifiedClass.toString() + ")";
            String desc = null != type ? "不能使用泛型传递的方式获得类" : "没有" + (isClass ? "继承指定父类" : "实现指定接口") + spClassName;
            throw new RuntimeException(className + desc);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获得类中指定的父类
     *
     * @return null：对象没有继承指定的父类
     */
    public static Type getSpecifiedSuperclass(Object object, Class specifiedSuperclass) {
        Type genericSuperclass = object.getClass().getGenericSuperclass();
        if (genericSuperclass.toString().contains(specifiedSuperclass.getName())) {
            return genericSuperclass;
        }
        return null;
    }

    /**
     * 获得对象所实现的指定接口(不递归类的继承)
     *
     * @return null：对象没有实现指定的接口
     */
    public static Type getSpecifiedInterface(Object object, Class specifiedInterface) {
        return getSpecifiedInterface(object.getClass(), specifiedInterface);
    }

    /**
     * 获得对象所实现的指定接口(递归类的继承)
     *
     * @return null：对象没有实现指定的接口
     */
    public static Type getSpecifiedInterfaceRec(Object object, Class specifiedInterface) {
        Class clazz = object.getClass(); // 需要验证的类
        Type type;
        while (null == (type = getSpecifiedInterface(clazz, specifiedInterface))) {
            if (isType(clazz = clazz.getSuperclass(), Object.class)) {
                break;
            }
        }
        return type;
    }

    /**
     * 获得一个类型的泛型
     *
     * @return 空数组：没有对泛型赋值，或没有泛型
     */
    public static Type[] getGeneric(Type type) {
        if (type instanceof ParameterizedType) {
            return ((ParameterizedType) type).getActualTypeArguments();
        } else {
            return new Type[0];
        }
    }

    /**
     * 判断是否相同类别
     */
    public static boolean isType(Type type, Class clazz) {
        return type.toString().equals(clazz.toString());
    }

    /**
     * 获得类中指定的接口(递归继承的接口)
     */
    private static Type getSpecifiedInterface(Class clazz, Class specifiedInterface) {
        Type[] types = clazz.getGenericInterfaces();
        for (Type type : types) {
            if (type.toString().contains(specifiedInterface.getName())) {
                return type;
            } else if (type instanceof Class) {
                if (null != (type = getSpecifiedInterface((Class) type, specifiedInterface))) {
                    return type;
                }
            }
        }
        return null;
    }

}
