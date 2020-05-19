package com.jakewharton.rxbinding4.internal

import androidx.annotation.RestrictTo
import androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP

@RestrictTo(LIBRARY_GROUP)
object AlwaysTrue : () -> Boolean, (Any) -> Boolean {
  override fun invoke() = true
  override fun invoke(ignored: Any) = true
}
