package com.jakewharton.rxbinding.view;

import android.content.Context;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * The system UI visibility change event on a view.
 * <p>
 * <strong>Warning:</strong> Instances keep a strong reference to the view. Operators that
 * cache instances have the potential to leak the associated {@link Context}.
 */
public final class ViewSystemUiVisibilityChangeEvent extends ViewEvent<View> {
  @CheckResult @NonNull
  public static ViewSystemUiVisibilityChangeEvent create(@NonNull View view,
      @NonNull Integer visibility) {
    return new ViewSystemUiVisibilityChangeEvent(view, visibility);
  }

  private final Integer visibility;

  private ViewSystemUiVisibilityChangeEvent(@NonNull View view, @NonNull Integer visibility) {
    super(view);
    this.visibility = visibility;
  }

  @NonNull
  public Integer visibility() {
    return visibility;
  }

  @Override public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof ViewSystemUiVisibilityChangeEvent)) return false;
    ViewSystemUiVisibilityChangeEvent other = (ViewSystemUiVisibilityChangeEvent) o;
    return (other.view() == view())
        && (other.visibility.intValue() == visibility().intValue());
  }

  @Override public int hashCode() {
    int result = 17;
    result = result * 37 + view().hashCode();
    result = result * 37 + visibility.hashCode();
    return result;
  }

  @Override public String toString() {
    return "ViewSystemUiVisibilityChangeEvent{visibility="
        + visibility
        + '}';
  }
}
