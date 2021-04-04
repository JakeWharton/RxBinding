package com.jakewharton.rxbinding4.material

import androidx.annotation.CheckResult
import com.google.android.material.slider.Slider
import com.jakewharton.rxbinding4.internal.checkMainThread
import io.reactivex.rxjava3.android.MainThreadDisposable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer

/**
 * Create an observable of touch events for `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
fun Slider.touchEvents(): Observable<SliderTouchEvent> {
    return SliderTouchEventsObservable(this)
}

private class SliderTouchEventsObservable(
    private val view: Slider
) : Observable<SliderTouchEvent>() {

    override fun subscribeActual(observer: Observer<in SliderTouchEvent>) {
        if (!checkMainThread(observer)) {
            return
        }
        val listener = Listener(view, observer)
        observer.onSubscribe(listener)
        view.addOnSliderTouchListener(listener)
    }

    private class Listener(
        private val view: Slider,
        private val observer: Observer<in SliderTouchEvent>
    ) : MainThreadDisposable(), Slider.OnSliderTouchListener {

        override fun onStartTrackingTouch(slider: Slider) {
            observer.onNext(SliderStartTrackingTouchEvent(view))
        }

        override fun onStopTrackingTouch(slider: Slider) {
            observer.onNext(SliderStopTrackingTouchEvent(view))
        }

        override fun onDispose() {
            view.clearOnSliderTouchListeners()
        }
    }
}