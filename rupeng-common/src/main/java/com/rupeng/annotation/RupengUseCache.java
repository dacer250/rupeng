package com.rupeng.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//缓存的方法级别上的开关，标注了此注解的方法可以使用缓存
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited//支持注解被继承
public @interface RupengUseCache {

}
