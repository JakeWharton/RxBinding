@file:JvmName("RxChipGroup")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.material

import androidx.annotation.CheckResult
import com.google.android.material.chip.ChipGroup
import io.reactivex.functions.Consumer

/**
 * An action which sets the checked child of [ChipGroup] with ID. Passing `-1` will clear
 * any checked view.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
fun ChipGroup.checked(): Consumer<in Int> {
  return Consumer { value ->
    if (value == -1) {
      clearCheck()
    } else {
      check(value!!)
    }
  }
}
