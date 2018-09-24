@file:Suppress("NOTHING_TO_INLINE")

package com.jakewharton.rxbinding2.widget

import android.support.annotation.CheckResult
import android.widget.CheckedTextView
import io.reactivex.functions.Consumer
import kotlin.Deprecated
import kotlin.Suppress

/**
 * A consumer which sets the checked property of `view` with a boolean value.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@Deprecated("Use view::setChecked method reference.")
@CheckResult
inline fun CheckedTextView.check(): Consumer<in Boolean> = RxCheckedTextView.check(this)
