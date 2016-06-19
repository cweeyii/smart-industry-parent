package com.cweeyii.cache.framework.annotation;


import com.cweeyii.rabbitmq.framework.vo.InternalMessageFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.FIELD)
@Retention(RUNTIME)
public @interface ItemProviderUnit {

    String dataProviderUnit();

    InternalMessageFactory.MessageType[] relatedMessageType();

}
