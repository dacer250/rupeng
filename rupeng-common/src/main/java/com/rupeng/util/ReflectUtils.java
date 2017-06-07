package com.rupeng.util;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public class ReflectUtils {
    //获得方法实际的返回值类型
    @SuppressWarnings("all")
    public static Class getActualReturnType(Class childClass, Method method) {
        Type returnType = method.getGenericReturnType();

        //如果returnType直接就是Class，比如Xxx，直接返回
        if (returnType instanceof Class) {
            return (Class) returnType;
        }
        //如果返回值类型是类似Xxx<T>的形式，则返回Xxx的Class
        if (returnType instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) returnType;
            return (Class) pt.getRawType();
        }

        //执行到这里则说明返回值类型本身就是泛型，比如 T ，这时需要拿到T的真实类型
        //此时的returnTypeVariable.toString()的结果就是T
        TypeVariable returnTypeVariable = (TypeVariable) returnType;

        //使用getSuperclass方法获取没有特殊处理过的父类的Class，即Parent<T>，从这个Class可以获取到类型变量T
        //然后再调用getTypeParameters()方法获取到父类的所有类型变量，包括T
        TypeVariable[] parentTypeVariables = childClass.getSuperclass().getTypeParameters();

        //确定returnTypeVariable在parentTypeVariables中的索引位置，为后续处理做准备
        int index = -1;
        for (int i = 0; i < parentTypeVariables.length; i++) {
            if (returnTypeVariable.equals(parentTypeVariables[i])) {
                index = i;
                break;
            }
        }

        //使用子类的getGenericSuperclass()方法得到经过特殊处理的父类的Class，即Parent<String>，从这个Class中可以获得类型变量的真实类型String
        ParameterizedType parentClass = (ParameterizedType) childClass.getGenericSuperclass();
        Type[] actualTypes = parentClass.getActualTypeArguments();

        //拿到方法返回值类型的真实类型并返回
        Type actualReturnType = actualTypes[index];

        return (Class) actualReturnType;
    }

    //获得方法返回值类型的实际参数类型
    //比如public Map<K,V> show() ， 此方法要获得K和V的实际类型
    @SuppressWarnings("all")
    public static Class[] getActualParametricTypeOfReturnType(Class childClass, Method method) {

        Type returnType = method.getGenericReturnType();

        //如果返回值类型中没有泛型，返回null
        if (!(returnType instanceof ParameterizedType)) {
            return null;
        }
        ParameterizedType pt = (ParameterizedType) returnType;

        //拿到方法返回值类型上的所有泛型变量，比如[K,V]
        Type[] methodTypeVariables = pt.getActualTypeArguments();
        if (methodTypeVariables == null || methodTypeVariables.length == 0) {
            return null;
        }

        //拿到方法返回值类型的真实类型并返回
        Class[] actualMethodParametricTypes = new Class[methodTypeVariables.length];

        //拿到类上的泛型变量，比如[K,V]
        TypeVariable[] classTypeVariables = childClass.getSuperclass().getTypeParameters();

        //拿到类上的泛型变量的真实类型，比如[String,String]
        ParameterizedType parentClass = (ParameterizedType) childClass.getGenericSuperclass();
        Type[] actualClassParametricTypes = parentClass.getActualTypeArguments();

        for (int i = 0; i < methodTypeVariables.length; i++) {
            Type methodTypeVariable = methodTypeVariables[i];

            if (!(methodTypeVariable instanceof TypeVariable)) {
                //如果这些类型变量已经是真实类型，就放入actualMethodParametricTypes
                actualMethodParametricTypes[i] = (Class) methodTypeVariable;
            } else {
                //寻找真实类型
                for (int j = 0; j < classTypeVariables.length; j++) {
                    if (methodTypeVariable.toString().equals(classTypeVariables[j].toString())) {
                        actualMethodParametricTypes[i] = (Class) actualClassParametricTypes[j];
                        break;
                    }
                }
            }
        }

        return actualMethodParametricTypes;
    }
}
