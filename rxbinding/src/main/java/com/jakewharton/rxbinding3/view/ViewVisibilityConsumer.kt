@file:JvmName("RxView")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.view

import androidx.annotation.CheckResult
import android.view.View
import io.reactivex.rxjava3.functions.Consumer

/**
 * An action which sets the visibility property of `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe to free this
 * reference.
 *
 * @param visibilityWhenFalse Visibility to set on a `false` value (`View.INVISIBLE` or
 * `View.GONE`).
 */
@CheckResult
@JvmOverloads
fun View.visibility(visibilityWhenFalse: Int = View.GONE): Consumer<in Boolean> {
  require(visibilityWhenFalse != View.VISIBLE) {
    "Setting visibility to VISIBLE when false would have no effect."
  }
  require(visibilityWhenFalse == View.INVISIBLE || visibilityWhenFalse == View.GONE) {
    "Must set visibility to INVISIBLE or GONE when false."
  }
  return Consumer { value -> visibility = if (value) View.VISIBLE else visibilityWhenFalse }
}
