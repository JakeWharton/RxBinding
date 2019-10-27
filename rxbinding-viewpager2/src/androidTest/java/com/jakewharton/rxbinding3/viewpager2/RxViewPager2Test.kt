package com.jakewharton.rxbinding3.viewpager2

import androidx.test.annotation.UiThreadTest
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.GeneralLocation
import androidx.test.espresso.action.GeneralSwipeAction
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Swipe
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_DRAGGING
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_IDLE
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_SETTLING
import com.jakewharton.rxbinding3.RecordingObserver
import com.jakewharton.rxbinding3.viewpager2.RxViewPager2TestActivity.Companion.PAGER_ID
import com.jakewharton.rxbinding3.viewpager2.RxViewPager2TestActivity.Companion.RAINBOW
import io.reactivex.android.schedulers.AndroidSchedulers
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RxViewPager2Test {

  @get:Rule
  val activityRule = ActivityTestRule(RxViewPager2TestActivity::class.java)

  private val instrumentation = InstrumentationRegistry.getInstrumentation()
  private lateinit var viewPager2: ViewPager2

  @Before
  fun setup() {
    val activity = activityRule.activity
    viewPager2 = activity.getViewPager2()
  }

  @Test
  fun pageScrollEvents() {
    viewPager2.currentItem = 0
    val recorder = RecordingObserver<PageScrollEvent>()

    viewPager2.pageScrollEvents()
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(recorder)

    recorder.assertNoMoreEvents()

    instrumentation.runOnMainSync { viewPager2.setCurrentItem(1, true) }
    instrumentation.waitForIdleSync()

    val event = recorder.takeNext()
    assertEquals(0, event.position)
    assertTrue(event.positionOffset > 0f)
    assertTrue(event.positionOffsetPixels > 0)
    recorder.clearEvents()

    recorder.dispose()

    instrumentation.runOnMainSync { viewPager2.setCurrentItem(0, true) }
    instrumentation.waitForIdleSync()

    recorder.assertNoMoreEvents()
  }

  @Test
  fun pageScrollStateChanges() {
    viewPager2.currentItem = 0
    val recorder = RecordingObserver<Int>()

    viewPager2.pageScrollStateChanges()
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(recorder)

    recorder.assertNoMoreEvents()

    onView(withId(PAGER_ID)).perform(swipeLeft())
    assertEquals(SCROLL_STATE_DRAGGING, recorder.takeNext())
    assertEquals(SCROLL_STATE_SETTLING, recorder.takeNext())
    assertEquals(SCROLL_STATE_IDLE, recorder.takeNext())
    recorder.assertNoMoreEvents()

    recorder.dispose()

    onView(withId(PAGER_ID)).perform(swipeLeft())
    recorder.assertNoMoreEvents()
  }

  @Test
  @UiThreadTest
  fun pageSelections() {
    viewPager2.currentItem = 0
    val recorder = RecordingObserver<Int>()

    viewPager2.pageSelections()
        .subscribe(recorder)

    assertEquals(0, recorder.takeNext())

    viewPager2.currentItem = (RAINBOW.size / 2)
    assertEquals(RAINBOW.size / 2, recorder.takeNext())

    viewPager2.currentItem = (RAINBOW.size - 1)
    assertEquals(RAINBOW.size - 1, recorder.takeNext())

    recorder.dispose()

    viewPager2.currentItem = 0
    recorder.assertNoMoreEvents()
  }

  private fun swipeLeft(): ViewAction =
    GeneralSwipeAction(
        Swipe.FAST, GeneralLocation.CENTER_RIGHT,
        GeneralLocation.CENTER_LEFT, Press.FINGER
    )
}
