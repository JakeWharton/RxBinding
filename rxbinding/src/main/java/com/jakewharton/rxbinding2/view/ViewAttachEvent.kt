package com.jakewharton.rxbinding2.view

import android.content.Context
import android.view.View

/**
 * A view attach event on a view.
 *
 * **Warning:** Instances keep a strong reference to the view. Operators that
 * cache instances have the potential to leak the associated [Context].
 */
sealed class ViewAttachEvent {
  /** The view from which this event occurred.  */
  abstract val view: View
}

/**
 * A view attached event on a view.
 *
 * **Warning:** Instances keep a strong reference to the view. Operators that cache
 * instances have the potential to leak the associated [Context].
 */
data class ViewAttachAttachedEvent(
  override val view: View
) : ViewAttachEvent()

/**
 * A view detached event on a view.
 *
 * **Warning:** Instances keep a strong reference to the view. Operators that cache
 * instances have the potential to leak the associated [Context].
 */
data class ViewAttachDetachedEvent(
  override val view: View
) : ViewAttachEvent()
