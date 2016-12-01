package com.jakewharton.rxbinding.widget;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jakewharton.rxbinding.view.ActivityResultEvent;

import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;

import rx.Observable;
import rx.functions.Func0;
import rx.subjects.PublishSubject;

public class ActivityResultBroker extends Fragment {

  static final String TAG = ActivityResultBroker.class.getSimpleName();
  private static final int REQUEST_CODE = randInt(0, 65535);
  public static final String KEY_INTENT = "KEY_INTENT";
  public static final String KEY_OPTIONS = "KEY_OPTIONS";

  private final PublishSubject<ActivityResultEvent> resultSubject = PublishSubject.create();

  static Observable<ActivityResultEvent> startActivityForResult(
      final FragmentManager fragmentManager, final Intent intent, @Nullable final Bundle options) {
    return Observable.defer(new Func0<Observable<ActivityResultEvent>>() {
      @Override
      public Observable<ActivityResultEvent> call() {
        ActivityResultBroker broker = new ActivityResultBroker();
        Bundle args = new Bundle();
        broker.setArguments(args);
        args.putParcelable(KEY_INTENT, intent);
        if (options != null) {
          args.putParcelable(KEY_OPTIONS, options);
        }

        fragmentManager.beginTransaction()
            .add(broker, TAG)
            .commit();
        return broker.resultSubject;
      }
    });
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
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

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_CODE) {
      resultSubject.onNext(ActivityResultEvent.create(resultCode, data));
      resultSubject.onCompleted();
      getFragmentManager()
          .beginTransaction()
          .remove(this)
          .commit();
    }
  }

  /**
   * @return a pseudo-random number between min and max, inclusive.
   */
  private static int randInt(int min, int max) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      return ThreadLocalRandom.current().nextInt(min, max + 1);
    } else {
      return new SecureRandom().nextInt((max - min) + 1) + min;
    }
  }

}


