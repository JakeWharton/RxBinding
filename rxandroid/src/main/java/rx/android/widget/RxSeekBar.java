package rx.android.widget;

import android.widget.SeekBar;
import rx.Observable;

public final class RxSeekBar {
  /**
   * Create an observable of progress value changes on {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  public static Observable<Integer> changes(SeekBar view) {
    return Observable.create(new SeekBarChangeOnSubscribe(view));
  }

  /**
   * Create an observable of progress change events for {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  public static Observable<SeekBarChangeEvent> changeEvents(SeekBar view) {
    return Observable.create(new SeekBarChangeEventOnSubscribe(view));
  }

  private RxSeekBar() {
    throw new AssertionError("No instances.");
  }
}
