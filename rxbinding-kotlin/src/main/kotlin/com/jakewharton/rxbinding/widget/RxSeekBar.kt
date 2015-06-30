package com.jakewharton.rxbinding.widget

import android.widget.SeekBar
import rx.Observable

/**
 * Create an observable of progress value changes on {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
public inline fun SeekBar.changes(): Observable<Int> = RxSeekBar.changes(this)

/**
 * Create an observable of progress change events for {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
public inline fun SeekBar.changeEvents(): Observable<SeekBarChangeEvent> = RxSeekBar.changeEvents(this)
