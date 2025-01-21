package com.tfkfan.webgame.route;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MessageRoute {
    int value() default -1;
}
