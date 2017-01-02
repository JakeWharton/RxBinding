package com.jakewharton.rxbinding2.support.design.widget

import android.support.design.widget.TextInputLayout
import io.reactivex.functions.Consumer

/**
 * An action which sets the counterEnabled property of `view` with a boolean value.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
inline fun TextInputLayout.counterEnabled(): Consumer<in Boolean> = RxTextInputLayout.counterEnabled(this)

/**
 * An action which sets the counterMaxLength property of `view` with an integer value.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
inline fun TextInputLayout.counterMaxLength(): Consumer<in Int> = RxTextInputLayout.counterMaxLength(this)

/**
 * An action which sets the error text of `view` with a character sequence.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
inline fun TextInputLayout.error(): Consumer<in CharSequence?> = RxTextInputLayout.error(this)

/**
 * An action which sets the error text of `view` with a string resource.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
inline fun TextInputLayout.errorRes(): Consumer<in Int?> = RxTextInputLayout.errorRes(this)

/**
 * An action which sets the hint property of `view` with character sequences.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
inline fun TextInputLayout.hint(): Consumer<in CharSequence> = RxTextInputLayout.hint(this)

/**
 * An action which sets the hint property of `view` string resource IDs.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
inline fun TextInputLayout.hintRes(): Consumer<in Int> = RxTextInputLayout.hintRes(this)
