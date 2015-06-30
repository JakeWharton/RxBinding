package com.jakewharton.rxbinding.widget;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;
import com.jakewharton.rxbinding.test.R;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import rx.Subscription;
import com.jakewharton.rxbinding.RecordingObserver;

import static android.view.inputmethod.EditorInfo.IME_ACTION_GO;
import static android.view.inputmethod.EditorInfo.IME_ACTION_NEXT;
import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public final class RxTextViewTest {
  @Rule public final UiThreadTestRule uiThread = new UiThreadTestRule();

  private final Context context = InstrumentationRegistry.getContext();
  private final TextView view = new TextView(context);

  @Test @UiThreadTest public void editorActions() {
    RecordingObserver<Integer> o = new RecordingObserver<>();
    Subscription subscription = RxTextView.editorActions(view).subscribe(o);
    o.assertNoMoreEvents();

    view.onEditorAction(IME_ACTION_GO);
    assertThat(o.takeNext()).isEqualTo(IME_ACTION_GO);

    view.onEditorAction(IME_ACTION_NEXT);
    assertThat(o.takeNext()).isEqualTo(IME_ACTION_NEXT);

    subscription.unsubscribe();

    view.onEditorAction(IME_ACTION_GO);
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void editorActionEvents() {
    RecordingObserver<TextViewEditorActionEvent> o = new RecordingObserver<>();
    Subscription subscription = RxTextView.editorActionEvents(view).subscribe(o);
    o.assertNoMoreEvents();

    view.onEditorAction(IME_ACTION_GO);
    TextViewEditorActionEvent event1 = o.takeNext();
    assertThat(event1.view()).isSameAs(view);
    assertThat(event1.actionId()).isEqualTo(IME_ACTION_GO);
    assertThat(event1.keyEvent()).isNull();

    view.onEditorAction(IME_ACTION_NEXT);
    TextViewEditorActionEvent event2 = o.takeNext();
    assertThat(event2.view()).isSameAs(view);
    assertThat(event2.actionId()).isEqualTo(IME_ACTION_NEXT);
    assertThat(event2.keyEvent()).isNull(); // TODO figure out a user event?

    subscription.unsubscribe();

    view.onEditorAction(IME_ACTION_GO);
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void textChanges() {
    view.setText("Initial");

    RecordingObserver<CharSequence> o = new RecordingObserver<>();
    Subscription subscription = RxTextView.textChanges(view).subscribe(o);
    assertThat(o.takeNext().toString()).isEqualTo("Initial");

    view.setText("H");
    assertThat(o.takeNext().toString()).isEqualTo("H");
    view.setText("He");
    assertThat(o.takeNext().toString()).isEqualTo("He");

    view.setText(null); // Internally coerced to empty string.
    assertThat(o.takeNext().toString()).isEqualTo("");

    subscription.unsubscribe();

    view.setText("Silent");
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void textChangeEvents() {
    view.setText("Initial");

    RecordingObserver<TextViewTextChangeEvent> o = new RecordingObserver<>();
    Subscription subscription = RxTextView.textChangeEvents(view).subscribe(o);
    TextViewTextChangeEvent event0 = o.takeNext();
    assertThat(event0.view()).isSameAs(view);
    assertThat(event0.text().toString()).isEqualTo("Initial");
    assertThat(event0.start()).isEqualTo(0);
    assertThat(event0.before()).isEqualTo(0);
    assertThat(event0.count()).isEqualTo(0);

    view.setText("H");
    TextViewTextChangeEvent event1 = o.takeNext();
    assertThat(event1.view()).isSameAs(view);
    assertThat(event1.text().toString()).isEqualTo("H");
    assertThat(event1.start()).isEqualTo(0);
    assertThat(event1.before()).isEqualTo(7);
    assertThat(event1.count()).isEqualTo(1);

    view.setText("He");
    TextViewTextChangeEvent event2 = o.takeNext();
    assertThat(event2.view()).isSameAs(view);
    assertThat(event2.text().toString()).isEqualTo("He");
    assertThat(event2.start()).isEqualTo(0);
    assertThat(event2.before()).isEqualTo(1);
    assertThat(event2.count()).isEqualTo(2);

    subscription.unsubscribe();

    view.setText("Silent");
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void text() {
    RxTextView.text(view).call("Hey");
    assertThat(view.getText().toString()).isEqualTo("Hey");
  }

  @Test @UiThreadTest public void textRes() {
    RxTextView.textRes(view).call(R.string.hey);
    assertThat(view.getText().toString()).isEqualTo("Hey");
  }
}
