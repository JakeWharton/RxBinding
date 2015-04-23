package rx.android.widget;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import rx.android.view.ViewEvent;

public final class AdapterViewItemClickEvent extends ViewEvent<AdapterView<?>> {
  public static AdapterViewItemClickEvent create(@NonNull AdapterView<?> view, View selectedView,
      int position, long id) {
    return new AdapterViewItemClickEvent(view, selectedView, position, id);
  }

  private final View clickedView;
  private final int position;
  private final long id;

  private AdapterViewItemClickEvent(@NonNull AdapterView<?> view, View clickedView,
      int position, long id) {
    super(view);
    this.clickedView = clickedView;
    this.position = position;
    this.id = id;
  }

  public View clickedView() {
    return clickedView;
  }

  public int position() {
    return position;
  }

  public long id() {
    return id;
  }

  @Override public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof AdapterViewItemClickEvent)) return false;
    AdapterViewItemClickEvent other = (AdapterViewItemClickEvent) o;
    return other.view() == view()
        && other.clickedView == clickedView
        && other.position == position
        && other.id == id;
  }

  @Override public int hashCode() {
    int result = 17;
    result = result * 37 + view().hashCode();
    result = result * 37 + clickedView.hashCode();
    result = result * 37 + position;
    result = result * 37 + (int) (id ^ (id >>> 32));
    return result;
  }

  @Override public String toString() {
    return "AdapterViewItemClickEvent{view="
        + view()
        + ", clickedView="
        + clickedView
        + ", position="
        + position
        + ", id="
        + id
        + '}';
  }
}
