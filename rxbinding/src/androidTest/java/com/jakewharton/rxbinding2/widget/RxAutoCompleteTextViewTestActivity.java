package com.jakewharton.rxbinding2.widget;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import com.jakewharton.rxbinding.test.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public final class RxAutoCompleteTextViewTestActivity extends Activity {
  AutoCompleteTextView autoCompleteTextView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    LinearLayout layout = new LinearLayout(this);
    setContentView(layout);

    autoCompleteTextView = new AutoCompleteTextView(this);
    autoCompleteTextView.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
    autoCompleteTextView.setId(R.id.auto_complete);
    layout.addView(autoCompleteTextView);
  }
}
