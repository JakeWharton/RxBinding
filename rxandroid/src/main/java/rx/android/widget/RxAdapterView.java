package rx.android.widget;

import android.widget.AdapterView;
import rx.Observable;
import rx.functions.Action1;

/**
 * Static factory methods for creating {@linkplain Observable observables} and {@linkplain Action1
 * actions} for {@link AdapterView}.
 */
public final class RxAdapterView {
  /**
   * Create an observable of the selected position of {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  public static Observable<Integer> itemSelections(AdapterView<?> view) {
    return Observable.create(new AdapterViewItemSelectionOnSubscribe(view));
  }

  /**
   * Create an observable of selection events for {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  public static Observable<AdapterViewSelectionEvent> selectionEvents(AdapterView<?> view) {
    return Observable.create(new AdapterViewSelectionOnSubscribe(view));
  }

  /**
   * An action which sets the selected position of {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  public static Action1<? super Integer> setSelection(final AdapterView<?> view) {
    return new Action1<Integer>() {
      @Override public void call(Integer position) {
        view.setSelection(position);
      }
    };
  }

  private RxAdapterView() {
    throw new AssertionError("No instances.");
  }
}
