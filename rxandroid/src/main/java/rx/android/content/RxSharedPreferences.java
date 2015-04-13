package rx.android.content;

import android.content.SharedPreferences;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;

import static android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import static rx.Observable.OnSubscribe;
import static rx.android.internal.Preconditions.checkNotNull;

/** A factory for observables and actions which operate on {@link SharedPreferences}. */
public final class RxSharedPreferences {
  /** Create an instance which uses {@code sharedPreferences} for storage. */
  public static RxSharedPreferences create(SharedPreferences sharedPreferences) {
    return new RxSharedPreferences(sharedPreferences);
  }

  private final SharedPreferences sharedPreferences;
  private final Observable<String> changedKeys;

  private RxSharedPreferences(final SharedPreferences sharedPreferences) {
    this.sharedPreferences = checkNotNull(sharedPreferences, "sharedPreferences == null");
    this.changedKeys = Observable.create(new OnSubscribe<String>() {
      @Override public void call(final Subscriber<? super String> subscriber) {
        final OnSharedPreferenceChangeListener listener = new OnSharedPreferenceChangeListener() {
          @Override
          public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            subscriber.onNext(key);
          }
        };

        Subscription subscription = Subscriptions.create(new Action0() {
          @Override public void call() {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
          }
        });
        subscriber.add(subscription);

        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
      }
    }).share();
  }

  /** Observe changes to one key, including a synthetic initial emission. */
  private Observable<String> observeKey(final String key) {
    return changedKeys //
        .filter(new Func1<String, Boolean>() {
          @Override public Boolean call(String value) {
            return key.equals(value);
          }
        }) //
        .startWith(key);
  }

  /** Observe the {@code boolean} preference for {@code key} with a default of {@code false}. */
  public Observable<Boolean> getBoolean(String key) {
    return getBoolean(key, false);
  }

  /**
   * Create an observable for the {@code boolean} preference of {@code key} with a {@code
   * defaultValue} default.
   */
  public Observable<Boolean> getBoolean(String key, final boolean defaultValue) {
    return observeKey(key).map(new Func1<String, Boolean>() {
      @Override public Boolean call(String changedKey) {
        return sharedPreferences.getBoolean(changedKey, defaultValue);
      }
    });
  }

  /** Create an action which sets the value of {@code key} to a {@code boolean}. */
  public Action1<Boolean> setBoolean(final String key) {
    return new Action1<Boolean>() {
      @Override public void call(Boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
      }
    };
  }

  /** Observe the {@code float} preference for {@code key} with a default of {@code 0f}. */
  public Observable<Float> getFloat(String key) {
    return getFloat(key, 0);
  }

  /**
   * Create an observable for the {@code float} preference of {@code key} with a {@code
   * defaultValue} default.
   */
  public Observable<Float> getFloat(String key, final float defaultValue) {
    return observeKey(key).map(new Func1<String, Float>() {
      @Override public Float call(String changedKey) {
        return sharedPreferences.getFloat(changedKey, defaultValue);
      }
    });
  }

  /** Create an action which sets the value of {@code key} to a {@code float}. */
  public Action1<Float> setFloat(final String key) {
    return new Action1<Float>() {
      @Override public void call(Float value) {
        sharedPreferences.edit().putFloat(key, value).apply();
      }
    };
  }

  /** Observe the {@code int} preference for {@code key} with a default of {@code 0}. */
  public Observable<Integer> getInt(String key) {
    return getInt(key, 0);
  }

  /**
   * Create an observable for the {@code int} preference of {@code key} with a {@code
   * defaultValue} default.
   */
  public Observable<Integer> getInt(String key, final int defaultValue) {
    return observeKey(key).map(new Func1<String, Integer>() {
      @Override public Integer call(String changedKey) {
        return sharedPreferences.getInt(changedKey, defaultValue);
      }
    });
  }

  /** Create an action which sets the value of {@code key} to a {@code int}. */
  public Action1<Integer> setInt(final String key) {
    return new Action1<Integer>() {
      @Override public void call(Integer value) {
        sharedPreferences.edit().putInt(key, value).apply();
      }
    };
  }

  /** Observe the {@code String} preference for {@code key} with a default of {@code null}. */
  public Observable<String> getString(String key) {
    return getString(key, null);
  }

  /**
   * Create an observable for the {@code String} preference of {@code key} with a {@code
   * defaultValue} default.
   */
  public Observable<String> getString(String key, final String defaultValue) {
    return observeKey(key).map(new Func1<String, String>() {
      @Override public String call(String changedKey) {
        return sharedPreferences.getString(changedKey, defaultValue);
      }
    });
  }

  /** Create an action which sets the value of {@code key} to a {@code String}. */
  public Action1<String> setString(final String key) {
    return new Action1<String>() {
      @Override public void call(String value) {
        sharedPreferences.edit().putString(key, value).apply();
      }
    };
  }
}
