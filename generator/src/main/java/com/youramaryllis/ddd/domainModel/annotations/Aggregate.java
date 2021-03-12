package com.youramaryllis.ddd.domainModel.annotations;

import java.lang.annotation.*;

// yellow sticker
// arrow only points to aggregate
// aggregate contains entity and value objects, the root entity is aggregate root
// aggregate maintains invariants
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aggregate {

}
