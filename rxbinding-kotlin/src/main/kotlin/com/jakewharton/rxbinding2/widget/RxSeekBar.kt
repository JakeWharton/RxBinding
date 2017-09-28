@file:Suppress(
    names = "NOTHING_TO_INLINE"
)

package com.jakewharton.rxbinding2.widget

import android.support.annotation.CheckResult
import android.widget.SeekBar
import com.jakewharton.rxbinding2.InitialValueObservable

/**
 * Create an observable of progress value changes on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
@CheckResult
inline fun SeekBar.changes(): InitialValueObservable<Int> = RxSeekBar.changes(this)

/**
 * Create an observable of progress value changes on `view` that were made only from the
 * user.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
@CheckResult
inline fun SeekBar.userChanges(): InitialValueObservable<Int> = RxSeekBar.userChanges(this)

/**
 * Create an observable of progress value changes on `view` that were made only from the
 * system.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
@CheckResult
inline fun SeekBar.systemChanges(): InitialValueObservable<Int> = RxSeekBar.systemChanges(this)

/**
 * Create an observable of progress change events for `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
@CheckResult
inline fun SeekBar.changeEvents(): InitialValueObservable<SeekBarChangeEvent> = RxSeekBar.changeEvents(this)
