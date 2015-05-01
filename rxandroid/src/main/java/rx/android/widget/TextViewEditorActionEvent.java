package rx.android.widget;

import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.widget.TextView;
import rx.android.view.ViewEvent;

public final class TextViewEditorActionEvent extends ViewEvent<TextView> {
  public static TextViewEditorActionEvent create(TextView view, int actionId, KeyEvent keyEvent) {
    return new TextViewEditorActionEvent(view, actionId, keyEvent);
  }

  private final int actionId;
  private final KeyEvent keyEvent;

  private TextViewEditorActionEvent(@NonNull TextView view, int actionId, KeyEvent keyEvent) {
    super(view);
    this.actionId = actionId;
    this.keyEvent = keyEvent;
  }

  public int actionId() {
    return actionId;
  }

  public KeyEvent keyEvent() {
    return keyEvent;
  }

  @Override public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof TextViewEditorActionEvent)) return false;
    TextViewEditorActionEvent other = (TextViewEditorActionEvent) o;
    return other.view() == view() && other.actionId == actionId && other.keyEvent.equals(keyEvent);
  }

  @Override public int hashCode() {
    int result = 17;
    result = result * 37 + view().hashCode();
    result = result * 37 + actionId;
    result = result * 37 + keyEvent.hashCode();
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
