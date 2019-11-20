package com.jakewharton.rxbinding3.material

import android.content.Context
import android.view.ContextThemeWrapper
import androidx.test.annotation.UiThreadTest
import androidx.test.core.app.ApplicationProvider
import com.google.android.material.chip.Chip
import com.jakewharton.rxbinding3.RecordingObserver
import org.junit.Assert.assertNotNull
import org.junit.Test

class RxChipTest {
  private val rawContext: Context = ApplicationProvider.getApplicationContext()
  private val context = ContextThemeWrapper(rawContext, R.style.Theme_MaterialComponents)
  private val view = Chip(context)

  @Test @UiThreadTest fun closeIconClicks() {
    val o = RecordingObserver<Unit>()
    view.closeIconClicks().subscribe(o)
    o.assertNoMoreEvents() // No initial value.

    view.performCloseIconClick()
    assertNotNull(o.takeNext())

    view.performCloseIconClick()
    assertNotNull(o.takeNext())

    o.dispose()

    view.performCloseIconClick()
    o.assertNoMoreEvents()
  }
}
