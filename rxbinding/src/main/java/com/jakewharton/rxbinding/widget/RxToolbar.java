package com.jakewharton.rxbinding.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.MenuItem;
import android.widget.Toolbar;
import rx.Observable;

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
    return Observable.create(new ToolbarItemClickOnSubscribe(view));
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
  public static Observable<Void> navigationClicks(@NonNull Toolbar view) {
    checkNotNull(view, "view == null");
    return Observable.create(new ToolbarNavigationClickOnSubscribe(view));
  }

  private RxToolbar() {
    throw new AssertionError("No instances.");
  }
}
