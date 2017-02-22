package com.jakewharton.rxbinding2.view;

import android.content.Context;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.View;
import com.google.auto.value.AutoValue;

/**
 * A view attached event on a view.
 * <p>
 * <strong>Warning:</strong> Instances keep a strong reference to the view. Operators that
 * cache instances have the potential to leak the associated {@link Context}.
 */
@AutoValue
public abstract class ViewAttachAttachedEvent extends ViewAttachEvent {
  @CheckResult @NonNull
  public static ViewAttachAttachedEvent create(@NonNull View view) {
    return new AutoValue_ViewAttachAttachedEvent(view);
  }

  ViewAttachAttachedEvent() {
  }
}
