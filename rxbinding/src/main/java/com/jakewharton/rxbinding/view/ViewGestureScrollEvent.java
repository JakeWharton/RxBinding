package com.jakewharton.rxbinding.view;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;

public final class ViewGestureScrollEvent extends ViewEvent<View> {
    @CheckResult @NonNull
    public static ViewGestureScrollEvent create(@NonNull View view, @NonNull MotionEvent e1,
            @NonNull MotionEvent e2, float distanceX, float distanceY) {
        return new ViewGestureScrollEvent(view, e1, e2, distanceX, distanceY);
    }

    private final MotionEvent e1, e2;
    private final float distanceX, distanceY;

    private ViewGestureScrollEvent(@NonNull View view, @NonNull MotionEvent e1,
            @NonNull MotionEvent e2, float distanceX, float distanceY) {
        super(view);
        this.e1 = e1;
        this.e2 = e2;
        this.distanceX = distanceX;
        this.distanceY = distanceY;
    }

    @NonNull public MotionEvent e1() {
        return e1;
    }

    @NonNull public MotionEvent e2() {
        return e2;
    }

    public float distanceX() {
        return distanceX;
    }

    public float distanceY() {
        return distanceY;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ViewGestureScrollEvent)) return false;
        ViewGestureScrollEvent other = (ViewGestureScrollEvent) o;
        return other.view() == view() && other.e1.equals(e1) && other.e2.equals(e2)
                && Float.compare(other.distanceX, distanceX) == 0
                && Float.compare(other.distanceY, distanceY) == 0;
    }

    @Override public int hashCode() {
        int result = 17;
        result = result * 37 + view().hashCode();
        result = result * 37 + e1.hashCode();
        result = result * 37 + e2.hashCode();
        result = result * 37 + (distanceX != +0.0f ? Float.floatToIntBits(distanceX) : 0);
        result = result * 37 + (distanceY != +0.0f ? Float.floatToIntBits(distanceY) : 0);
        return result;
    }

    @Override public String toString() {
        return "ViewScrollGestureEvent{view=" + view() + ", e1=" + e1 + ", e2=" + e2 +
                ", distanceX=" + distanceX + ", distanceY=" + distanceY + '}';
    }
}
