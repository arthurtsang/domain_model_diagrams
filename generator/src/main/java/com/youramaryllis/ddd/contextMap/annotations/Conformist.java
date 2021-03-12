package com.youramaryllis.ddd.contextMap.annotations;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Relationship
public @interface Conformist {
    String value();
}
