package com.jakewharton.rxbinding.widget;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public final class RxAbsListViewTestActivity extends Activity {
  ListView listView;

  List<String> values;
  ArrayAdapter<String> adapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    values = createValues(100);
    adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, values);

    listView = new ListView(this);
    listView.setAdapter(adapter);

    FrameLayout layout = new FrameLayout(this);
    layout.addView(listView);
    setContentView(layout);
  }

  private static List<String> createValues(int count) {
    final List<String> values = new ArrayList<String>(count);
    for (int i = 0; i < count; i++) {
      values.add(String.valueOf(i));
    }
    return values;
  }
}
