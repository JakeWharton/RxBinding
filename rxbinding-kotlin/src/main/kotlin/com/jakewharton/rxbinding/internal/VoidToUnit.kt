package com.jakewharton.rxbinding.internal

import rx.functions.Func1

object VoidToUnit : Func1<Void, Unit> {
  override fun call(ignored: Void?) = Unit
}
