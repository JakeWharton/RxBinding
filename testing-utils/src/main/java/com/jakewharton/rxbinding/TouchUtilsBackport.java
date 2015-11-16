package com.jakewharton.rxbinding;

import android.app.Instrumentation;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

public final class TouchUtilsBackport {
  /**
   * Simulate touching a view and dragging it to a specified location.
   *
   * @param v The view that should be dragged
   * @param gravity Which part of the view to use for the initial down event. A combination of
   * (TOP, CENTER_VERTICAL, BOTTOM) and (LEFT, CENTER_HORIZONTAL, RIGHT)
   * @param toX Final location of the view after dragging
   * @param toY Final location of the view after dragging
   * @return distance in pixels covered by the drag
   */
  public static int dragViewTo(View v, int gravity, int toX, int toY) {
    int[] xy = new int[2];

    getStartLocation(v, gravity, xy);

    int fromX = xy[0];
    int fromY = xy[1];

    int deltaX = fromX - toX;
    int deltaY = fromY - toY;

    int distance = (int) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    drag(fromX, toX, fromY, toY, distance);

    return distance;
  }

  /**
   * Simulate touching a specific location and dragging to a new location.
   *
   * @param fromX X coordinate of the initial touch, in screen coordinates
   * @param toX Xcoordinate of the drag destination, in screen coordinates
   * @param fromY X coordinate of the initial touch, in screen coordinates
   * @param toY Y coordinate of the drag destination, in screen coordinates
   * @param stepCount How many move steps to include in the drag
   */
  public static void drag(float fromX, float toX, float fromY, float toY, int stepCount) {
    Instrumentation inst = InstrumentationRegistry.getInstrumentation();

    long downTime = SystemClock.uptimeMillis();
    long eventTime = SystemClock.uptimeMillis();

    float y = fromY;
    float x = fromX;

    float yStep = (toY - fromY) / stepCount;
    float xStep = (toX - fromX) / stepCount;

    MotionEvent event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, x, y, 0);
    inst.sendPointerSync(event);
    for (int i = 0; i < stepCount; ++i) {
      y += yStep;
      x += xStep;
      eventTime = SystemClock.uptimeMillis();
      event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, x, y, 0);
      inst.sendPointerSync(event);
    }

    eventTime = SystemClock.uptimeMillis();
    event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, x, y, 0);
    inst.sendPointerSync(event);
    inst.waitForIdleSync();
  }

  /**
   * Get the location of a view. Use the gravity param to specify which part of the view to
   * return.
   *
   * @param v View to find
   * @param gravity A combination of (TOP, CENTER_VERTICAL, BOTTOM) and (LEFT, CENTER_HORIZONTAL,
   * RIGHT)
   * @param xy Result
   */
  private static void getStartLocation(View v, int gravity, int[] xy) {
    v.getLocationOnScreen(xy);

    int viewWidth = v.getWidth();
    int viewHeight = v.getHeight();

    switch (gravity & Gravity.VERTICAL_GRAVITY_MASK) {
      case Gravity.TOP:
        break;
      case Gravity.CENTER_VERTICAL:
        xy[1] += viewHeight / 2;
        break;
      case Gravity.BOTTOM:
        xy[1] += viewHeight - 1;
        break;
      default:
        // Same as top -- do nothing
    }

    switch (gravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
      case Gravity.LEFT:
        break;
      case Gravity.CENTER_HORIZONTAL:
        xy[0] += viewWidth / 2;
        break;
      case Gravity.RIGHT:
        xy[0] += viewWidth - 1;
        break;
      default:
        // Same as left -- do nothing
    }
  }

  private TouchUtilsBackport() {
    throw new AssertionError("No instances");
  }
}
