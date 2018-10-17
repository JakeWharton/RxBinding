@file:JvmName("RxSearchView")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.appcompat

import androidx.annotation.CheckResult
import androidx.appcompat.widget.SearchView
import com.jakewharton.rxbinding3.InitialValueObservable
import com.jakewharton.rxbinding3.internal.checkMainThread
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

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
    observer.onSubscribe(listener)
    view.setOnQueryTextListener(listener)
  }

  override val initialValue get() = view.query

  private class Listener(
    private val searchView: SearchView,
    private val observer: Observer<in CharSequence>
  ) : MainThreadDisposable(), SearchView.OnQueryTextListener {

    override fun onQueryTextChange(s: String): Boolean {
      if (!isDisposed) {
        observer.onNext(s)
        return true
      }
      return false
    }

    override fun onQueryTextSubmit(query: String): Boolean {
      return false
    }

    override fun onDispose() {
      searchView.setOnQueryTextListener(null)
    }
  }
}
