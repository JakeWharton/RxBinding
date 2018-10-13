package com.jakewharton.rxbinding2.view;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import android.view.MenuItem;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class MenuItemActionViewExpandEvent extends MenuItemActionViewEvent {
  @CheckResult @NonNull
  public static MenuItemActionViewExpandEvent create(@NonNull MenuItem menuItem) {
    return new AutoValue_MenuItemActionViewExpandEvent(menuItem);
  }

  MenuItemActionViewExpandEvent() {
  }
}
