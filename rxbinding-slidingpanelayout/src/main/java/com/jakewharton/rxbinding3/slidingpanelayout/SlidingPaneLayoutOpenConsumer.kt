@file:JvmName("RxSlidingPaneLayout")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.slidingpanelayout

import androidx.annotation.CheckResult
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import io.reactivex.functions.Consumer

/**
 * An action which sets whether the pane of `view` is open.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
fun SlidingPaneLayout.open(): Consumer<in Boolean> {
  return Consumer { value ->
    if (value) {
      openPane()
    } else {
      closePane()
    }
  }
}
