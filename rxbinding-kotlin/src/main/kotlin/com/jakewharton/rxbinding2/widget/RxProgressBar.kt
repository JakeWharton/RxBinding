@file:Suppress(
    names = "NOTHING_TO_INLINE"
)

package com.jakewharton.rxbinding2.widget

import android.support.annotation.CheckResult
import android.widget.ProgressBar
import io.reactivex.functions.Consumer

/**
 * An action which increments the progress value of `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
inline fun ProgressBar.incrementProgressBy(): Consumer<in Int> = RxProgressBar.incrementProgressBy(this)

/**
 * An action which increments the secondary progress value of `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
inline fun ProgressBar.incrementSecondaryProgressBy(): Consumer<in Int> = RxProgressBar.incrementSecondaryProgressBy(this)

/**
 * An action which sets whether `view` is indeterminate.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
inline fun ProgressBar.indeterminate(): Consumer<in Boolean> = RxProgressBar.indeterminate(this)

/**
 * An action which sets the max value of `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
inline fun ProgressBar.max(): Consumer<in Int> = RxProgressBar.max(this)

/**
 * An action which sets the progress value of `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
inline fun ProgressBar.progress(): Consumer<in Int> = RxProgressBar.progress(this)

/**
 * An action which sets the secondary progress value of `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
inline fun ProgressBar.secondaryProgress(): Consumer<in Int> = RxProgressBar.secondaryProgress(this)
