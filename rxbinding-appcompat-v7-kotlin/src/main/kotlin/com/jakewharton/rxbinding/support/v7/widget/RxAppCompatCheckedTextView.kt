package com.jakewharton.rxbinding.support.v7.widget

import android.support.v7.widget.AppCompatCheckedTextView
import rx.functions.Action1

/**
 * An action which sets the checked property of `view` with a boolean value.
 * 
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun AppCompatCheckedTextView.check(): Action1<in Boolean> = RxAppCompatCheckedTextView.check(this)
