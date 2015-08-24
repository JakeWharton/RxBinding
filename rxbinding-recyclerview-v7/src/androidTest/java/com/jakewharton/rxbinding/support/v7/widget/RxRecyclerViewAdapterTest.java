package com.jakewharton.rxbinding.support.v7.widget;

import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;
import com.jakewharton.rxbinding.RecordingObserver;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import rx.Subscription;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public final class RxRecyclerViewAdapterTest {
  @Rule public final UiThreadTestRule uiThread = new UiThreadTestRule();

  private final TestRecyclerAdapter adapter = new TestRecyclerAdapter();

  @Test @UiThreadTest public void dataChanges() {
    RecordingObserver<Object> o = new RecordingObserver<>();
    Subscription subscription = RxRecyclerViewAdapter.dataChanges(adapter).subscribe(o);
    assertThat(o.takeNext()).isSameAs(adapter);

    adapter.notifyDataSetChanged();
    assertThat(o.takeNext()).isSameAs(adapter);

    adapter.notifyDataSetChanged();
    assertThat(o.takeNext()).isSameAs(adapter);

    subscription.unsubscribe();
    adapter.notifyDataSetChanged();
    o.assertNoMoreEvents();
  }

  private static final class TestRecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {
    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      return null;
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
    }

    @Override public int getItemCount() {
      return 0;
    }
  }
}
