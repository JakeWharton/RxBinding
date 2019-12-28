@file:JvmName("RxTabLayout")
@file:JvmMultifileClass

package com.jakewharton.rxbinding4.material

import androidx.annotation.CheckResult
import com.google.android.material.tabs.TabLayout
import io.reactivex.rxjava3.functions.Consumer

/**
 * An action which sets the selected tab of `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
fun select(view: TabLayout): Consumer<in Int> {
  return Consumer { index ->
    if (index < 0 || index >= view.tabCount) {
      throw IllegalArgumentException("No tab for index " + index!!)
    }
    view.getTabAt(index)!!.select()
  }
}
