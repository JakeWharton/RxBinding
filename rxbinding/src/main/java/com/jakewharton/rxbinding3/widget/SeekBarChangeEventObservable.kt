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
 * Create an observable of progress change events for `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
@CheckResult
fun SeekBar.changeEvents(): InitialValueObservable<SeekBarChangeEvent> {
  return SeekBarChangeEventObservable(this)
}

private class SeekBarChangeEventObservable(
  private val view: SeekBar
) : InitialValueObservable<SeekBarChangeEvent>() {

  override fun subscribeListener(observer: Observer<in SeekBarChangeEvent>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, observer)
    view.setOnSeekBarChangeListener(listener)
    observer.onSubscribe(listener)
  }

  override val initialValue get() = SeekBarProgressChangeEvent(view, view.progress, false)

  private class Listener(
    private val view: SeekBar,
    private val observer: Observer<in SeekBarChangeEvent>
  ) : MainThreadDisposable(), OnSeekBarChangeListener {

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
      if (!isDisposed) {
        observer.onNext(SeekBarProgressChangeEvent(seekBar, progress, fromUser))
      }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
      if (!isDisposed) {
        observer.onNext(SeekBarStartChangeEvent(seekBar))
      }
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
      if (!isDisposed) {
        observer.onNext(SeekBarStopChangeEvent(seekBar))
      }
    }

    override fun onDispose() {
      view.setOnSeekBarChangeListener(null)
    }
  }
}
