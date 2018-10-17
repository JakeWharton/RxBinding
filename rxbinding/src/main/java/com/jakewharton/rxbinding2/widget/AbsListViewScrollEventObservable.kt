@file:JvmName("RxAbsListView")
@file:JvmMultifileClass

package com.jakewharton.rxbinding2.widget

import android.widget.AbsListView
import androidx.annotation.CheckResult
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

import com.jakewharton.rxbinding2.internal.checkMainThread

/**
 * Create an observable of scroll events on `absListView`.
 *
 * *Warning:* The created observable keeps a strong reference to `absListView`.
 * Unsubscribe to free this reference.
 *
 * *Warning:* The created observable uses
 * [AbsListView.setOnScrollListener] to observe scroll
 * changes. Only one observable can be used for a view at a time.
 */
@CheckResult
fun AbsListView.scrollEvents(): Observable<AbsListViewScrollEvent> {
  return AbsListViewScrollEventObservable(this)
}

data class AbsListViewScrollEvent(
  /** The view from which this event occurred.  */
  val view: AbsListView,
  val scrollState: Int,
  val firstVisibleItem: Int,
  val visibleItemCount: Int,
  val totalItemCount: Int
)

private class AbsListViewScrollEventObservable(
  private val view: AbsListView
) : Observable<AbsListViewScrollEvent>() {

  override fun subscribeActual(observer: Observer<in AbsListViewScrollEvent>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, observer)
    observer.onSubscribe(listener)
    view.setOnScrollListener(listener)
  }

  private class Listener(
    private val view: AbsListView,
    private val observer: Observer<in AbsListViewScrollEvent>
  ) : MainThreadDisposable(), AbsListView.OnScrollListener {
    private var currentScrollState = AbsListView.OnScrollListener.SCROLL_STATE_IDLE

    override fun onScrollStateChanged(absListView: AbsListView, scrollState: Int) {
      currentScrollState = scrollState
      if (!isDisposed) {
        val event = AbsListViewScrollEvent(view, scrollState, view.firstVisiblePosition,
            view.childCount, view.count)
        observer.onNext(event)
      }
    }

    override fun onScroll(
      absListView: AbsListView, firstVisibleItem: Int, visibleItemCount: Int,
      totalItemCount: Int
    ) {
      if (!isDisposed) {
        val event = AbsListViewScrollEvent(view, currentScrollState, firstVisibleItem,
            visibleItemCount, totalItemCount)
        observer.onNext(event)
      }
    }

    override fun onDispose() {
      view.setOnScrollListener(null)
    }
  }
}
