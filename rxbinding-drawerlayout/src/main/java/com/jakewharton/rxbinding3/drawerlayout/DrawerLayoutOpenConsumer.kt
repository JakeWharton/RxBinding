@file:JvmName("RxDrawerLayout")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.drawerlayout

import androidx.annotation.CheckResult
import androidx.drawerlayout.widget.DrawerLayout
import io.reactivex.functions.Consumer

/**
 * An action which sets whether the drawer with `gravity` of `view` is open.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
fun DrawerLayout.open(gravity: Int): Consumer<in Boolean> {
  return Consumer { value ->
    if (value) {
      openDrawer(gravity)
    } else {
      closeDrawer(gravity)
    }
  }
}
