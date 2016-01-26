package com.jakewharton.rxbinding.support.design.widget

import android.support.design.widget.SwipeDismissBehavior
import android.view.View
import rx.Observable

/**
 * Create an observable which emits the dismiss events from `view` on
 * [SwipeDismissBehavior].
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun View.dismisses(): Observable<View> = RxSwipeDismissBehavior.dismisses(this)
