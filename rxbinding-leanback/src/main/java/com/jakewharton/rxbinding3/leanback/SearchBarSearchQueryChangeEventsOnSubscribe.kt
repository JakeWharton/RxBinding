@file:JvmName("RxSearchBar")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.leanback

import androidx.annotation.CheckResult
import androidx.leanback.widget.SearchBar
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

import com.jakewharton.rxbinding3.internal.checkMainThread

/**
 * Create an observable of [search query events][SearchBarSearchQueryEvent] on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
fun SearchBar.searchQueryChangeEvents(): Observable<SearchBarSearchQueryEvent> {
  return SearchBarSearchQueryChangeEventsOnSubscribe(this)
}

private class SearchBarSearchQueryChangeEventsOnSubscribe(
  private val view: SearchBar
) : Observable<SearchBarSearchQueryEvent>() {

  override fun subscribeActual(observer: Observer<in SearchBarSearchQueryEvent>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, observer)
    observer.onSubscribe(listener)
    view.setSearchBarListener(listener)
  }

  private class Listener(
    private val view: SearchBar,
    private val observer: Observer<in SearchBarSearchQueryEvent>
  ) : MainThreadDisposable(), SearchBar.SearchBarListener {

    override fun onSearchQueryChange(query: String) {
      if (!isDisposed) {
        observer.onNext(SearchBarSearchQueryChangedEvent(view, query))
      }
    }

    override fun onSearchQuerySubmit(query: String) {
      if (!isDisposed) {
        observer.onNext(SearchBarSearchQuerySubmittedEvent(view, query))
      }
    }

    override fun onKeyboardDismiss(query: String) {
      if (!isDisposed) {
        observer.onNext(SearchBarSearchQueryKeyboardDismissedEvent(view, query))
      }
    }

    override fun onDispose() {
      view.setSearchBarListener(null)
    }
  }
}
