package com.jakewharton.rxbinding2.support.v4.widget;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.widget.FrameLayout;

import static android.view.Gravity.RIGHT;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public final class RxDrawerLayoutTestActivity extends Activity {
  DrawerLayout drawerLayout;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    drawerLayout = new DrawerLayout(this);
    drawerLayout.setId(android.R.id.primary);

    FrameLayout main = new FrameLayout(this);
    LayoutParams mainParams = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
    drawerLayout.addView(main, mainParams);

    FrameLayout drawer = new FrameLayout(this);
    drawer.setBackgroundColor(Color.WHITE);
    LayoutParams drawerParams = new LayoutParams(300, MATCH_PARENT, RIGHT);
    drawerLayout.addView(drawer, drawerParams);

    setContentView(drawerLayout);
  }
}
