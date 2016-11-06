package com.jakewharton.rxbinding;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.jakewharton.rxrelay.PublishRelay;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Func1;

@SuppressWarnings({"WeakerAccess", "unused"})
public class ActivityMonitor implements Application.ActivityLifecycleCallbacks {

  private final List<Activity> createdActivities = new ArrayList<>();
  private PublishRelay<LifeCycleSignal> signalRelay = PublishRelay.create();
  private Lock lock = new ReentrantLock();

  public ActivityMonitor(Context context) {
    Application application = (Application) context.getApplicationContext();
    application.registerActivityLifecycleCallbacks(this);
  }

  private boolean activityContainIntentStringExtra(Activity activity, String key, String value) {
    String activityValue = activity.getIntent().getStringExtra(key);
    return value.equals(activityValue);
  }

  private <T extends Activity> T findCreatedActivityWithIntentStringExtra(String key, String value) {
    T result = null;
    for (Activity createdActivity : createdActivities) {
      if (activityContainIntentStringExtra(createdActivity, key, value)) {
        //noinspection unchecked
        result = (T) createdActivity;
      }
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  public void waitForActivityDestructionWithIntentStringExtra(final String key,
                                                              final String value) {
    lock.lock();
    Activity a = findCreatedActivityWithIntentStringExtra(key, value);
    if (a != null) {
      signalRelay.filter(new Func1<LifeCycleSignal, Boolean>() {
        @Override
        public Boolean call(LifeCycleSignal event) {
          return event.lifeCycleEvent == LifeCycleEvent.DESTROYED && activityContainIntentStringExtra(event.activity, key, value);
        }
      }).doOnSubscribe(new Action0() {
        @Override
        public void call() {
          lock.unlock();
        }
      }).timeout(5, TimeUnit.SECONDS).toBlocking().first();
    }
  }

  @SuppressWarnings("unchecked")
  public <T extends Activity> T waitForActivityCreationWithIntentStringExtra(final String key,
                                                                             final String value) {
    lock.lock();
    T a = findCreatedActivityWithIntentStringExtra(key, value);
    if (a != null) {
      lock.unlock();
      return a;
    } else {
      return (T) signalRelay.filter(new Func1<LifeCycleSignal, Boolean>() {
        @Override
        public Boolean call(LifeCycleSignal event) {
          return event.lifeCycleEvent == LifeCycleEvent.CREATED && activityContainIntentStringExtra(event.activity, key, value);
        }
      }).doOnSubscribe(new Action0() {
        @Override
        public void call() {
          lock.unlock();
        }
      }).timeout(5, TimeUnit.SECONDS).toBlocking().first().activity;
    }
  }

  private void emitSignal(Activity activity, LifeCycleEvent event) {
    signalRelay.call(new LifeCycleSignal(activity, event));
  }

  public Observable<LifeCycleSignal> activityLifeCycleObservables() {
    return signalRelay.asObservable();
  }

  @Override
  public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    lock.lock();
    createdActivities.add(activity);
    emitSignal(activity, LifeCycleEvent.CREATED);
    lock.unlock();
  }

  @Override
  public void onActivityStarted(Activity activity) {
    emitSignal(activity, LifeCycleEvent.STARTED);
  }

  @Override
  public void onActivityResumed(Activity activity) {
    emitSignal(activity, LifeCycleEvent.RESUMED);
  }

  @Override
  public void onActivityPaused(Activity activity) {
    emitSignal(activity, LifeCycleEvent.PAUSED);
  }

  @Override
  public void onActivityStopped(Activity activity) {
    emitSignal(activity, LifeCycleEvent.STOPPED);
  }

  @Override
  public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    emitSignal(activity, LifeCycleEvent.SAVE_INSTANCE_STATE);
  }

  @Override
  public void onActivityDestroyed(Activity activity) {
    lock.lock();
    createdActivities.remove(activity);
    emitSignal(activity, LifeCycleEvent.DESTROYED);
    lock.unlock();
  }

  enum LifeCycleEvent {
    CREATED, STARTED, RESUMED, PAUSED, STOPPED, SAVE_INSTANCE_STATE, DESTROYED
  }

  public static class LifeCycleSignal {
    public final Activity activity;
    public final LifeCycleEvent lifeCycleEvent;

    private LifeCycleSignal(Activity activity, LifeCycleEvent lifeCycleEvent) {
      this.activity = activity;
      this.lifeCycleEvent = lifeCycleEvent;
    }
  }
}
