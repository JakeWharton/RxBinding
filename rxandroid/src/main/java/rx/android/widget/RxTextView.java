package rx.android.widget;

import android.widget.TextView;
import rx.Observable;
import rx.functions.Action1;

import static rx.android.internal.Preconditions.checkNotNull;

/**
 * Static factory methods for creating {@linkplain Observable observables} and {@linkplain Action1
 * actions} for {@link TextView}.
 */
public final class RxTextView {
  /**
   * Create an observable of character sequences for text changes on {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  public static Observable<CharSequence> textChanges(TextView view) {
    checkNotNull(view, "view == null");
    return Observable.create(new TextViewTextOnSubscribe(view));
  }

  /**
   * Create an observable of text change events for {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  public static Observable<TextViewTextChangeEvent> textChangeEvents(TextView view) {
    checkNotNull(view, "view == null");
    return Observable.create(new TextViewTextEventOnSubscribe(view));
  }

  /**
   * An action which sets the text property of {@code view} with character sequences.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  public static Action1<? super CharSequence> setText(final TextView view) {
    checkNotNull(view, "view == null");
    return new Action1<CharSequence>() {
      @Override public void call(CharSequence text) {
        view.setText(text);
      }
    };
  }

  /**
   * An action which sets the text property of {@code view} string resource IDs.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  public static Action1<? super Integer> setTextRes(final TextView view) {
    checkNotNull(view, "view == null");
    return new Action1<Integer>() {
      @Override public void call(Integer textRes) {
        view.setText(textRes);
      }
    };
  }

  private RxTextView() {
    throw new AssertionError("No instances.");
  }
}
