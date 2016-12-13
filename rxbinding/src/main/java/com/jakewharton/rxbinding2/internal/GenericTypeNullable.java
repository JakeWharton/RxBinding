package com.jakewharton.rxbinding2.internal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This is used to mark a method as having nullable return type parameters
 * for the kotlin generation task.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface GenericTypeNullable {
}
