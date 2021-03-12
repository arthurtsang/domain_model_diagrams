package com.youramaryllis.ddd.contextMap.annotations;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Relationship
public @interface CustomerSupplier {
    String value();
    boolean upStream();
}
