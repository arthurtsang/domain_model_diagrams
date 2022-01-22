package com.youramaryllis.ddd.domainModel.annotations;

import java.lang.annotation.*;

// add field ID if it's not there
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Entity {
}
