@file:JvmName("RxSearchView")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.widget

import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import androidx.annotation.CheckResult
import com.jakewharton.rxbinding3.InitialValueObservable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.android.MainThreadDisposable

import com.jakewharton.rxbinding3.internal.checkMainThread

/**
 * Create an observable of character sequences for query text changes on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
@CheckResult
fun SearchView.queryTextChanges(): InitialValueObservable<CharSequence> {
  return SearchViewQueryTextChangesObservable(this)
}

private class SearchViewQueryTextChangesObservable(
  private val view: SearchView
) : InitialValueObservable<CharSequence>() {

  override fun subscribeListener(observer: Observer<in CharSequence>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, observer)
    view.setOnQueryTextListener(listener)
    observer.onSubscribe(listener)
  }

  override val initialValue get() = view.query

  private class Listener(
    private val view: SearchView,
    private val observer: Observer<in CharSequence>
  ) : MainThreadDisposable(), OnQueryTextListener {

    override fun onQueryTextChange(s: String): Boolean {
      if (!isDisposed) {
        observer.onNext(s)
        return true
      }
      return false
    }

    override fun onQueryTextSubmit(query: String) = false

    override fun onDispose() {
      view.setOnQueryTextListener(null)
    }
  }
}
