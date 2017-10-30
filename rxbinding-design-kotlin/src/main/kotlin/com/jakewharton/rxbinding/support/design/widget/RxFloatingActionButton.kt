@file:Suppress(
    names = "NOTHING_TO_INLINE"
)

package com.jakewharton.rxbinding.support.design.widget

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
inline fun FloatingActionButton.fabVisibility(): Consumer<in Boolean> = RxFloatingActionButton.fabVisibility(this)
