@file:JvmName("RxSlider")
@file:JvmMultifileClass

package com.jakewharton.rxbinding4.material

import androidx.annotation.CheckResult
import com.google.android.material.slider.Slider
import com.jakewharton.rxbinding4.InitialValueObservable
import com.jakewharton.rxbinding4.internal.checkMainThread
import io.reactivex.rxjava3.android.MainThreadDisposable
import io.reactivex.rxjava3.core.Observer


/**
 * Create an observable of progress value changes on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
@CheckResult
fun Slider.changes(): InitialValueObservable<Float> {
    return SliderChangeObservable(this, null)
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
fun Slider.userChanges(): InitialValueObservable<Float> {
    return SliderChangeObservable(this, true)
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
fun Slider.systemChanges(): InitialValueObservable<Float> {
    return SliderChangeObservable(this, false)
}

private class SliderChangeObservable(
    private val view: Slider,
    private val shouldBeFromUser: Boolean?
) : InitialValueObservable<Float>() {

    override fun subscribeListener(observer: Observer<in Float>) {
        if (!checkMainThread(observer)) {
            return
        }
        val listener = Listener(view, shouldBeFromUser, observer)
        view.addOnChangeListener(listener)
        observer.onSubscribe(listener)
    }

    override val initialValue get() = view.value

    private class Listener(
        private val view: Slider,
        private val shouldBeFromUser: Boolean?,
        private val observer: Observer<in Float>
    ) : MainThreadDisposable(), Slider.OnChangeListener {

        override fun onValueChange(slider: Slider, value: Float, fromUser: Boolean) {
            if (!isDisposed && (shouldBeFromUser == null || shouldBeFromUser == fromUser)) {
                observer.onNext(value)
            }
        }

        override fun onDispose() {
            view.clearOnChangeListeners()
        }
    }
}