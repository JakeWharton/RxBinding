package rx.android.widget;

import android.widget.ProgressBar;
import rx.functions.Action1;

public final class RxProgressBar {
  /**
   * An action which increments the progress value of {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  public static Action1<? super Integer> incrementProgessBy(final ProgressBar view) {
    return new Action1<Integer>() {
      @Override public void call(Integer value) {
        view.incrementProgressBy(value);
      }
    };
  }

  /**
   * An action which increments the secondary progress value of {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  public static Action1<? super Integer> incrementSecondaryProgressBy(final ProgressBar view) {
    return new Action1<Integer>() {
      @Override public void call(Integer value) {
        view.incrementSecondaryProgressBy(value);
      }
    };
  }

  /**
   * An action which sets whether {@code view} is indeterminate.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  public static Action1<? super Boolean> setIndeterminate(final ProgressBar view) {
    return new Action1<Boolean>() {
      @Override public void call(Boolean value) {
        view.setIndeterminate(value);
      }
    };
  }

  /**
   * An action which sets the max value of {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  public static Action1<? super Integer> setMax(final ProgressBar view) {
    return new Action1<Integer>() {
      @Override public void call(Integer value) {
        view.setMax(value);
      }
    };
  }

  /**
   * An action which sets the progress value of {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  public static Action1<? super Integer> setProgress(final ProgressBar view) {
    return new Action1<Integer>() {
      @Override public void call(Integer value) {
        view.setProgress(value);
      }
    };
  }

  /**
   * An action which sets the secondary progress value of {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  public static Action1<? super Integer> setSecondaryProgress(final ProgressBar view) {
    return new Action1<Integer>() {
      @Override public void call(Integer value) {
        view.setSecondaryProgress(value);
      }
    };
  }
}
