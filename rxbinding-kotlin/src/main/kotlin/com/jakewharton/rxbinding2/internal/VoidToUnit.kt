package com.jakewharton.rxbinding2.internal

import io.reactivex.functions.Function

object VoidToUnit : Function<Any, Unit> {
  override fun apply(ignored: Any?) = Unit
}
