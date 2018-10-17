package com.jakewharton.rxbinding3.internal

import io.reactivex.functions.Predicate
import java.util.concurrent.Callable

object AlwaysTrue : Callable<Boolean>, Predicate<Any> {
  override fun call() = true
  override fun test(t: Any) = true
}
