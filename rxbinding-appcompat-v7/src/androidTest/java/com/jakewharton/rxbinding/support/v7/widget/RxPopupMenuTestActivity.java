package com.jakewharton.rxbinding.support.v7.widget;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.View;

public final class RxPopupMenuTestActivity extends Activity {

  PopupMenu popupMenu;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    View anchor = new View(this);
    setContentView(anchor);
    popupMenu = new PopupMenu(this, anchor);
  }
}
