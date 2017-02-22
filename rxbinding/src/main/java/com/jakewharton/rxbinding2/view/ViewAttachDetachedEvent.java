package com.jakewharton.rxbinding2.view;

import android.content.Context;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.View;
import com.google.auto.value.AutoValue;

/**
 * A view detached event on a view.
 * <p>
 * <strong>Warning:</strong> Instances keep a strong reference to the view. Operators that
 * cache instances have the potential to leak the associated {@link Context}.
 */
@AutoValue
public abstract class ViewAttachDetachedEvent extends ViewAttachEvent {
  @CheckResult @NonNull
  public static ViewAttachDetachedEvent create(@NonNull View view) {
    return new AutoValue_ViewAttachDetachedEvent(view);
  }

  ViewAttachDetachedEvent() {
  }
}
