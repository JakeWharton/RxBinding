@file:JvmName("RxAppBarLayout")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.material

import androidx.annotation.CheckResult
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.jakewharton.rxbinding2.internal.checkMainThread
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

/**
 * Create an observable which emits the offset change in `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
fun AppBarLayout.offsetChanges(): Observable<Int> {
  return AppBarLayoutOffsetChangeObservable(this)
}

private class AppBarLayoutOffsetChangeObservable(
  private val view: AppBarLayout
) : Observable<Int>() {

  override fun subscribeActual(observer: Observer<in Int>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, observer)
    observer.onSubscribe(listener)
    view.addOnOffsetChangedListener(listener)
  }

  private class Listener(
    private val appBarLayout: AppBarLayout,
    private val observer: Observer<in Int>
  ) : MainThreadDisposable(), OnOffsetChangedListener {

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
      if (!isDisposed) {
        observer.onNext(verticalOffset)
      }
    }

    override fun onDispose() {
      appBarLayout.removeOnOffsetChangedListener(this)
    }
  }
}
