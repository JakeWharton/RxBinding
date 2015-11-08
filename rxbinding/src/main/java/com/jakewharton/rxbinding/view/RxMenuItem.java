package com.jakewharton.rxbinding.view;

import android.graphics.drawable.Drawable;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.MenuItem;

import com.jakewharton.rxbinding.internal.Functions;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

/**
 * Static factory methods for creating {@linkplain Observable observables} and {@linkplain Action1
 * actions} for {@link MenuItem}.
 */
public final class RxMenuItem {

  /**
   * Create an observable which emits on {@code menuItem} click events. The emitted value is
   * unspecified and should only be used as notification.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code menuItem}.
   * Unsubscribe to free this reference.
   * <p>
   * <em>Warning:</em> The created observable uses {@link MenuItem#setOnMenuItemClickListener} to
   * observe clicks. Only one observable can be used for a menu item at a time.
   */
  @CheckResult @NonNull
  public static Observable<Void> clicks(@NonNull MenuItem menuItem) {
    checkNotNull(menuItem, "menuItem == null");
    return Observable.create(new MenuItemClickOnSubscribe(menuItem, Functions.FUNC1_ALWAYS_TRUE));
  }

  /**
   * Create an observable which emits on {@code menuItem} click events. The emitted value is
   * unspecified and should only be used as notification.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code menuItem}.
   * Unsubscribe to free this reference.
   * <p>
   * <em>Warning:</em> The created observable uses {@link MenuItem#setOnMenuItemClickListener} to
   * observe clicks. Only one observable can be used for a menu item at a time.
   *
   * @param handled Function invoked with each value to determine the return value of the
   * underlying {@link MenuItem.OnMenuItemClickListener}.
   */
  @CheckResult @NonNull
  public static Observable<Void> clicks(@NonNull MenuItem menuItem,
      @NonNull Func1<? super MenuItem, Boolean> handled) {
    checkNotNull(menuItem, "menuItem == null");
    checkNotNull(handled, "handled == null");
    return Observable.create(new MenuItemClickOnSubscribe(menuItem, handled));
  }

  /**
   * Create an observable of action view events for {@code menuItem}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code menuItem}.
   * Unsubscribe to free this reference.
   * <p>
   * <em>Warning:</em> The created observable uses {@link MenuItem#setOnActionExpandListener} to
   * observe action view events. Only one observable can be used for a menu item at a time.
   */
  @CheckResult @NonNull
  public static Observable<MenuItemActionViewEvent> actionViewEvents(@NonNull MenuItem menuItem) {
    checkNotNull(menuItem, "menuItem == null");
    return Observable.create(new MenuItemActionViewEventOnSubscribe(menuItem,
        Functions.FUNC1_ALWAYS_TRUE));
  }

  /**
   * Create an observable of action view events for {@code menuItem}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code menuItem}.
   * Unsubscribe to free this reference.
   * <p>
   * <em>Warning:</em> The created observable uses {@link MenuItem#setOnActionExpandListener} to
   * observe action view events. Only one observable can be used for a menu item at a time.
   *
   * @param handled Function invoked with each value to determine the return value of the
   * underlying {@link MenuItem.OnActionExpandListener}.
   */
  @CheckResult @NonNull
  public static Observable<MenuItemActionViewEvent> actionViewEvents(@NonNull MenuItem menuItem,
      @NonNull Func1<? super MenuItemActionViewEvent, Boolean> handled) {
    checkNotNull(menuItem, "menuItem == null");
    checkNotNull(handled, "handled == null");
    return Observable.create(new MenuItemActionViewEventOnSubscribe(menuItem, handled));
  }

  /**
   * An action which sets the checked property of {@code menuItem}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code menuItem}.
   * Unsubscribe to free this reference.
   */
  @CheckResult @NonNull
  public static Action1<? super Boolean> checked(@NonNull final MenuItem menuItem) {
    checkNotNull(menuItem, "menuItem == null");
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
    checkNotNull(menuItem, "menuItem == null");
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
    checkNotNull(menuItem, "menuItem == null");
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
    checkNotNull(menuItem, "menuItem == null");
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
    checkNotNull(menuItem, "menuItem == null");
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
    checkNotNull(menuItem, "menuItem == null");
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
    checkNotNull(menuItem, "menuItem == null");
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
