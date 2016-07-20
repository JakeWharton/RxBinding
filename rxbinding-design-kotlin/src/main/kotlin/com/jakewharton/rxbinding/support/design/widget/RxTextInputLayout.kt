package com.jakewharton.rxbinding.support.design.widget

import android.support.design.widget.TextInputLayout
import rx.functions.Action1

/**
 * An action which sets the counterEnabled property of `view` with a boolean value.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun TextInputLayout.counterEnabled(): Action1<in Boolean> = RxTextInputLayout.counterEnabled(this)

/**
 * An action which sets the counterMaxLength property of `view` with an integer value.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun TextInputLayout.counterMaxLength(): Action1<in Int> = RxTextInputLayout.counterMaxLength(this)

/**
 * An action which sets the error text of `view` with a character sequence.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun TextInputLayout.error(): Action1<in CharSequence> = RxTextInputLayout.error(this)

/**
 * An action which sets the error text of `view` with a string resource.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun TextInputLayout.errorRes(): Action1<in Int> = RxTextInputLayout.errorRes(this)

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
