package com.jakewharton.rxbinding.widget;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.Arrays;
import java.util.List;

public final class RxAutoCompleteTextViewTestActivity extends Activity {
  AutoCompleteTextView autoCompleteTextView;

  ArrayAdapter<String> adapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    List<String> values = Arrays.asList("One", "Two", "Three");
    adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, values);

    autoCompleteTextView = new AutoCompleteTextView(this);
    autoCompleteTextView.setAdapter(adapter);
    setContentView(autoCompleteTextView);
  }
}
