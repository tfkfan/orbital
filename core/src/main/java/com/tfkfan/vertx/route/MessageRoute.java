package com.tfkfan.vertx.route;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MessageRoute {
    int value() default -1;
}
