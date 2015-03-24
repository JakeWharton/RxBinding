package rx.android.util;

import android.util.Log;
import rx.functions.Action1;

import static rx.android.internal.Preconditions.checkNotNull;

/** Static factory methods for creating {@linkplain Action1 actions} for {@link Log}. */
public final class RxLog {
  /** Log values at verbose level using the {@code tag}. */
  public static Action1<? super Object> v(String tag) {
    return log(Log.VERBOSE, tag);
  }

  /** Log values at debug level using the {@code tag}. */
  public static Action1<? super Object> d(String tag) {
    return log(Log.DEBUG, tag);
  }

  /** Log values at info level using the {@code tag}. */
  public static Action1<? super Object> i(String tag) {
    return log(Log.INFO, tag);
  }

  /** Log values at warn level using the {@code tag}. */
  public static Action1<? super Object> w(String tag) {
    return log(Log.WARN, tag);
  }

  /** Log values at error level using the {@code tag}. */
  public static Action1<? super Object> e(String tag) {
    return log(Log.ERROR, tag);
  }

  private static Action1<? super Object> log(final int priority, final String tag) {
    checkNotNull(tag, "tag == null");
    return new Action1<Object>() {
      @Override public void call(Object o) {
        Log.println(priority, tag, String.valueOf(o));
      }
    };
  }

  /** Log errors using the {@code tag}. */
  public static Action1<? super Throwable> error(String tag) {
    return error(tag, "");
  }

  /** Log errors using the {@code tag} and {@code message}. */
  public static Action1<? super Throwable> error(final String tag, final String message) {
    checkNotNull(tag, "tag == null");
    checkNotNull(message, "message == null");
    return new Action1<Throwable>() {
      @Override public void call(Throwable throwable) {
        Log.e(tag, message, throwable);
      }
    };
  }

  private RxLog() {
    throw new AssertionError("No instances.");
  }
}
