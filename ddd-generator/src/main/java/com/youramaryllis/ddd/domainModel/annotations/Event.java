package com.youramaryllis.ddd.domainModel.annotations;

import org.apache.logging.log4j.util.Strings;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Events.class)
public @interface Event {
    String value();
    Class target();
    String persona() default Strings.EMPTY;
}
