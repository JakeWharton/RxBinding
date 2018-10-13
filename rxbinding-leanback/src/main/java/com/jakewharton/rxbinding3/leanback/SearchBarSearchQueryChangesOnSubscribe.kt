@file:JvmName("RxSearchBar")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.leanback

import androidx.annotation.CheckResult
import androidx.leanback.widget.SearchBar
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

import com.jakewharton.rxbinding2.internal.Preconditions.checkMainThread

/**
 * Create an observable of String values for search query changes on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
fun SearchBar.searchQueryChanges(): Observable<String> {
  return SearchBarSearchQueryChangesOnSubscribe(this)
}

private class SearchBarSearchQueryChangesOnSubscribe(
  private val view: SearchBar
) : Observable<String>() {

  override fun subscribeActual(observer: Observer<in String>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, observer)
    observer.onSubscribe(listener)
    view.setSearchBarListener(listener)
  }

  private class Listener(
    private val view: SearchBar,
    private val observer: Observer<in String>
  ) : MainThreadDisposable(), SearchBar.SearchBarListener {

    override fun onSearchQueryChange(query: String) {
      if (!isDisposed) {
        observer.onNext(query)
      }
    }

    override fun onSearchQuerySubmit(query: String) {}

    override fun onKeyboardDismiss(query: String) {}

    override fun onDispose() {
      view.setSearchBarListener(null)
    }
  }
}
