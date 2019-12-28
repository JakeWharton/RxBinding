package com.jakewharton.rxbinding4.view

import android.view.View
import android.view.ViewGroup

sealed class ViewGroupHierarchyChangeEvent {
  /** The view from which this event occurred.  */
  abstract val view: ViewGroup

  /** The child from which this event occurred.  */
  abstract val child: View
}

/**
 * A child view add event on a [ViewGroup].
 *
 * **Warning:** Instances keep a strong reference to the view. Operators that cache
 * instances have the potential to leak the associated [android.content.Context].
 */
data class ViewGroupHierarchyChildViewAddEvent(
  override val view: ViewGroup,
  override val child: View
) : ViewGroupHierarchyChangeEvent()

/**
 * A child view remove event on a [ViewGroup].
 *
 * **Warning:** Instances keep a strong reference to the view. Operators that cache
 * instances have the potential to leak the associated [android.content.Context].
 */
data class ViewGroupHierarchyChildViewRemoveEvent(
  override val view: ViewGroup,
  override val child: View
) : ViewGroupHierarchyChangeEvent()
