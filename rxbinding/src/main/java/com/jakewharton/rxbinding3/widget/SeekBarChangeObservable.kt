@file:JvmName("RxSeekBar")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.widget

import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.annotation.CheckResult
import com.jakewharton.rxbinding3.InitialValueObservable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.android.MainThreadDisposable

import com.jakewharton.rxbinding3.internal.checkMainThread

/**
 * Create an observable of progress value changes on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
@CheckResult
fun SeekBar.changes(): InitialValueObservable<Int> {
  return SeekBarChangeObservable(this, null)
}

/**
 * Create an observable of progress value changes on `view` that were made only from the
 * user.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
@CheckResult
fun SeekBar.userChanges(): InitialValueObservable<Int> {
  return SeekBarChangeObservable(this, true)
}

/**
 * Create an observable of progress value changes on `view` that were made only from the
 * system.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
@CheckResult
fun SeekBar.systemChanges(): InitialValueObservable<Int> {
  return SeekBarChangeObservable(this, false)
}

private class SeekBarChangeObservable(
  private val view: SeekBar,
  private val shouldBeFromUser: Boolean?
) : InitialValueObservable<Int>() {

  override fun subscribeListener(observer: Observer<in Int>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, shouldBeFromUser, observer)
    view.setOnSeekBarChangeListener(listener)
    observer.onSubscribe(listener)
  }

  override val initialValue get() = view.progress

  private class Listener(
    private val view: SeekBar,
    private val shouldBeFromUser: Boolean?,
    private val observer: Observer<in Int>
  ) : MainThreadDisposable(), OnSeekBarChangeListener {

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
      if (!isDisposed && (shouldBeFromUser == null || shouldBeFromUser == fromUser)) {
        observer.onNext(progress)
      }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {}

    override fun onStopTrackingTouch(seekBar: SeekBar) {}

    override fun onDispose() {
      view.setOnSeekBarChangeListener(null)
    }
  }
}
