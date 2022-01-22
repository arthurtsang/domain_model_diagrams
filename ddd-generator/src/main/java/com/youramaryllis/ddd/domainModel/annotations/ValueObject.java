package com.youramaryllis.ddd.domainModel.annotations;

import java.lang.annotation.*;

// remove field ID if it's there
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValueObject {
}
