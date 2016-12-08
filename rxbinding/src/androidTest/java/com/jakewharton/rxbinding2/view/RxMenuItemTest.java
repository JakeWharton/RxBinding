package com.jakewharton.rxbinding2.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import com.jakewharton.rxbinding.RecordingObserver;
import com.jakewharton.rxbinding.test.R;
import com.jakewharton.rxbinding2.view.MenuItemActionViewEvent.Kind;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import io.reactivex.functions.Function;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class) public final class RxMenuItemTest {
  @Rule public final UiThreadTestRule uiThread = new UiThreadTestRule();

  private final Context context = InstrumentationRegistry.getContext();
  private final TestMenuItem menuItem = new TestMenuItem(context);

  @Test @UiThreadTest public void clicks() {
    RecordingObserver<Object> o = new RecordingObserver<>();
    RxMenuItem.clicks(menuItem).subscribe(o);
    o.assertNoMoreEvents(); // No initial value.

    menuItem.performClick();
    assertThat(o.takeNext()).isNotNull();

    menuItem.performClick();
    assertThat(o.takeNext()).isNotNull();

    o.dispose();

    menuItem.performClick();
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void clicksAvoidHandling() {
    Function<MenuItem, Boolean> handled = new Function<MenuItem, Boolean>() {
      @Override public Boolean apply(MenuItem menuItem) {
        return Boolean.FALSE;
      }
    };

    RecordingObserver<Object> o = new RecordingObserver<>();
    RxMenuItem.clicks(menuItem, handled).subscribe(o);
    o.assertNoMoreEvents(); // No initial value.

    menuItem.performClick();
    o.assertNoMoreEvents();

    menuItem.performClick();
    o.assertNoMoreEvents();

    o.dispose();

    menuItem.performClick();
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void actionViewEvents() {
    RecordingObserver<MenuItemActionViewEvent> o = new RecordingObserver<>();
    RxMenuItem.actionViewEvents(menuItem).subscribe(o);
    o.assertNoMoreEvents(); // No initial value.

    menuItem.expandActionView();
    assertThat(o.takeNext()).isEqualTo(MenuItemActionViewEvent.create(menuItem, Kind.EXPAND));

    menuItem.collapseActionView();
    assertThat(o.takeNext()).isEqualTo(MenuItemActionViewEvent.create(menuItem, Kind.COLLAPSE));

    o.dispose();

    menuItem.performClick();
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void actionViewEventsAvoidHandling() {
    Function<MenuItemActionViewEvent, Boolean> handled =
        new Function<MenuItemActionViewEvent, Boolean>() {
          @Override public Boolean apply(MenuItemActionViewEvent menuItem) {
            return Boolean.FALSE;
          }
        };

    RecordingObserver<MenuItemActionViewEvent> o = new RecordingObserver<>();
    RxMenuItem.actionViewEvents(menuItem, handled).subscribe(o);
    o.assertNoMoreEvents(); // No initial value.

    menuItem.expandActionView();
    assertThat(menuItem.isActionViewExpanded()).isEqualTo(false); // Should be prevented by handler
    o.assertNoMoreEvents();

    o.dispose();

    menuItem.performClick();
    o.assertNoMoreEvents();
  }

  @Test public void checked() throws Exception {
    menuItem.setCheckable(true);
    RxMenuItem.checked(menuItem).accept(true);
    assertThat(menuItem.isChecked()).isEqualTo(true);
    RxMenuItem.checked(menuItem).accept(false);
    assertThat(menuItem.isChecked()).isEqualTo(false);
  }

  @Test public void enabled() throws Exception {
    RxMenuItem.enabled(menuItem).accept(true);
    assertThat(menuItem.isEnabled()).isEqualTo(true);
    RxMenuItem.enabled(menuItem).accept(false);
    assertThat(menuItem.isEnabled()).isEqualTo(false);
  }

  @Test public void icon() throws Exception {
    Drawable drawable = context.getResources().getDrawable(R.drawable.icon);
    RxMenuItem.icon(menuItem).accept(drawable);
    assertThat(menuItem.getIcon()).isEqualTo(drawable);
  }

  @Test public void iconRes() throws Exception {
    ColorDrawable drawable = (ColorDrawable) context.getResources().getDrawable(R.drawable.icon);
    RxMenuItem.iconRes(menuItem).accept(R.drawable.icon);
    assertThat(((ColorDrawable) menuItem.getIcon()).getColor()).isEqualTo(drawable.getColor());
  }

  @Test public void title() throws Exception {
    RxMenuItem.title(menuItem).accept("Hey");
    assertThat(menuItem.getTitle()).isEqualTo("Hey");
  }

  @Test public void titleRes() throws Exception {
    RxMenuItem.titleRes(menuItem).accept(R.string.hey);
    assertThat(menuItem.getTitle()).isEqualTo(context.getText(R.string.hey));
  }

  @Test public void visible() throws Exception {
    RxMenuItem.visible(menuItem).accept(true);
    assertThat(menuItem.isVisible()).isEqualTo(true);
    RxMenuItem.visible(menuItem).accept(false);
    assertThat(menuItem.isVisible()).isEqualTo(false);
  }

  // There is no accessible default implementation of MenuItem, so we have to create one
  private static final class TestMenuItem implements MenuItem {

    private final Context context;

    private int itemId;
    private int groupId;
    private int order;
    private CharSequence title;
    private CharSequence titleCondensed;
    private Drawable icon;
    private Intent intent;
    private char numericChar;
    private char alphaChar;
    private boolean checkable;
    private boolean checked;
    private boolean visible;
    private boolean enabled;
    private OnMenuItemClickListener menuItemClickListener;
    private int actionEnum;
    private View actionView;
    private ActionProvider actionProvider;
    private boolean isActionViewExpanded;
    private OnActionExpandListener actionExpandListener;

    public TestMenuItem(Context context) {
      this.context = context;
    }

    public void performClick() {
      if (menuItemClickListener != null) {
        menuItemClickListener.onMenuItemClick(this);
      }
    }

    @Override public int getItemId() {
      return itemId;
    }

    @Override public int getGroupId() {
      return groupId;
    }

    @Override public int getOrder() {
      return order;
    }

    @Override public MenuItem setTitle(CharSequence title) {
      this.title = title;
      return this;
    }

    @Override public MenuItem setTitle(int title) {
      this.title = context.getText(title);
      return this;
    }

    @Override public CharSequence getTitle() {
      return title;
    }

    @Override public MenuItem setTitleCondensed(CharSequence title) {
      this.titleCondensed = title;
      return this;
    }

    @Override public CharSequence getTitleCondensed() {
      return titleCondensed;
    }

    @Override public MenuItem setIcon(Drawable icon) {
      this.icon = icon;
      return this;
    }

    @Override public MenuItem setIcon(int iconRes) {
      this.icon = context.getResources().getDrawable(iconRes);
      return this;
    }

    @Override public Drawable getIcon() {
      return icon;
    }

    @Override public MenuItem setIntent(Intent intent) {
      this.intent = intent;
      return this;
    }

    @Override public Intent getIntent() {
      return intent;
    }

    @Override public MenuItem setShortcut(char numericChar, char alphaChar) {
      this.numericChar = numericChar;
      this.alphaChar = alphaChar;
      return this;
    }

    @Override public MenuItem setNumericShortcut(char numericChar) {
      this.numericChar = numericChar;
      return this;
    }

    @Override public char getNumericShortcut() {
      return numericChar;
    }

    @Override public MenuItem setAlphabeticShortcut(char alphaChar) {
      this.alphaChar = alphaChar;
      return this;
    }

    @Override public char getAlphabeticShortcut() {
      return alphaChar;
    }

    @Override public MenuItem setCheckable(boolean checkable) {
      this.checkable = checkable;
      return this;
    }

    @Override public boolean isCheckable() {
      return checkable;
    }

    @Override public MenuItem setChecked(boolean checked) {
      if (checkable) {
        this.checked = checked;
      }
      return this;
    }

    @Override public boolean isChecked() {
      return checked;
    }

    @Override public MenuItem setVisible(boolean visible) {
      this.visible = visible;
      return this;
    }

    @Override public boolean isVisible() {
      return visible;
    }

    @Override public MenuItem setEnabled(boolean enabled) {
      this.enabled = enabled;
      return this;
    }

    @Override public boolean isEnabled() {
      return enabled;
    }

    @Override public boolean hasSubMenu() {
      return false;
    }

    @Override public SubMenu getSubMenu() {
      return null;
    }

    @Override
    public MenuItem setOnMenuItemClickListener(OnMenuItemClickListener menuItemClickListener) {
      this.menuItemClickListener = menuItemClickListener;
      return this;
    }

    @Override public ContextMenu.ContextMenuInfo getMenuInfo() {
      return null;
    }

    @Override public void setShowAsAction(int actionEnum) {
      this.actionEnum = actionEnum;
    }

    @Override public MenuItem setShowAsActionFlags(int actionEnum) {
      this.actionEnum = actionEnum;
      return this;
    }

    @Override public MenuItem setActionView(View view) {
      this.actionView = view;
      return this;
    }

    @Override public MenuItem setActionView(int resId) {
      this.actionView = LayoutInflater.from(context).inflate(resId, null);
      return this;
    }

    @Override public View getActionView() {
      return actionView;
    }

    @Override public MenuItem setActionProvider(ActionProvider actionProvider) {
      this.actionProvider = actionProvider;
      return this;
    }

    @Override public ActionProvider getActionProvider() {
      return actionProvider;
    }

    @Override public boolean expandActionView() {
      if (isActionViewExpanded) {
        return false;
      }

      if (actionExpandListener != null && !actionExpandListener.onMenuItemActionExpand(this)) {
        return false;
      }

      isActionViewExpanded = true;
      return true;
    }

    @Override public boolean collapseActionView() {
      if (!isActionViewExpanded) {
        return false;
      }

      if (actionExpandListener != null && !actionExpandListener.onMenuItemActionCollapse(this)) {
        return false;
      }

      isActionViewExpanded = false;
      return true;
    }

    @Override public boolean isActionViewExpanded() {
      return isActionViewExpanded;
    }

    @Override public MenuItem setOnActionExpandListener(OnActionExpandListener listener) {
      this.actionExpandListener = listener;
      return this;
    }
  }
}
