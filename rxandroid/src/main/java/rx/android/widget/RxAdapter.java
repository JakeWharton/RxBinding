package rx.android.widget;

import android.widget.Adapter;
import rx.Observable;

/**
 * Static factory methods for creating {@linkplain Observable observables} for {@link Adapter}.
 */
public final class RxAdapter {
  /** Create an observable of data change events for {@code adapter}. */
  public static <T extends Adapter> Observable<T> dataChanges(T adapter) {
    return Observable.create(new AdapterDataChangeOnSubscribe<>(adapter));
  }

  private RxAdapter() {
    throw new AssertionError("No instances.");
  }
}
