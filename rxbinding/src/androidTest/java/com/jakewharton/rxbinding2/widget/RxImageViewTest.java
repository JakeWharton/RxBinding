package com.jakewharton.rxbinding2.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ImageView;
import com.jakewharton.rxbinding2.test.R;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by edwardwong on 12/10/2017.
 */
@RunWith(AndroidJUnit4.class)
public final class RxImageViewTest {
  @Rule public final UiThreadTestRule uiThread = new UiThreadTestRule();

  private final Context context = InstrumentationRegistry.getContext();
  private final ImageView view = new ImageView(context);

  @Test @UiThreadTest public void src() throws Exception {
    Drawable drawable = context.getDrawable(R.drawable.image);
    RxImageView.src(view).accept(drawable);
    assertEquals(drawable, view.getDrawable());
  }

  @Test @UiThreadTest public void srcRes() throws Exception {
    Drawable drawable = context.getDrawable(R.drawable.image);
    RxImageView.srcRes(view).accept(R.drawable.image);
    assertEquals(drawable.getConstantState(), view.getDrawable().getConstantState());
  }

  @Test @UiThreadTest public void srcBitmap() throws Exception {
    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_cross);
    RxImageView.srcBitmap(view).accept(bitmap);
    BitmapDrawable drawable = (BitmapDrawable) view.getDrawable();
    assertEquals(bitmap, drawable.getBitmap());
  }
}