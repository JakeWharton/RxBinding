package com.jakewharton.rxbinding.support.design.widget;

import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.Tab;
import com.jakewharton.rxbinding.view.ViewEvent;

public final class TabLayoutSelectionEvent extends ViewEvent<TabLayout> {
  public static TabLayoutSelectionEvent create(TabLayout view, Kind kind, Tab tab) {
    return new TabLayoutSelectionEvent(view, kind, tab);
  }

  public enum Kind {
    SELECTED,
    RESELECTED,
    UNSELECTED
  }

  private final Kind kind;
  private final Tab tab;

  private TabLayoutSelectionEvent(@NonNull TabLayout view, Kind kind, Tab tab) {
    super(view);
    this.tab = tab;
    this.kind = kind;
  }

  public Kind kind() {
    return kind;
  }

  public Tab tab() {
    return tab;
  }

  @Override public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof TabLayoutSelectionEvent)) return false;
    TabLayoutSelectionEvent other = (TabLayoutSelectionEvent) o;
    return view() == other.view()
        && kind == other.kind
        && tab == other.tab;
  }

  @Override public int hashCode() {
    int result = 17;
    result = result * 37 + view().hashCode();
    result = result * 37 + kind.hashCode();
    result = result * 37 + tab.hashCode();
    return result;
  }

  @Override public String toString() {
    return "ViewTouchEvent{view=" + view() + ", kind=" + kind + ", tab=" + tab + '}';
  }
}
