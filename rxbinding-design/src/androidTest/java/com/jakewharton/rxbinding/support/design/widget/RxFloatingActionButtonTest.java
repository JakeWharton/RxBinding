package com.jakewharton.rxbinding.support.design.widget;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.functions.Consumer;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class RxFloatingActionButtonTest {

  private final Context context = InstrumentationRegistry.getContext();
  private final FloatingActionButton fab = new FloatingActionButton(context);

  @Test
  @UiThreadTest
  public void visibility() throws Exception {
    fab.show();
    Consumer<? super Boolean> action = RxFloatingActionButton.fabVisibility(fab);
    action.accept(false);
    assertEquals(View.GONE, fab.getVisibility());
    action.accept(true);
    assertEquals(View.VISIBLE, fab.getVisibility());
  }
}
