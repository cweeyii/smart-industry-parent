package com.cweeyii.cache.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Target(ElementType.TYPE)
@Retention(RUNTIME)
public @interface CacheEntity {
    CacheDataType value();
}
