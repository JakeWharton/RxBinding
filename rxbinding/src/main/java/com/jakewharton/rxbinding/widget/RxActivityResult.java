package com.jakewharton.rxbinding.widget;


import android.app.Activity;
import android.content.Intent;

import com.jakewharton.rxbinding.view.ActivityResultEvent;

import rx.Observable;

/**
 * Static factory methods for creating {@linkplain Observable observables} of an Activity result
 * for a started {@link android.app.Activity}.
 */
public class RxActivityResult {

  /**
   * Start an activity and get an {@link Observable<ActivityResultEvent>} that only emits the result
   * of the intent started.
   *
   * @param activity The Activity to host the intent start behavior.
   * @param intent   The intent to start.
   */
  public static Observable<ActivityResultEvent> startActivityForResult(Activity activity,
                                                                       Intent intent) {
    return ActivityResultBroker.startActivityForResult(activity.getFragmentManager(), intent, null);
  }

//  NOTE: disabled until the generateKotlin task doesn't choke on 3 arguments.
//  /**
//   * Start an activity and get an {@link Observable<ActivityResultEvent>} that only emits the result
//   * of the intent started.
//   *
//   * @param activity The Activity to host the intent start behavior.
//   * @param intent   The intent to start.
//   * @param options  Additional options for how the Activity should be started.
//   */
//  @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
//  public static Observable<ActivityResultEvent> startActivityForResult(Activity activity,
//                                                                       Intent intent,
//                                                                       Bundle options) {
//    return ActivityResultBroker.startActivityForResult(activity.getFragmentManager(), intent, options);
//  }

}

