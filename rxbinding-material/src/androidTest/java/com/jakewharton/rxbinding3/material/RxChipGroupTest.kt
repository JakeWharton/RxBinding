package com.jakewharton.rxbinding3.material

import android.view.ContextThemeWrapper
import androidx.test.InstrumentationRegistry
import androidx.test.annotation.UiThreadTest
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.jakewharton.rxbinding3.RecordingObserver
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@SuppressWarnings("ResourceType")  // Don't need real IDs for test case.
class RxChipGroupTest {
  private val rawContext = InstrumentationRegistry.getContext()
  private val context = ContextThemeWrapper(rawContext, R.style.Theme_MaterialComponents)
  private val view = ChipGroup(context)

  @Before fun setUp() {
    val chip1 = Chip(context)
    chip1.id = 1
    view.addView(chip1)
    val chip2 = Chip(context)
    chip2.id = 2
    view.addView(chip2)
  }

  @Test @UiThreadTest fun checkedChangesNotInSingleSelectionModeNotifies() {
    view.isSingleSelection = false

    val o = RecordingObserver<Int>()
    view.checkedChanges().subscribe(o)

    val e = o.takeError()
    assertTrue(e is IllegalStateException)
    assertTrue(e.message == "The view is not in single selection mode!")
  }

  @Test @UiThreadTest fun checkedChanges() {
    view.isSingleSelection = true

    val o = RecordingObserver<Int>()
    view.checkedChanges().subscribe(o)
    assertEquals(-1, o.takeNext())

    view.check(1)
    assertEquals(1, o.takeNext())

    view.clearCheck()
    assertEquals(-1, o.takeNext())

    view.check(2)
    assertEquals(2, o.takeNext())

    o.dispose()

    view.check(1)
    o.assertNoMoreEvents()
  }

  @Test @UiThreadTest fun checked() {
    view.isSingleSelection = true // as `checkedChipId` always returns -1 otherwise

    val action = view.checked()
    assertEquals(-1, view.checkedChipId)
    action.accept(1)
    assertEquals(1, view.checkedChipId)
    action.accept(-1)
    assertEquals(-1, view.checkedChipId)
    action.accept(2)
    assertEquals(2, view.checkedChipId)
  }
}
