package com.jakewharton.rxbinding.widget

import android.widget.SeekBar
import rx.Observable

/**
 * Create an observable of progress value changes on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
public inline fun SeekBar.changes(): Observable<Int> = RxSeekBar.changes(this)

/**
 * Create an observable of progress value changes on `view` that were made only from the
 * user.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
public inline fun SeekBar.userChanges(): Observable<Int> = RxSeekBar.userChanges(this)

/**
 * Create an observable of progress value changes on `view` that were made only from the
 * system.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
public inline fun SeekBar.systemChanges(): Observable<Int> = RxSeekBar.systemChanges(this)

/**
 * Create an observable of progress change events for `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
public inline fun SeekBar.changeEvents(): Observable<SeekBarChangeEvent> = RxSeekBar.changeEvents(this)
