package com.jakewharton.rxbinding4

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer

abstract class InitialValueObservable<T> : Observable<T>() {
  protected abstract val initialValue: T

  override fun subscribeActual(observer: Observer<in T>) {
    subscribeListener(observer)
    observer.onNext(initialValue)
  }

  protected abstract fun subscribeListener(observer: Observer<in T>)

  fun skipInitialValue(): Observable<T> = Skipped()

  private inner class Skipped : Observable<T>() {
    override fun subscribeActual(observer: Observer<in T>) {
      subscribeListener(observer)
    }
  }
}
