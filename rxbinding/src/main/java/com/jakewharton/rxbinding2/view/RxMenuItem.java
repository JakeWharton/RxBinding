package com.jakewharton.rxbinding2.view;

import android.graphics.drawable.Drawable;
import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import android.view.MenuItem;
import com.jakewharton.rxbinding2.internal.Functions;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkNotNull;

/**
 * Static factory methods for creating {@linkplain Observable observables} and {@linkplain Consumer
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
  public static Observable<Object> clicks(@NonNull MenuItem menuItem) {
    checkNotNull(menuItem, "menuItem == null");
    return new MenuItemClickOnSubscribe(menuItem, Functions.PREDICATE_ALWAYS_TRUE);
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
  public static Observable<Object> clicks(@NonNull MenuItem menuItem,
      @NonNull Predicate<? super MenuItem> handled) {
    checkNotNull(menuItem, "menuItem == null");
    checkNotNull(handled, "handled == null");
    return new MenuItemClickOnSubscribe(menuItem, handled);
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
    return new MenuItemActionViewEventObservable(menuItem, Functions.PREDICATE_ALWAYS_TRUE);
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
      @NonNull Predicate<? super MenuItemActionViewEvent> handled) {
    checkNotNull(menuItem, "menuItem == null");
    checkNotNull(handled, "handled == null");
    return new MenuItemActionViewEventObservable(menuItem, handled);
  }

  /**
   * An action which sets the checked property of {@code menuItem}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code menuItem}.
   * Unsubscribe to free this reference.
   *
   * @deprecated Use menuItem::setChecked method reference.
   */
  @Deprecated
  @CheckResult @NonNull
  public static Consumer<? super Boolean> checked(@NonNull MenuItem menuItem) {
    checkNotNull(menuItem, "menuItem == null");
    return menuItem::setChecked;
  }

  /**
   * An action which sets the enabled property of {@code menuItem}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code menuItem}.
   * Unsubscribe to free this reference.
   *
   * @deprecated Use menuItem::setEnabled method reference.
   */
  @Deprecated
  @CheckResult @NonNull
  public static Consumer<? super Boolean> enabled(@NonNull MenuItem menuItem) {
    checkNotNull(menuItem, "menuItem == null");
    return menuItem::setEnabled;
  }

  /**
   * An action which sets the icon property of {@code menuItem}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code menuItem}.
   * Unsubscribe to free this reference.
   *
   * @deprecated Use menuItem::setIcon method reference.
   */
  @Deprecated
  @CheckResult @NonNull
  public static Consumer<? super Drawable> icon(@NonNull MenuItem menuItem) {
    checkNotNull(menuItem, "menuItem == null");
    return menuItem::setIcon;
  }

  /**
   * An action which sets the icon property of {@code menuItem}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code menuItem}.
   * Unsubscribe to free this reference.
   *
   * @deprecated Use menuItem::setIcon method reference.
   */
  @Deprecated
  @CheckResult @NonNull
  public static Consumer<? super Integer> iconRes(@NonNull MenuItem menuItem) {
    checkNotNull(menuItem, "menuItem == null");
    return menuItem::setIcon;
  }

  /**
   * An action which sets the title property of {@code menuItem}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code menuItem}.
   * Unsubscribe to free this reference.
   *
   * @deprecated Use menuItem::setTitle method reference.
   */
  @Deprecated
  @CheckResult @NonNull
  public static Consumer<? super CharSequence> title(@NonNull MenuItem menuItem) {
    checkNotNull(menuItem, "menuItem == null");
    return menuItem::setTitle;
  }

  /**
   * An action which sets the title property of {@code menuItem}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code menuItem}.
   * Unsubscribe to free this reference.
   *
   * @deprecated Use menuItem::setTitle method reference.
   */
  @Deprecated
  @CheckResult @NonNull
  public static Consumer<? super Integer> titleRes(@NonNull MenuItem menuItem) {
    checkNotNull(menuItem, "menuItem == null");
    return menuItem::setTitle;
  }

  /**
   * An action which sets the visibility property of {@code menuItem}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code menuItem}.
   * Unsubscribe to free this reference.
   *
   * @deprecated Use menuItem::setVisible method reference.
   */
  @Deprecated
  @CheckResult @NonNull
  public static Consumer<? super Boolean> visible(@NonNull MenuItem menuItem) {
    checkNotNull(menuItem, "menuItem == null");
    return menuItem::setVisible;
  }

  private RxMenuItem() {
    throw new AssertionError("No instances.");
  }
}
