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

    int topLeft[] = new int[2];
    view.getLocationInWindow(topLeft);
    int x1 = topLeft[0] + paddingLeft;
    int y1 = topLeft[1] + paddingTop;
    int x2 = x1 + width - paddingLeft - paddingRight;
    int y2 = y1 + height - paddingTop - paddingBottom;

    float x = x1 + ((x2 - x1) * xPercent / 100f);
    float y = y1 + ((y2 - y1) * yPercent / 100f);

    long time = SystemClock.uptimeMillis();

    MotionEvent.PointerProperties pointerProperties = new MotionEvent.PointerProperties();
    pointerProperties.toolType = MotionEvent.TOOL_TYPE_UNKNOWN;

    MotionEvent.PointerCoords pointerCoords = new MotionEvent.PointerCoords();
    pointerCoords.setAxisValue(MotionEvent.AXIS_X, x);
    pointerCoords.setAxisValue(MotionEvent.AXIS_Y, y);

    return MotionEvent.obtain(time, time, action, 1,
        new MotionEvent.PointerProperties[] {pointerProperties},
        new MotionEvent.PointerCoords[]{pointerCoords}, 0, 0, 1.0f, 1.0f,
        0, 0, InputDevice.SOURCE_CLASS_POINTER, 0);
  }

  private MotionEventUtil() {
    throw new AssertionError("No instances.");
  }
}
