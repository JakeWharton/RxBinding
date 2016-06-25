package com.jakewharton.rxbinding.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.widget.TextView;
import com.jakewharton.rxbinding.view.ViewEvent;

public final class TextViewEditorActionEvent extends ViewEvent<TextView> {
  @CheckResult @NonNull
  public static TextViewEditorActionEvent create(@NonNull TextView view, int actionId,
      @Nullable KeyEvent keyEvent) {
    return new TextViewEditorActionEvent(view, actionId, keyEvent);
  }

  private final int actionId;
  @Nullable private final KeyEvent keyEvent;

  private TextViewEditorActionEvent(@NonNull TextView view, int actionId,
      @Nullable KeyEvent keyEvent) {
    super(view);
    this.actionId = actionId;
    this.keyEvent = keyEvent;
  }

  public int actionId() {
    return actionId;
  }

  /**
   * If triggered by an enter key, this is the event, otherwise {@code null}.
   *
   * @see TextView.OnEditorActionListener#onEditorAction(android.widget.TextView, int,
   * android.view.KeyEvent)
   */
  @Nullable public KeyEvent keyEvent() {
    return keyEvent;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof TextViewEditorActionEvent)) return false;
    TextViewEditorActionEvent other = (TextViewEditorActionEvent) o;
    if (actionId != other.actionId) return false;
    if (keyEvent != null ? !keyEvent.equals(other.keyEvent) : other.keyEvent != null) return false;
    return view() == other.view();
  }

  @Override public int hashCode() {
    int result = actionId;
    result = 31 * result + (keyEvent != null ? keyEvent.hashCode() : 0);
    result = 31 * result + view().hashCode();
    return result;
  }

  @Override public String toString() {
    return "TextViewEditorActionEvent{view="
        + view()
        + ", actionId="
        + actionId
        + ", keyEvent="
        + keyEvent
        + '}';
  }
}
