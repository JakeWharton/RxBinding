package com.jakewharton.rxbinding2.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.filters.SdkSuppress;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import com.jakewharton.rxbinding2.RecordingObserver;
import com.jakewharton.rxbinding2.internal.Functions;
import io.reactivex.functions.Consumer;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.os.Build.VERSION_CODES.JELLY_BEAN;
import static android.os.Build.VERSION_CODES.M;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_HOVER_ENTER;
import static android.view.MotionEvent.ACTION_HOVER_EXIT;
import static android.view.MotionEvent.ACTION_HOVER_MOVE;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;
import static com.jakewharton.rxbinding.MotionEventUtil.hoverMotionEventAtPosition;
import static com.jakewharton.rxbinding.MotionEventUtil.motionEventAtPosition;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public final class RxViewTest {
  @Rule public final UiThreadTestRule uiThread = new UiThreadTestRule();

  private final Context context = InstrumentationRegistry.getContext();
  private final View view = new View(context);

  @Test @UiThreadTest public void clicks() {
    RecordingObserver<Object> o = new RecordingObserver<>();
    RxView.clicks(view).subscribe(o);
    o.assertNoMoreEvents(); // No initial value.

    view.performClick();
    assertNotNull(o.takeNext());

    view.performClick();
    assertNotNull(o.takeNext());

    o.dispose();

    view.performClick();
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void drags() {
    //RecordingObserver<ViewClickEvent> o = new RecordingObserver<>();
    //RxView.clickEvents(view).subscribe(o);
    //o.assertNoMoreEvents(); // No initial value.
    //
    //clock.advance(1, SECONDS);
    //view.performClick();
    //assertEquals(ViewClickEvent.create(view, 1000), o.takeNext());
    //
    //clock.advance(1, SECONDS);
    //view.performClick();
    //assertEquals(ViewClickEvent.create(view, 2000), o.takeNext());
    //
    //o.dispose();
    //
    //clock.advance(1, SECONDS);
    //view.performClick();
    //o.assertNoMoreEvents();
  }

  @TargetApi(JELLY_BEAN)
  @SdkSuppress(minSdkVersion = JELLY_BEAN)
  @Test @UiThreadTest public void drawEvents() {
    RecordingObserver<Object> o = new RecordingObserver<>();
    RxView.draws(view).subscribe(o);
    o.assertNoMoreEvents(); // No initial value.

    view.getViewTreeObserver().dispatchOnDraw();
    assertNotNull(o.takeNext());

    o.dispose();

    view.getViewTreeObserver().dispatchOnDraw();
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void focusChanges() {
    // We need a parent which can take focus from our view when it attempts to clear.
    LinearLayout parent = new LinearLayout(context);
    parent.setFocusable(true);
    parent.addView(view);

    view.setFocusable(true);

    RecordingObserver<Boolean> o = new RecordingObserver<>();
    RxView.focusChanges(view).subscribe(o);
    assertFalse(o.takeNext());

    view.requestFocus();
    assertTrue(o.takeNext());

    view.clearFocus();
    assertFalse(o.takeNext());

    o.dispose();

    view.requestFocus();
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void globalLayouts() {
    RecordingObserver<Object> o = new RecordingObserver<>();
    RxView.globalLayouts(view).subscribe(o);
    o.assertNoMoreEvents(); // No initial value.

    view.getViewTreeObserver().dispatchOnGlobalLayout();
    assertNotNull(o.takeNext());

    o.dispose();
    view.getViewTreeObserver().dispatchOnGlobalLayout();

    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void hovers() {
    RecordingObserver<MotionEvent> o = new RecordingObserver<>();
    RxView.hovers(view).subscribe(o);
    o.assertNoMoreEvents();

    view.dispatchGenericMotionEvent(hoverMotionEventAtPosition(view, ACTION_HOVER_ENTER, 0, 50));
    MotionEvent event1 = o.takeNext();
    assertEquals(ACTION_HOVER_ENTER, event1.getAction());

    view.dispatchGenericMotionEvent(hoverMotionEventAtPosition(view, ACTION_HOVER_MOVE, 1, 50));
    MotionEvent event2 = o.takeNext();
    assertEquals(ACTION_HOVER_MOVE, event2.getAction());

    o.dispose();

    view.dispatchGenericMotionEvent(hoverMotionEventAtPosition(view, ACTION_HOVER_EXIT, 1, 50));
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void layoutChanges() {
    RecordingObserver<Object> o = new RecordingObserver<>();
    RxView.layoutChanges(view).subscribe(o);
    o.assertNoMoreEvents();

    view.layout(view.getLeft() - 5, view.getTop() - 5, view.getRight(), view.getBottom());
    assertNotNull(o.takeNext());

    view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
    o.assertNoMoreEvents();

    o.dispose();
    view.layout(view.getLeft() - 5, view.getTop() - 5, view.getRight(), view.getBottom());
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void layoutChangeEvents() {
    RecordingObserver<ViewLayoutChangeEvent> o = new RecordingObserver<>();
    RxView.layoutChangeEvents(view).subscribe(o);
    o.assertNoMoreEvents();

    view.layout(view.getLeft() - 5, view.getTop() - 5, view.getRight(), view.getBottom());
    ViewLayoutChangeEvent event1 = o.takeNext();
    assertSame(view, event1.view());
    assertNotSame(event1.oldLeft(), event1.left());
    assertSame(event1.oldRight(), event1.right());

    view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
    o.assertNoMoreEvents();

    o.dispose();
    view.layout(view.getLeft() - 5, view.getTop() - 5, view.getRight(), view.getBottom());
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void longClicks() {
    // We need a parent because long presses delegate to the parent.
    LinearLayout parent = new LinearLayout(context) {
      @Override public boolean showContextMenuForChild(View originalView) {
        return true;
      }
    };
    parent.addView(view);

    RecordingObserver<Object> o = new RecordingObserver<>();
    RxView.longClicks(view).subscribe(o);
    o.assertNoMoreEvents(); // No initial value.

    view.performLongClick();
    assertNotNull(o.takeNext());

    view.performLongClick();
    assertNotNull(o.takeNext());

    o.dispose();

    view.performLongClick();
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void preDrawEvents() {
    RecordingObserver<Object> o = new RecordingObserver<>();
    RxView.preDraws(view, Functions.CALLABLE_ALWAYS_TRUE).subscribe(o);
    o.assertNoMoreEvents(); // No initial value.

    view.getViewTreeObserver().dispatchOnPreDraw();
    assertNotNull(o.takeNext());

    o.dispose();

    view.getViewTreeObserver().dispatchOnPreDraw();
    o.assertNoMoreEvents();
  }

  @TargetApi(M)
  @SdkSuppress(minSdkVersion = M)
  @Test @UiThreadTest public void scrollChangeEvents() {
    RecordingObserver<ViewScrollChangeEvent> o = new RecordingObserver<>();
    RxView.scrollChangeEvents(view).subscribe(o);
    o.assertNoMoreEvents();

    view.scrollTo(1, 1);
    ViewScrollChangeEvent event0 = o.takeNext();
    assertSame(view, event0.view());
    assertEquals(1, event0.scrollX());
    assertEquals(1, event0.scrollY());
    assertEquals(0, event0.oldScrollX());
    assertEquals(0, event0.oldScrollY());

    view.scrollTo(2, 2);
    ViewScrollChangeEvent event1 = o.takeNext();
    assertSame(view, event1.view());
    assertEquals(2, event1.scrollX());
    assertEquals(2, event1.scrollY());
    assertEquals(1, event1.oldScrollX());
    assertEquals(1, event1.oldScrollY());

    o.dispose();
    view.scrollTo(3, 3);
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void touches() {
    RecordingObserver<MotionEvent> o = new RecordingObserver<>();
    RxView.touches(view).subscribe(o);
    o.assertNoMoreEvents();

    view.dispatchTouchEvent(motionEventAtPosition(view, ACTION_DOWN, 0, 50));
    MotionEvent event1 = o.takeNext();
    assertEquals(ACTION_DOWN, event1.getAction());

    view.dispatchTouchEvent(motionEventAtPosition(view, ACTION_MOVE, 1, 50));
    MotionEvent event2 = o.takeNext();
    assertEquals(ACTION_MOVE, event2.getAction());

    o.dispose();

    view.dispatchTouchEvent(motionEventAtPosition(view, ACTION_UP, 1, 50));
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void keys() {
    RecordingObserver<KeyEvent> o = new RecordingObserver<>();
    RxView.keys(view).subscribe(o);
    o.assertNoMoreEvents();

    view.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_R));
    KeyEvent event1 = o.takeNext();
    assertEquals(KeyEvent.ACTION_DOWN, event1.getAction());

    view.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_H));
    KeyEvent event2 = o.takeNext();
    assertEquals(KeyEvent.KEYCODE_H, event2.getKeyCode());

    o.dispose();

    view.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_S));
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void activated() throws Exception {
    view.setActivated(true);
    Consumer<? super Boolean> action = RxView.activated(view);
    action.accept(false);
    assertFalse(view.isActivated());
    action.accept(true);
    assertTrue(view.isActivated());
  }

  @Test @UiThreadTest public void clickable() throws Exception {
    view.setClickable(true);
    Consumer<? super Boolean> action = RxView.clickable(view);
    action.accept(false);
    assertFalse(view.isClickable());
    action.accept(true);
    assertTrue(view.isClickable());
  }

  @Test @UiThreadTest public void enabled() throws Exception {
    view.setEnabled(true);
    Consumer<? super Boolean> action = RxView.enabled(view);
    action.accept(false);
    assertFalse(view.isEnabled());
    action.accept(true);
    assertTrue(view.isEnabled());
  }

  @Test @UiThreadTest public void pressed() throws Exception {
    view.setPressed(true);
    Consumer<? super Boolean> action = RxView.pressed(view);
    action.accept(false);
    assertFalse(view.isPressed());
    action.accept(true);
    assertTrue(view.isPressed());
  }

  @Test @UiThreadTest public void selected() throws Exception {
    view.setSelected(true);
    Consumer<? super Boolean> action = RxView.selected(view);
    action.accept(false);
    assertFalse(view.isSelected());
    action.accept(true);
    assertTrue(view.isSelected());
  }

  @Test @UiThreadTest public void visibility() throws Exception {
    view.setVisibility(View.VISIBLE);
    Consumer<? super Boolean> action = RxView.visibility(view);
    action.accept(false);
    assertEquals(View.GONE, view.getVisibility());
    action.accept(true);
    assertEquals(View.VISIBLE, view.getVisibility());
  }

  @Test @UiThreadTest public void visibilityCustomFalse() throws Exception {
    view.setVisibility(View.VISIBLE);
    Consumer<? super Boolean> action = RxView.visibility(view, View.INVISIBLE);
    action.accept(false);
    assertEquals(View.INVISIBLE, view.getVisibility());
    action.accept(true);
    assertEquals(View.VISIBLE, view.getVisibility());
  }

  @SuppressWarnings("ResourceType") @Test @UiThreadTest public void setVisibilityCustomFalseToVisibleThrows() {
    try {
      RxView.visibility(view, View.VISIBLE);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Setting visibility to VISIBLE when false would have no effect.", e.getMessage());
    }
  }
}
