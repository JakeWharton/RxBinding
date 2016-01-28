package com.jakewharton.rxbinding.widget;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.PopupMenu;

public final class RxPopupMenuTestActivity extends Activity {

  PopupMenu popupMenu;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    View anchor = new View(this);
    setContentView(anchor);
    popupMenu = new PopupMenu(this, anchor);
  }
}
