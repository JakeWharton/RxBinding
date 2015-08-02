package com.jakewharton.rxbinding;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;

public final class MotionEventUtil {
  public static MotionEvent motionEventAtPosition(View view, int action, int position) {
    // NOTE: This method is not perfect. If you send touch events in a granular nature, you'll
    // see varying results of accuracy depending on the size of the jump.

    int paddingLeft = view.getPaddingLeft();
    int paddingRight = view.getPaddingRight();
    int paddingTop = view.getPaddingTop();
    int paddingBottom = view.getPaddingBottom();

    int width = view.getWidth();
    int height = view.getHeight();

    int topLeft[] = new int[2];
    view.getLocationInWindow(topLeft);
    int x1 = topLeft[0] + paddingLeft;
    int y1 = topLeft[1] + paddingTop;
    int x2 = x1 + width - paddingLeft - paddingRight;
    int y2 = y1 + height - paddingTop - paddingBottom;

    float x = x1 + ((x2 - x1) * position / 100f);
    float y = y1 + ((y2 - y1) / 2f);

    long time = SystemClock.uptimeMillis();
    return MotionEvent.obtain(time, time, action, x, y, 0);
  }

  private MotionEventUtil() {
    throw new AssertionError("No instances.");
  }
}
