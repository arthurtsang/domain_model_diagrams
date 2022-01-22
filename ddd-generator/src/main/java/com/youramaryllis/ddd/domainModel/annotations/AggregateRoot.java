package com.youramaryllis.ddd.domainModel.annotations;

import java.lang.annotation.*;

// red sticker
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AggregateRoot {
}
