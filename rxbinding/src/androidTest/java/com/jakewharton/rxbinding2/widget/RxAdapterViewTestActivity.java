package com.jakewharton.rxbinding2.widget;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class RxAdapterViewTestActivity extends Activity {
  Spinner spinner;
  ListView listView;

  List<String> values;
  ArrayAdapter<String> adapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    values = new ArrayList<>(Arrays.asList("One", "Two", "Three"));
    adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, values);

    spinner = new Spinner(this);
    spinner.setAdapter(adapter);

    listView = new ListView(this);
    listView.setAdapter(adapter);

    FrameLayout layout = new FrameLayout(this);
    layout.addView(spinner);
    layout.addView(listView);
    setContentView(layout);
  }
}
