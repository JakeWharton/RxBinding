package com.jakewharton.rxbinding2.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.MenuItem;
import android.widget.Toolbar;
import com.jakewharton.rxbinding2.internal.GenericTypeNullable;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

/**
 * Static factory methods for creating {@linkplain Observable observables} for {@link Toolbar}.
 */
@RequiresApi(LOLLIPOP)
public final class RxToolbar {
  /**
   * Create an observable which emits the clicked item in {@code view}'s menu.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Observable<MenuItem> itemClicks(@NonNull Toolbar view) {
    checkNotNull(view, "view == null");
    return new ToolbarItemClickObservable(view);
  }

  /**
   * Create an observable which emits on {@code view} navigation click events. The emitted value is
   * unspecified and should only be used as notification.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Warning:</em> The created observable uses {@link Toolbar#setNavigationOnClickListener}
   * to observe clicks. Only one observable can be used for a view at a time.
   */
  @CheckResult @NonNull
  public static Observable<Object> navigationClicks(@NonNull Toolbar view) {
    checkNotNull(view, "view == null");
    return new ToolbarNavigationClickObservable(view);
  }

  /**
   * An action which sets the title property of {@code view} with character sequences.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull @GenericTypeNullable
  public static Consumer<? super CharSequence> title(@NonNull final Toolbar view) {
    checkNotNull(view, "view == null");
    return new Consumer<CharSequence>() {
      @Override public void accept(CharSequence title) {
        view.setTitle(title);
      }
    };
  }

  /**
   * An action which sets the title property of {@code view} string resource IDs.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Consumer<? super Integer> titleRes(@NonNull final Toolbar view) {
    checkNotNull(view, "view == null");
    return new Consumer<Integer>() {
      @Override public void accept(Integer titleRes) {
        view.setTitle(titleRes);
      }
    };
  }

  /**
   * An action which sets the subtitle property of {@code view} with character sequences.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull @GenericTypeNullable
  public static Consumer<? super CharSequence> subtitle(@NonNull final Toolbar view) {
    checkNotNull(view, "view == null");
    return new Consumer<CharSequence>() {
      @Override public void accept(CharSequence subtitle) {
        view.setSubtitle(subtitle);
      }
    };
  }

  /**
   * An action which sets the subtitle property of {@code view} string resource IDs.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Consumer<? super Integer> subtitleRes(@NonNull final Toolbar view) {
    checkNotNull(view, "view == null");
    return new Consumer<Integer>() {
      @Override public void accept(Integer subtitleRes) {
        view.setSubtitle(subtitleRes);
      }
    };
  }

  private RxToolbar() {
    throw new AssertionError("No instances.");
  }
}
