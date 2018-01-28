@file:Suppress("NOTHING_TO_INLINE")

package com.jakewharton.rxbinding2.support.design.widget

import android.support.annotation.CheckResult
import android.support.design.widget.FloatingActionButton
import io.reactivex.functions.Consumer
import kotlin.Suppress

/**
 * An action which tells the `floatingActionButton`. to show on `true`
 * and hide on `false`
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
inline fun FloatingActionButton.visibility(): Consumer<in Boolean> = RxFloatingActionButton.visibility(this)
