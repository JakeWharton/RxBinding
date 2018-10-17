package com.jakewharton.rxbinding3.recyclerview;

import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import androidx.test.annotation.UiThreadTest;
import com.jakewharton.rxbinding3.RecordingObserver;
import org.junit.Test;

import static org.junit.Assert.assertSame;

public final class RxRecyclerViewAdapterTest {
  private final TestRecyclerAdapter adapter = new TestRecyclerAdapter();

  @Test @UiThreadTest public void dataChanges() {
    RecordingObserver<Object> o = new RecordingObserver<>();
    RxRecyclerViewAdapter.dataChanges(adapter).subscribe(o);
    assertSame(adapter, o.takeNext());

    adapter.notifyDataSetChanged();
    assertSame(adapter, o.takeNext());

    adapter.notifyDataSetChanged();
    assertSame(adapter, o.takeNext());

    o.dispose();
    adapter.notifyDataSetChanged();
    o.assertNoMoreEvents();
  }

  private static final class TestRecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {
    TestRecyclerAdapter() {
    }

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
