package com.jakewharton.rxbinding2.widget

import android.widget.CheckedTextView
import io.reactivex.functions.Consumer

/**
 * A consumer which sets the checked property of `view` with a boolean value.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
inline fun CheckedTextView.check(): Consumer<in Boolean> = RxCheckedTextView.check(this)
