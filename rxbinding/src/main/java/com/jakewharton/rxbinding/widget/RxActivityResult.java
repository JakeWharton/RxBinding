package com.jakewharton.rxbinding.widget;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jakewharton.rxbinding.view.ActivityResultEvent;

import java.security.SecureRandom;

import rx.Observable;
import rx.functions.Func0;
import rx.subjects.PublishSubject;

/**
 * Static factory methods for creating {@linkplain Observable observables} of an Activity result
 * for a started {@link android.app.Activity}.
 */
public class RxActivityResult extends Fragment {

  static final String TAG = RxActivityResult.class.getSimpleName();
  private static final int REQUEST_CODE = new SecureRandom().nextInt();
  public static final String KEY_INTENT = "KEY_INTENT";
  public static final String KEY_OPTIONS = "KEY_OPTIONS";

  private final PublishSubject<ActivityResultEvent> resultSubject = PublishSubject.create();

  /**
   * Start an activity and get an {@link Observable<ActivityResultEvent>} that only emits the result
   * of the intent started.
   *
   * @param activity The Activity to host the intent start behavior.
   * @param intent   The intent to start.
   */
  public static Observable<ActivityResultEvent> startActivityForResult(Activity activity,
                                                                       Intent intent) {
    return startActivityForResult(activity.getFragmentManager(), intent, null);
  }

  /**
   * Start an activity and get an {@link Observable<ActivityResultEvent>} that only emits the result
   * of the intent started.
   *
   * @param activity The Activity to host the intent start behavior.
   * @param intent   The intent to start.
   * @param options  Additional options for how the Activity should be started.
   */
  @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
  public static Observable<ActivityResultEvent> startActivityForResult(Activity activity,
                                                                       Intent intent,
                                                                       Bundle options) {
    return startActivityForResult(activity.getFragmentManager(), intent, options);
  }

  private static Observable<ActivityResultEvent> startActivityForResult(final FragmentManager fragmentManager,
                                                                        final Intent intent,
                                                                        @Nullable final Bundle options) {
    return Observable.defer(new Func0<Observable<ActivityResultEvent>>() {
      @Override
      public Observable<ActivityResultEvent> call() {
        RxActivityResult rxActivityResult = new RxActivityResult();
        Bundle args = new Bundle();
        rxActivityResult.setArguments(args);
        args.putParcelable(KEY_INTENT, intent);
        if (options != null) {
          args.putParcelable(KEY_OPTIONS, options);
        }

        fragmentManager.beginTransaction()
            .add(rxActivityResult, TAG)
            .commit();
        return rxActivityResult.resultSubject;
      }
    });
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
    final Bundle arguments = getArguments();
    Intent intent = arguments.getParcelable(KEY_INTENT);
    Bundle options = arguments.getBundle(KEY_OPTIONS);
    if (options != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
      startActivityForResult(intent, REQUEST_CODE, options);
    } else {
      startActivityForResult(intent, REQUEST_CODE);
    }
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_CODE) {
      resultSubject.onNext(new ActivityResultEvent(resultCode, data));
      resultSubject.onCompleted();
      getFragmentManager()
          .beginTransaction()
          .remove(this)
          .commit();
    }
  }

}

