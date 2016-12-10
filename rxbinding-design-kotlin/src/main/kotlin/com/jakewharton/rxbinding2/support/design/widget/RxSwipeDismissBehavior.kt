package com.jakewharton.rxbinding2.support.design.widget

import android.support.design.widget.SwipeDismissBehavior
import android.view.View
import io.reactivex.Observable

/**
 * Create an observable which emits the dismiss events from `view` on
 * [SwipeDismissBehavior].
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
inline fun View.dismisses(): Observable<View> = RxSwipeDismissBehavior.dismisses(this)
