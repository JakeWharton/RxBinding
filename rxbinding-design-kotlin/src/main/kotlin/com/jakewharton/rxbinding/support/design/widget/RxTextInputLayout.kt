package com.jakewharton.rxbinding.support.design.widget

import android.support.design.widget.TextInputLayout
import rx.functions.Action1

/**
 * An action which sets the hint property of `view` with character sequences.
 * 
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun TextInputLayout.hint(): Action1<in CharSequence> = RxTextInputLayout.hint(this)

/**
 * An action which sets the hint property of `view` string resource IDs.
 * 
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun TextInputLayout.hintRes(): Action1<in Int> = RxTextInputLayout.hintRes(this)
