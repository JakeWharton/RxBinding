package com.jakewharton.rxbinding4.material

import android.util.Log
import android.view.MotionEvent
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.google.android.material.slider.Slider
import com.jakewharton.rxbinding4.MotionEventUtil
import com.jakewharton.rxbinding4.RecordingObserver
import com.jakewharton.rxbinding4.widget.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RxSliderTest {
    @Rule @JvmField
    val activityRule = ActivityTestRule(RxSliderTestActivity::class.java)
    private val instrumentation = InstrumentationRegistry.getInstrumentation()
    lateinit var slider: Slider

    @Before
    fun setUp() {
        slider = activityRule.activity.slider
    }

    @Test
    fun changes() {
        val o = RecordingObserver<Float>()
        slider.changes() //
            .subscribeOn(AndroidSchedulers.mainThread()) //
            .subscribe(o)
        assertEquals(0.0f, o.takeNext())
        instrumentation.sendPointerSync(MotionEventUtil.motionEventAtPosition(slider, MotionEvent.ACTION_DOWN, 0, 50))
        instrumentation.waitForIdleSync()
        o.assertNoMoreEvents()
        instrumentation.sendPointerSync(MotionEventUtil.motionEventAtPosition(slider, MotionEvent.ACTION_MOVE, 100, 50))
        instrumentation.waitForIdleSync()
        assertEquals(1.0f, o.takeNext())
        instrumentation.sendPointerSync(MotionEventUtil.motionEventAtPosition(slider, MotionEvent.ACTION_MOVE, 0, 50))
        instrumentation.waitForIdleSync()
        assertEquals(0.0f, o.takeNext())
        instrumentation.runOnMainSync { slider.value = 0.85f }
        instrumentation.waitForIdleSync()
        assertEquals(0.85f, o.takeNext())
        o.dispose()
        instrumentation.sendPointerSync(MotionEventUtil.motionEventAtPosition(slider, MotionEvent.ACTION_MOVE, 100, 50))
        instrumentation.waitForIdleSync()
        o.assertNoMoreEvents()
        instrumentation.runOnMainSync { slider.value = 0.85f }
        instrumentation.waitForIdleSync()
        o.assertNoMoreEvents()
    }

    @Test
    fun systemChanges() {
        val o = RecordingObserver<Float>()
        slider.systemChanges() //
            .subscribeOn(AndroidSchedulers.mainThread()) //
            .subscribe(o)
        assertEquals(0.0f, o.takeNext())
        instrumentation.sendPointerSync(MotionEventUtil.motionEventAtPosition(slider, MotionEvent.ACTION_MOVE, 100, 50))
        instrumentation.waitForIdleSync()
        o.assertNoMoreEvents()
        instrumentation.runOnMainSync { slider.value = 0.85f }
        instrumentation.waitForIdleSync()
        assertEquals(0.85f, o.takeNext())
        o.dispose()
        instrumentation.sendPointerSync(MotionEventUtil.motionEventAtPosition(slider, MotionEvent.ACTION_MOVE, 100, 50))
        instrumentation.waitForIdleSync()
        o.assertNoMoreEvents()
        instrumentation.runOnMainSync { slider.value = 0.85f }
        instrumentation.waitForIdleSync()
        o.assertNoMoreEvents()
    }

    @Test
    fun userChanges() {
        val o = RecordingObserver<Float>()
        slider.userChanges() //
            .subscribeOn(AndroidSchedulers.mainThread()) //
            .subscribe(o)
        assertEquals(0.0f, o.takeNext())
        instrumentation.sendPointerSync(MotionEventUtil.motionEventAtPosition(slider, MotionEvent.ACTION_DOWN, 0, 50))
        instrumentation.waitForIdleSync()
        o.assertNoMoreEvents()
        instrumentation.sendPointerSync(MotionEventUtil.motionEventAtPosition(slider, MotionEvent.ACTION_MOVE, 100, 50))
        instrumentation.waitForIdleSync()
        assertEquals(1.0f, o.takeNext())
        instrumentation.sendPointerSync(MotionEventUtil.motionEventAtPosition(slider, MotionEvent.ACTION_MOVE, 0, 50))
        instrumentation.waitForIdleSync()
        assertEquals(0.0f, o.takeNext())
        instrumentation.runOnMainSync { slider.value = 0.85f }
        instrumentation.waitForIdleSync()
        o.assertNoMoreEvents()
        o.dispose()
        instrumentation.sendPointerSync(MotionEventUtil.motionEventAtPosition(slider, MotionEvent.ACTION_MOVE, 100, 50))
        instrumentation.waitForIdleSync()
        o.assertNoMoreEvents()
        instrumentation.runOnMainSync { slider.value = 0.85f }
        instrumentation.waitForIdleSync()
        o.assertNoMoreEvents()
    }

    @Test
    fun touchEvents() {
        val o = RecordingObserver<SliderTouchEvent>()
        slider.touchEvents() //
            .subscribeOn(AndroidSchedulers.mainThread()) //
            .subscribe(o)
        instrumentation.sendPointerSync(MotionEventUtil.motionEventAtPosition(slider, MotionEvent.ACTION_DOWN, 0, 50))
        instrumentation.waitForIdleSync()
        assertEquals(SliderStartTrackingTouchEvent(slider), o.takeNext())
        instrumentation.sendPointerSync(MotionEventUtil.motionEventAtPosition(slider, MotionEvent.ACTION_MOVE, 100, 50))
        instrumentation.waitForIdleSync()
        instrumentation.sendPointerSync(MotionEventUtil.motionEventAtPosition(slider, MotionEvent.ACTION_UP, 100, 50))
        instrumentation.waitForIdleSync()
        assertEquals(SliderStopTrackingTouchEvent(slider), o.takeNext())
        instrumentation.runOnMainSync { slider.value = 0f }
        o.dispose()
        instrumentation.sendPointerSync(MotionEventUtil.motionEventAtPosition(slider, MotionEvent.ACTION_DOWN, 0, 50))
        instrumentation.waitForIdleSync()
        o.assertNoMoreEvents()
    }
}
