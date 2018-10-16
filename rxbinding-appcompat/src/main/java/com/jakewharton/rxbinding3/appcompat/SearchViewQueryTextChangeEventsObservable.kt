@file:JvmName("RxSearchView")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.appcompat

import androidx.annotation.CheckResult
import androidx.appcompat.widget.SearchView
import com.jakewharton.rxbinding2.InitialValueObservable
import com.jakewharton.rxbinding2.internal.checkMainThread
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

/**
 * Create an observable of [query text events][SearchViewQueryTextEvent] on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
@CheckResult
fun SearchView.queryTextChangeEvents(): InitialValueObservable<SearchViewQueryTextEvent> {
  return SearchViewQueryTextChangeEventsObservable(this)
}

private class SearchViewQueryTextChangeEventsObservable(
  val view: SearchView
) : InitialValueObservable<SearchViewQueryTextEvent>() {

  override fun subscribeListener(observer: Observer<in SearchViewQueryTextEvent>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, observer)
    observer.onSubscribe(listener)
    view.setOnQueryTextListener(listener)
  }

  override val initialValue get() = SearchViewQueryTextEvent(view, view.query, false)

  private class Listener(
    private val view: SearchView,
    private val observer: Observer<in SearchViewQueryTextEvent>
  ) : MainThreadDisposable(), SearchView.OnQueryTextListener {

    override fun onQueryTextChange(s: String): Boolean {
      if (!isDisposed) {
        observer.onNext(SearchViewQueryTextEvent(view, s, false))
        return true
      }
      return false
    }

    override fun onQueryTextSubmit(query: String): Boolean {
      if (!isDisposed) {
        observer.onNext(SearchViewQueryTextEvent(view, view.query, true))
        return true
      }
      return false
    }

    override fun onDispose() {
      view.setOnQueryTextListener(null)
    }
  }
}
