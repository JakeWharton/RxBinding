package com.jakewharton.rxbinding;

import android.app.Activity;
import android.support.test.espresso.IdlingResource;
import android.view.View;

public final class ViewDirtyIdlingResource implements IdlingResource {
  private final View decorView;
  private ResourceCallback resourceCallback;

  public ViewDirtyIdlingResource(Activity activity) {
    decorView = activity.getWindow().getDecorView();
  }

  @Override public String getName() {
    return "view dirty";
  }

  @Override public boolean isIdleNow() {
    boolean clean = !decorView.isDirty();
    if (clean) {
      resourceCallback.onTransitionToIdle();
    }
    return clean;
  }

  @Override public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
    this.resourceCallback = resourceCallback;
  }
}
