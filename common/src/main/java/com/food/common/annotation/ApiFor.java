package com.food.common.annotation;

import com.food.common.user.enumeration.Role;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiFor {
    Role[] roles() default {Role.ALL};
}
