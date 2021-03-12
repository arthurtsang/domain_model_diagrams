package com.youramaryllis.ddd.contextMap.annotations;

import java.lang.annotation.*;

// a big circle around
@Documented
@Target(ElementType.PACKAGE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BoundedContext {
    String value();
}
