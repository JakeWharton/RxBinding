package com.jakewharton.rxbinding2.internal;

import android.support.annotation.RestrictTo;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;

/**
 * This is used to mark a method as having nullable return type parameters
 * for the kotlin generation task.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
@RestrictTo(LIBRARY_GROUP)
public @interface GenericTypeNullable {
}
