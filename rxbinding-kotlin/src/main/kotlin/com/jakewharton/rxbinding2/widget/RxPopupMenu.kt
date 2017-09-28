@file:Suppress(
    names = "NOTHING_TO_INLINE"
)

package com.jakewharton.rxbinding2.widget

import android.support.annotation.CheckResult
import android.view.MenuItem
import android.widget.PopupMenu
import com.jakewharton.rxbinding2.internal.VoidToUnit
import io.reactivex.Observable

/**
 * Create an observable which emits the clicked item in `view`'s menu.
 *
 * *Warning:* The created observable keeps a strong reference to `view`.
 * Unsubscribe to free this reference.
 *
 * *Warning:* The created observable uses [PopupMenu.setOnMenuItemClickListener]
 * to observe dismiss change. Only one observable can be used for a view at a time.
 */
@CheckResult
inline fun PopupMenu.itemClicks(): Observable<MenuItem> = RxPopupMenu.itemClicks(this)

/**
 * Create an observable which emits on `view` dismiss events. The emitted value is
 * unspecified and should only be used as notification.
 *
 * *Warning:* The created observable keeps a strong reference to `view`.
 * Unsubscribe to free this reference.
 *
 * *Warning:* The created observable uses [PopupMenu.setOnDismissListener] to
 * observe dismiss change. Only one observable can be used for a view at a time.
 */
@CheckResult
inline fun PopupMenu.dismisses(): Observable<Unit> = RxPopupMenu.dismisses(this).map(VoidToUnit)
