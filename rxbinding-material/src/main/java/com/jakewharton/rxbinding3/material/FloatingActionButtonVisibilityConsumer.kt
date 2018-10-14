@file:JvmName("RxFloatingActionButton")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.material

import androidx.annotation.CheckResult
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.reactivex.functions.Consumer

/**
 * An action which sets the visibility of `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
fun FloatingActionButton.visibility(): Consumer<in Boolean> {
  return Consumer { value ->
    if (value) {
      show()
    } else {
      hide()
    }
  }
}
