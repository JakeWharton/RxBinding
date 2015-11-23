package com.jakewharton.rxbinding;

import android.os.SystemClock;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.View;

public final class MotionEventUtil {
  public static MotionEvent motionEventAtPosition(View view, int action, int xPercent,
      int yPercent) {
    // NOTE: This method is not perfect. If you send touch events in a granular nature, you'll
    // see varying results of accuracy depending on the size of the jump.

    int paddingLeft = view.getPaddingLeft();
    int paddingRight = view.getPaddingRight();
    int paddingTop = view.getPaddingTop();
    int paddingBottom = view.getPaddingBottom();

    int width = view.getWidth();
    int height = view.getHeight();

    int[] topLeft = new int[2];
    view.getLocationInWindow(topLeft);
    int x1 = topLeft[0] + paddingLeft;
    int y1 = topLeft[1] + paddingTop;
    int x2 = x1 + width - paddingLeft - paddingRight;
    int y2 = y1 + height - paddingTop - paddingBottom;

    float x = x1 + ((x2 - x1) * xPercent / 100f);
    float y = y1 + ((y2 - y1) * yPercent / 100f);

    long time = SystemClock.uptimeMillis();
    return MotionEvent.obtain(time, time, action, x, y, 0);
  }

  public static MotionEvent hoverMotionEventAtPosition(View view, int action, int xPercent,
      int yPercent) {
    MotionEvent ev = motionEventAtPosition(view, action, xPercent, yPercent);

    MotionEvent.PointerProperties[] pointerProperties = new MotionEvent.PointerProperties[1];
    pointerProperties[0] = new MotionEvent.PointerProperties();

    MotionEvent.PointerCoords[] pointerCoords = new MotionEvent.PointerCoords[1];
    pointerCoords[0] = new MotionEvent.PointerCoords();
    pointerCoords[0].x = ev.getX();
    pointerCoords[0].y = ev.getY();

    return MotionEvent.obtain(ev.getDownTime(), ev.getEventTime(),
        ev.getAction(), 1, pointerProperties, pointerCoords, ev.getMetaState(), 0,
        ev.getXPrecision(), ev.getYPrecision(), ev.getDeviceId(), ev.getEdgeFlags(),
        InputDevice.SOURCE_CLASS_POINTER, ev.getFlags());
  }

  private MotionEventUtil() {
    throw new AssertionError("No instances.");
  }
}
