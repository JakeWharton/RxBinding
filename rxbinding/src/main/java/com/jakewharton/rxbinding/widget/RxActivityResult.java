package com.jakewharton.rxbinding.widget;


import android.app.Activity;
import android.content.Intent;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.jakewharton.rxbinding.view.ActivityResultEvent;

import rx.Observable;

import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

/**
 * Static factory methods for creating {@linkplain Observable observables} of an Activity result
 * for a started {@link android.app.Activity}.
 */
public class RxActivityResult {

  private RxActivityResult() {
  }

  /**
   * Start an activity and get an {@link Observable<ActivityResultEvent>} that only emits the result
   * of the intent started.
   *
   * @param activity The Activity to host the intent start behavior.
   * @param intent   The intent to start.
   */
  @CheckResult @NonNull
  public static Observable<ActivityResultEvent> startActivityForResult(@NonNull Activity activity,
                                                                       @NonNull Intent intent) {
    checkNotNull(activity, "activity == null");
    checkNotNull(intent, "intent == null");
    return ActivityResultBroker.startActivityForResult(activity.getFragmentManager(), intent, null);
  }

}

