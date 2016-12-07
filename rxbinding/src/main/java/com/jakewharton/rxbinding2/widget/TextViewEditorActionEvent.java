package com.jakewharton.rxbinding2.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.ViewEvent;

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

  @Nullable public KeyEvent keyEvent() {
    return keyEvent;
  }

  @Override public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof TextViewEditorActionEvent)) return false;
    TextViewEditorActionEvent other = (TextViewEditorActionEvent) o;
    return other.view() == view()
        && other.actionId == actionId
        && (other.keyEvent != null ? other.keyEvent.equals(keyEvent) : keyEvent == null);
  }

  @Override public int hashCode() {
    int result = 17;
    result = result * 37 + view().hashCode();
    result = result * 37 + actionId;
    result = result * 37 + (keyEvent != null ? keyEvent.hashCode() : 0);
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
