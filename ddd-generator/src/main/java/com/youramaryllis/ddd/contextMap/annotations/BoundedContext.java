package com.youramaryllis.ddd.contextMap.annotations;

import java.lang.annotation.*;

// a big circle around
@Documented
@Target(ElementType.PACKAGE)
@Retention(RetentionPolicy.RUNTIME)
/**
 * Bounded Context
 */
public @interface BoundedContext {
    String value();
}
