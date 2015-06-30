package com.jakewharton.rxbinding.widget;

import android.content.Context;
import android.widget.CompoundButton;
import com.jakewharton.rxbinding.view.ViewEvent;

/**
 * A checked-change event on a view.
 * <p>
 * <strong>Warning:</strong> Instances keep a strong reference to the view. Operators that cache
 * instances have the potential to leak the associated {@link Context}.
 */
public final class CompoundButtonCheckedChangeEvent extends ViewEvent<CompoundButton> {
  public static CompoundButtonCheckedChangeEvent create(CompoundButton view, boolean isChecked) {
    return new CompoundButtonCheckedChangeEvent(view, isChecked);
  }

  private final boolean isChecked;

  private CompoundButtonCheckedChangeEvent(CompoundButton view, boolean isChecked) {
    super(view);
    this.isChecked = isChecked;
  }

  public boolean isChecked() {
    return isChecked;
  }

  @Override public int hashCode() {
    int result = 17;
    result = result * 37 + view().hashCode();
    result = result * 37 + (isChecked ? 1 : 0);
    return result;
  }

  @Override public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof CompoundButtonCheckedChangeEvent)) return false;
    CompoundButtonCheckedChangeEvent other = (CompoundButtonCheckedChangeEvent) o;
    return other.view() == view() && isChecked == other.isChecked;
  }

  @Override public String toString() {
    return "CompoundButtonCheckedChangeEvent{isChecked=" + isChecked + ", view=" + view() + '}';
  }
}
