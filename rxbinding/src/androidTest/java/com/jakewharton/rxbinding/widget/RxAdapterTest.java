package com.jakewharton.rxbinding.widget;

import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import rx.Subscription;
import com.jakewharton.rxbinding.RecordingObserver;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public final class RxAdapterTest {
  @Rule public final UiThreadTestRule uiThread = new UiThreadTestRule();

  private final TestAdapter adapter = new TestAdapter();

  @Test @UiThreadTest public void dataChanges() {
    RecordingObserver<Object> o = new RecordingObserver<>();
    Subscription subscription = RxAdapter.dataChanges(adapter).subscribe(o);
    assertThat(o.takeNext()).isSameAs(adapter);

    adapter.notifyDataSetChanged();
    assertThat(o.takeNext()).isSameAs(adapter);

    adapter.notifyDataSetChanged();
    assertThat(o.takeNext()).isSameAs(adapter);

    subscription.unsubscribe();
    adapter.notifyDataSetChanged();
    o.assertNoMoreEvents();
  }

  private static final class TestAdapter extends BaseAdapter {
    @Override public int getCount() {
      return 0;
    }

    @Override public Object getItem(int position) {
      return null;
    }

    @Override public long getItemId(int position) {
      return 0;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
      return null;
    }
  }
}
