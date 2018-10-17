package com.jakewharton.rxbinding3.widget;

import android.content.Context;
import android.widget.TextView;
import androidx.test.InstrumentationRegistry;
import androidx.test.annotation.UiThreadTest;
import com.jakewharton.rxbinding3.RecordingObserver;
import org.junit.Test;

import static android.view.inputmethod.EditorInfo.IME_ACTION_GO;
import static android.view.inputmethod.EditorInfo.IME_ACTION_NEXT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public final class RxTextViewTest {
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
    assertSame(view, event1.getView());
    assertEquals(IME_ACTION_GO, event1.getActionId());
    assertNull(event1.getKeyEvent());

    view.onEditorAction(IME_ACTION_NEXT);
    TextViewEditorActionEvent event2 = o.takeNext();
    assertSame(view, event2.getView());
    assertEquals(IME_ACTION_NEXT, event2.getActionId());
    assertNull(event2.getKeyEvent()); // TODO figure out a user event?

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
    assertSame(view, event0.getView());
    assertEquals("Initial", event0.getText().toString());
    assertEquals(0, event0.getStart());
    assertEquals(0, event0.getBefore());
    assertEquals(0, event0.getCount());

    view.setText("H");
    TextViewTextChangeEvent event1 = o.takeNext();
    assertSame(view, event1.getView());
    assertEquals("H", event1.getText().toString());
    assertEquals(0, event1.getStart());
    assertEquals(7, event1.getBefore());
    assertEquals(1, event1.getCount());

    view.setText("He");
    TextViewTextChangeEvent event2 = o.takeNext();
    assertSame(view, event2.getView());
    assertEquals("He", event2.getText().toString());
    assertEquals(0, event2.getStart());
    assertEquals(1, event2.getBefore());
    assertEquals(2, event2.getCount());

    o.dispose();

    view.setText("Silent");
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void beforeTextChangeEvents() {
    view.setText("Initial");

    RecordingObserver<TextViewBeforeTextChangeEvent> o = new RecordingObserver<>();
    RxTextView.beforeTextChangeEvents(view).subscribe(o);
    TextViewBeforeTextChangeEvent event0 = o.takeNext();
    assertSame(view, event0.getView());
    assertEquals("Initial", event0.getText().toString());
    assertEquals(0, event0.getStart());
    assertEquals(0, event0.getCount());
    assertEquals(0, event0.getAfter());

    view.setText("H");
    TextViewBeforeTextChangeEvent event1 = o.takeNext();
    assertSame(view, event1.getView());
    assertEquals("Initial", event1.getText().toString());
    assertEquals(0, event1.getStart());
    assertEquals(7, event1.getCount());
    assertEquals(1, event1.getAfter());

    view.setText("He");
    TextViewBeforeTextChangeEvent event2 = o.takeNext();
    assertSame(view, event2.getView());
    assertEquals("H", event2.getText().toString());
    assertEquals(0, event2.getStart());
    assertEquals(1, event2.getCount());
    assertEquals(2, event2.getAfter());

    o.dispose();

    view.setText("Silent");
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void afterTextChangeEvents() {
    view.setText("Initial");

    RecordingObserver<TextViewAfterTextChangeEvent> o = new RecordingObserver<>();
    RxTextView.afterTextChangeEvents(view).subscribe(o);
    TextViewAfterTextChangeEvent event0 = o.takeNext();
    assertSame(view, event0.getView());
    assertEquals(null, event0.getEditable());

    view.setText("H");
    TextViewAfterTextChangeEvent event1 = o.takeNext();
    assertSame(view, event1.getView());
    assertEquals("H", event1.getEditable().toString());

    view.setText("He");
    TextViewAfterTextChangeEvent event2 = o.takeNext();
    assertSame(view, event2.getView());
    assertEquals("He", event2.getEditable().toString());

    o.dispose();

    view.setText("Silent");
    o.assertNoMoreEvents();
  }
}
