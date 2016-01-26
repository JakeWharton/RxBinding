package com.jakewharton.rxbinding.widget

import android.widget.TextSwitcher
import rx.Observable
import rx.functions.Action1

/**
 * An action which sets the text property of `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun TextSwitcher.text(): Action1<in CharSequence> = RxTextSwitcher.text(this)

/**
 * An action which sets the current text property of `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun TextSwitcher.currentText(): Action1<in CharSequence> = RxTextSwitcher.currentText(this)
