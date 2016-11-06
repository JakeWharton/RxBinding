package com.jakewharton.rxbinding.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

public final class RxActivityResultTestActivity extends Activity {

  public static final String KEY_IDENTIFIER = "KEY_IDENTIFIER";

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    FrameLayout layout = new FrameLayout(this);
    TextView textView = new TextView(this);
    textView.setText(getIntent().getStringExtra(KEY_IDENTIFIER));
    layout.addView(textView);
    setContentView(layout);
  }

  public static Intent createActivityStartIntent(Context context, String identifier) {
    Intent intent = new Intent(context, RxActivityResultTestActivity.class);
    intent.putExtra(KEY_IDENTIFIER, identifier);
    return intent;
  }

}