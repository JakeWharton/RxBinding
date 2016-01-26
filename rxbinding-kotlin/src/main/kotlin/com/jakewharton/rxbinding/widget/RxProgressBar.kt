package com.jakewharton.rxbinding.widget

import android.widget.ProgressBar
import rx.functions.Action1

/**
 * An action which increments the progress value of `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun ProgressBar.incrementProgressBy(): Action1<in Int> = RxProgressBar.incrementProgressBy(this)

/**
 * An action which increments the secondary progress value of `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun ProgressBar.incrementSecondaryProgressBy(): Action1<in Int> = RxProgressBar.incrementSecondaryProgressBy(this)

/**
 * An action which sets whether `view` is indeterminate.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun ProgressBar.indeterminate(): Action1<in Boolean> = RxProgressBar.indeterminate(this)

/**
 * An action which sets the max value of `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun ProgressBar.max(): Action1<in Int> = RxProgressBar.max(this)

/**
 * An action which sets the progress value of `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun ProgressBar.progress(): Action1<in Int> = RxProgressBar.progress(this)

/**
 * An action which sets the secondary progress value of `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun ProgressBar.secondaryProgress(): Action1<in Int> = RxProgressBar.secondaryProgress(this)
