package com.jakewharton.rxbinding.widget

import android.widget.CheckedTextView
import rx.functions.Action1

/**
 * An action which sets the checked property of `view` with a boolean value.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun CheckedTextView.check(): Action1<in Boolean> = RxCheckedTextView.check(this)
