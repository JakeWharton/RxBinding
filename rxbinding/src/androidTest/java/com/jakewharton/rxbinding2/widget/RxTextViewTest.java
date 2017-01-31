package com.jakewharton.rxbinding2.widget;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;
import com.jakewharton.rxbinding2.test.R;
import com.jakewharton.rxbinding2.RecordingObserver;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.view.inputmethod.EditorInfo.IME_ACTION_GO;
import static android.view.inputmethod.EditorInfo.IME_ACTION_NEXT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

@RunWith(AndroidJUnit4.class)
public final class RxTextViewTest {
  @Rule public final UiThreadTestRule uiThread = new UiThreadTestRule();

  private final Context context = InstrumentationRegistry.getContext();
  private final TextView view = new TextView(context);

  @Test @UiThreadTest public void editorActions() {
    RecordingObserver<Integer> o = new RecordingObserver<>();
    RxTextView.editorActions(view).subscribe(o);
    o.assertNoMoreEvents();

    view.onEditorAction(IME_ACTION_GO);
    assertEquals(IME_ACTION_GO, o.takeNext().intValue());

    view.onEditorAction(IME_ACTION_NEXT);
    assertEquals(IME_ACTION_NEXT, o.takeNext().intValue());

    o.dispose();

    view.onEditorAction(IME_ACTION_GO);
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void editorActionEvents() {
    RecordingObserver<TextViewEditorActionEvent> o = new RecordingObserver<>();
    RxTextView.editorActionEvents(view).subscribe(o);
    o.assertNoMoreEvents();

    view.onEditorAction(IME_ACTION_GO);
    TextViewEditorActionEvent event1 = o.takeNext();
    assertSame(view, event1.view());
    assertEquals(IME_ACTION_GO, event1.actionId());
    assertNull(event1.keyEvent());

    view.onEditorAction(IME_ACTION_NEXT);
    TextViewEditorActionEvent event2 = o.takeNext();
    assertSame(view, event2.view());
    assertEquals(IME_ACTION_NEXT, event2.actionId());
    assertNull(event2.keyEvent()); // TODO figure out a user event?

    o.dispose();

    view.onEditorAction(IME_ACTION_GO);
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void textChanges() {
    view.setText("Initial");

    RecordingObserver<CharSequence> o = new RecordingObserver<>();
    RxTextView.textChanges(view).subscribe(o);
    assertEquals("Initial", o.takeNext().toString());

    view.setText("H");
    assertEquals("H", o.takeNext().toString());
    view.setText("He");
    assertEquals("He", o.takeNext().toString());

    view.setText(null); // Internally coerced to empty string.
    assertEquals("", o.takeNext().toString());

    o.dispose();

    view.setText("Silent");
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void textChangeEvents() {
    view.setText("Initial");

    RecordingObserver<TextViewTextChangeEvent> o = new RecordingObserver<>();
    RxTextView.textChangeEvents(view).subscribe(o);
    TextViewTextChangeEvent event0 = o.takeNext();
    assertSame(view, event0.view());
    assertEquals("Initial", event0.text().toString());
    assertEquals(0, event0.start());
    assertEquals(0, event0.before());
    assertEquals(0, event0.count());

    view.setText("H");
    TextViewTextChangeEvent event1 = o.takeNext();
    assertSame(view, event1.view());
    assertEquals("H", event1.text().toString());
    assertEquals(0, event1.start());
    assertEquals(7, event1.before());
    assertEquals(1, event1.count());

    view.setText("He");
    TextViewTextChangeEvent event2 = o.takeNext();
    assertSame(view, event2.view());
    assertEquals("He", event2.text().toString());
    assertEquals(0, event2.start());
    assertEquals(1, event2.before());
    assertEquals(2, event2.count());

    o.dispose();

    view.setText("Silent");
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void beforeTextChangeEvents() {
    view.setText("Initial");

    RecordingObserver<TextViewBeforeTextChangeEvent> o = new RecordingObserver<>();
    RxTextView.beforeTextChangeEvents(view).subscribe(o);
    TextViewBeforeTextChangeEvent event0 = o.takeNext();
    assertSame(view, event0.view());
    assertEquals("Initial", event0.text().toString());
    assertEquals(0, event0.start());
    assertEquals(0, event0.count());
    assertEquals(0, event0.after());

    view.setText("H");
    TextViewBeforeTextChangeEvent event1 = o.takeNext();
    assertSame(view, event1.view());
    assertEquals("Initial", event1.text().toString());
    assertEquals(0, event1.start());
    assertEquals(7, event1.count());
    assertEquals(1, event1.after());

    view.setText("He");
    TextViewBeforeTextChangeEvent event2 = o.takeNext();
    assertSame(view, event2.view());
    assertEquals("H", event2.text().toString());
    assertEquals(0, event2.start());
    assertEquals(1, event2.count());
    assertEquals(2, event2.after());

    o.dispose();

    view.setText("Silent");
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void afterTextChangeEvents() {
    view.setText("Initial");

    RecordingObserver<TextViewAfterTextChangeEvent> o = new RecordingObserver<>();
    RxTextView.afterTextChangeEvents(view).subscribe(o);
    TextViewAfterTextChangeEvent event0 = o.takeNext();
    assertSame(view, event0.view());
    assertEquals(null, event0.editable());

    view.setText("H");
    TextViewAfterTextChangeEvent event1 = o.takeNext();
    assertSame(view, event1.view());
    assertEquals("H", event1.editable().toString());

    view.setText("He");
    TextViewAfterTextChangeEvent event2 = o.takeNext();
    assertSame(view, event2.view());
    assertEquals("He", event2.editable().toString());

    o.dispose();

    view.setText("Silent");
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void text() throws Exception {
    RxTextView.text(view).accept("Hey");
    assertEquals("Hey", view.getText().toString());
  }

  @Test @UiThreadTest public void textRes() throws Exception {
    RxTextView.textRes(view).accept(R.string.hey);
    assertEquals("Hey", view.getText().toString());
  }

  @Test @UiThreadTest public void error() throws Exception {
    RxTextView.error(view).accept("Ouch");
    assertEquals("Ouch", view.getError().toString());
  }

  @Test @UiThreadTest public void errorRes() throws Exception {
    RxTextView.errorRes(view).accept(R.string.ouch);
    assertEquals("Ouch", view.getError().toString());
  }

  @Test @UiThreadTest public void hint() throws Exception {
    RxTextView.hint(view).accept("Your name here");
    assertEquals("Your name here", view.getHint().toString());
  }

  @Test @UiThreadTest public void hintRes() throws Exception {
    RxTextView.hintRes(view).accept(R.string.hint);
    assertEquals("Your name here", view.getHint().toString());
  }

  @Test @UiThreadTest public void color() throws Exception {
    RxTextView.color(view).accept(0x3F51B5);
    assertEquals(0x3F51B5, view.getCurrentTextColor());
  }
}
