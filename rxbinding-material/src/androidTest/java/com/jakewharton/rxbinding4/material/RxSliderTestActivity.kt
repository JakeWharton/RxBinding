package com.jakewharton.rxbinding4.material

import android.app.Activity
import android.os.Bundle
import com.google.android.material.slider.Slider

class RxSliderTestActivity : Activity() {
    lateinit var slider: Slider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        slider = Slider(this)
        setContentView(slider)
    }
}