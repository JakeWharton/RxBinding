package com.jakewharton.rxbinding3.widget;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import androidx.test.annotation.UiThreadTest;
import com.jakewharton.rxbinding3.RecordingObserver;
import org.junit.Test;

import static org.junit.Assert.assertSame;

public final class RxAdapterTest {
  private final TestAdapter adapter = new TestAdapter();

  @Test @UiThreadTest public void dataChanges() {
    RecordingObserver<Object> o = new RecordingObserver<>();
    RxAdapter.dataChanges(adapter).subscribe(o);
    assertSame(adapter, o.takeNext());

    adapter.notifyDataSetChanged();
    assertSame(adapter, o.takeNext());

    adapter.notifyDataSetChanged();
    assertSame(adapter, o.takeNext());

    o.dispose();
    adapter.notifyDataSetChanged();
    o.assertNoMoreEvents();
  }

  private static final class TestAdapter extends BaseAdapter {
    TestAdapter() {
    }

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
