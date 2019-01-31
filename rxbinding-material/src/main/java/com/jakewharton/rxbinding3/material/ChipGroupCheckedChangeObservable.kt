@file:JvmName("RxChipGroup")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.material

import androidx.annotation.CheckResult
import com.google.android.material.chip.ChipGroup
import com.jakewharton.rxbinding3.InitialValueObservable
import com.jakewharton.rxbinding3.internal.checkMainThread
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable
import io.reactivex.disposables.Disposables

/**
 * Create an observable of the checked view ID changes in [ChipGroup] with
 * [ChipGroup.isSingleSelection] set to true.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Warning:* The created observable uses [ChipGroup.setOnCheckedChangeListener]
 * to observe checked changes. Only one observable can be used for a view at a time.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
@CheckResult
fun ChipGroup.checkedChanges(): InitialValueObservable<Int> {
  return ChipGroupCheckedChangeObservable(this)
}

private class ChipGroupCheckedChangeObservable(
  private val view: ChipGroup
) : InitialValueObservable<Int>() {

  override fun subscribeListener(observer: Observer<in Int>) {
    if (!checkMainThread(observer) || !checkSingleSelection(view, observer)) {
      return
    }
    val listener = Listener(view, observer)
    view.setOnCheckedChangeListener(listener)
    observer.onSubscribe(listener)
  }

  override val initialValue get() = view.checkedChipId

  private class Listener(
    private val view: ChipGroup,
    private val observer: Observer<in Int>
  ) : MainThreadDisposable(), ChipGroup.OnCheckedChangeListener {
    private var lastChecked = -1

    override fun onCheckedChanged(chipGroup: ChipGroup, checkedId: Int) {
      if (!isDisposed && checkedId != lastChecked) {
        lastChecked = checkedId
        observer.onNext(checkedId)
      }
    }

    override fun onDispose() {
      view.setOnCheckedChangeListener(null)
    }
  }
}

private fun checkSingleSelection(view: ChipGroup, observer: Observer<*>): Boolean {
  if (!view.isSingleSelection) {
    observer.onSubscribe(Disposables.empty())
    observer.onError(IllegalStateException("The view is not in single selection mode!"))
    return false
  }
  return true
}
