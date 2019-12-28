@file:JvmName("RxRadioGroup")
@file:JvmMultifileClass

package com.jakewharton.rxbinding4.widget

import androidx.annotation.CheckResult
import android.widget.RadioGroup
import io.reactivex.rxjava3.functions.Consumer

/**
 * An action which sets the checked child of `view` with ID. Passing `-1` will clear
 * any checked view.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
fun RadioGroup.checked(): Consumer<in Int> {
  return Consumer { value ->
    if (value == -1) {
      clearCheck()
    } else {
      check(value!!)
    }
  }
}
