@file:JvmName("Functions")

package com.jakewharton.rxbinding2.internal

import io.reactivex.functions.Predicate
import java.util.concurrent.Callable

object AlwaysTrue : Callable<Boolean>, Predicate<Any> {
  override fun call() = true
  override fun test(t: Any) = true
}

@JvmField val CALLABLE_ALWAYS_TRUE: Callable<Boolean> = AlwaysTrue
@JvmField val PREDICATE_ALWAYS_TRUE: Predicate<Any> = AlwaysTrue
