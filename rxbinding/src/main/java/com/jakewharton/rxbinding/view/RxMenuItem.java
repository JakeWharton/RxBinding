package com.jakewharton.rxbinding.view;

import android.graphics.drawable.Drawable;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import rx.Observable;
import rx.functions.Action1;

/**
 * Static factory methods for creating {@linkplain Observable observables} and {@linkplain Action1
 * actions} for {@link MenuItem}.
 */
public final class RxMenuItem {

  /**
   * An action which sets the checked property of {@code menuItem}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code menuItem}.
   * Unsubscribe to free this reference.
   */
  @CheckResult @NonNull
  public static Action1<? super Boolean> checked(@NonNull final MenuItem menuItem) {
    return new Action1<Boolean>() {
      @Override public void call(Boolean value) {
        menuItem.setChecked(value);
      }
    };
  }

  /**
   * An action which sets the enabled property of {@code menuItem}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code menuItem}.
   * Unsubscribe to free this reference.
   */
  @CheckResult @NonNull
  public static Action1<? super Boolean> enabled(@NonNull final MenuItem menuItem) {
    return new Action1<Boolean>() {
      @Override public void call(Boolean value) {
        menuItem.setEnabled(value);
      }
    };
  }

  /**
   * An action which sets the icon property of {@code menuItem}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code menuItem}.
   * Unsubscribe to free this reference.
   */
  @CheckResult @NonNull
  public static Action1<? super Drawable> icon(@NonNull final MenuItem menuItem) {
    return new Action1<Drawable>() {
      @Override public void call(Drawable value) {
        menuItem.setIcon(value);
      }
    };
  }

  /**
   * An action which sets the icon property of {@code menuItem}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code menuItem}.
   * Unsubscribe to free this reference.
   */
  @CheckResult @NonNull
  public static Action1<? super Integer> iconRes(@NonNull final MenuItem menuItem) {
    return new Action1<Integer>() {
      @Override public void call(Integer value) {
        menuItem.setIcon(value);
      }
    };
  }

  /**
   * An action which sets the title property of {@code menuItem}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code menuItem}.
   * Unsubscribe to free this reference.
   */
  @CheckResult @NonNull
  public static Action1<? super CharSequence> title(@NonNull final MenuItem menuItem) {
    return new Action1<CharSequence>() {
      @Override public void call(CharSequence value) {
        menuItem.setTitle(value);
      }
    };
  }

  /**
   * An action which sets the title property of {@code menuItem}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code menuItem}.
   * Unsubscribe to free this reference.
   */
  @CheckResult @NonNull
  public static Action1<? super Integer> titleRes(@NonNull final MenuItem menuItem) {
    return new Action1<Integer>() {
      @Override public void call(Integer value) {
        menuItem.setTitle(value);
      }
    };
  }

  /**
   * An action which sets the visibility property of {@code menuItem}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code menuItem}.
   * Unsubscribe to free this reference.
   */
  @CheckResult @NonNull
  public static Action1<? super Boolean> visible(@NonNull final MenuItem menuItem) {
    return new Action1<Boolean>() {
      @Override public void call(Boolean value) {
        menuItem.setVisible(value);
      }
    };
  }

  private RxMenuItem() {
    throw new AssertionError("No instances.");
  }
}
