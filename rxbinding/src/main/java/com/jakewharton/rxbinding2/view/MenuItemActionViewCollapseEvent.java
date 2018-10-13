package com.jakewharton.rxbinding2.view;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import android.view.MenuItem;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class MenuItemActionViewCollapseEvent extends MenuItemActionViewEvent {
  @CheckResult @NonNull
  public static MenuItemActionViewCollapseEvent create(@NonNull MenuItem menuItem) {
    return new AutoValue_MenuItemActionViewCollapseEvent(menuItem);
  }

  MenuItemActionViewCollapseEvent() {
  }
}
