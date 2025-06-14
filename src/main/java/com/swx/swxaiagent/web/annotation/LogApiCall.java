package com.swx.swxaiagent.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * API调用日志记录注解
 * 
 * @author: 
 * @create: 2025/6/9
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogApiCall {
    
    /**
     * 应用类型
     */
    String appType() default "";
    
    /**
     * 描述信息
     */
    String description() default "";

    /**
     * 是否APIKEY调用
     */
    boolean isApiKey() default false;
} 