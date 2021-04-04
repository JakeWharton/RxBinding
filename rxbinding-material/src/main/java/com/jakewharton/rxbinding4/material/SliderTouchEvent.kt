package com.jakewharton.rxbinding4.material

import com.google.android.material.slider.Slider

sealed class SliderTouchEvent {
    abstract val view: Slider
}

data class SliderStartTrackingTouchEvent(
    override val view: Slider
) : SliderTouchEvent()

data class SliderStopTrackingTouchEvent(
    override val view: Slider
) : SliderTouchEvent()