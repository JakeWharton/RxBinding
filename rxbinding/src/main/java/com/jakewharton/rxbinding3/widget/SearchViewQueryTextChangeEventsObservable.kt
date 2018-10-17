@file:JvmName("RxSearchView")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.widget

import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import androidx.annotation.CheckResult
import com.jakewharton.rxbinding3.InitialValueObservable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

import com.jakewharton.rxbinding3.internal.checkMainThread

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
  private val view: SearchView
) : InitialValueObservable<SearchViewQueryTextEvent>() {

  override fun subscribeListener(observer: Observer<in SearchViewQueryTextEvent>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, observer)
    view.setOnQueryTextListener(listener)
    observer.onSubscribe(listener)
  }

  override val initialValue get() = SearchViewQueryTextEvent(view, view.query, false)

  private class Listener(
    private val view: SearchView,
    private val observer: Observer<in SearchViewQueryTextEvent>
  ) : MainThreadDisposable(), OnQueryTextListener {

    override fun onQueryTextChange(s: String): Boolean {
      if (!isDisposed) {
        observer.onNext(SearchViewQueryTextEvent(view, s, false))
        return true
      }
      return false
    }

    override fun onQueryTextSubmit(query: String): Boolean {
      if (!isDisposed) {
        observer.onNext(SearchViewQueryTextEvent(view, query, true))
        return true
      }
      return false
    }

    override fun onDispose() {
      view.setOnQueryTextListener(null)
    }
  }
}
