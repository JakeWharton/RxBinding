package com.jakewharton.rxbinding.widget;

import android.content.Context;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.widget.TextView;
import com.jakewharton.rxbinding.view.ViewEvent;

/**
 * An after text-change event on a view.
 * <p>
 * <strong>Warning:</strong> Instances keep a strong reference to the view. Operators that cache
 * instances have the potential to leak the associated {@link Context}.
 */
public final class TextViewAfterTextChangeEvent extends ViewEvent<TextView> {
  @CheckResult @NonNull
  public static TextViewAfterTextChangeEvent create(@NonNull TextView view,
      @Nullable Editable editable) {
    return new TextViewAfterTextChangeEvent(view, editable);
  }

  private final Editable editable;

  private TextViewAfterTextChangeEvent(@NonNull TextView view, @Nullable Editable editable) {
    super(view);
    this.editable = editable;
  }

  @Nullable
  public Editable editable() {
    return editable;
  }

  @Override public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof TextViewAfterTextChangeEvent)) return false;
    TextViewAfterTextChangeEvent other = (TextViewAfterTextChangeEvent) o;
    return other.view() == view()
        && editable.equals(other.editable);
  }

  @Override public int hashCode() {
    int result = 17;
    result = result * 37 + view().hashCode();
    result = result * 37 + editable.hashCode();
    return result;
  }

  @Override public String toString() {
    return "TextViewAfterTextChangeEvent{editable="
        + editable
        + ", view="
        + view()
        + '}';
  }

}
