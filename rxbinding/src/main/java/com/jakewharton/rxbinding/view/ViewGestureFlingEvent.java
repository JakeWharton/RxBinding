package com.jakewharton.rxbinding.view;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;

public final class ViewGestureFlingEvent extends ViewEvent<View> {
    @CheckResult @NonNull
    public static ViewGestureFlingEvent create(@NonNull View view, @NonNull MotionEvent e1,
            @NonNull MotionEvent e2, float velocityX, float velocityY) {
        return new ViewGestureFlingEvent(view, e1, e2, velocityX, velocityY);
    }

    private final MotionEvent e1, e2;
    private final float velocityX, velocityY;

    private ViewGestureFlingEvent(@NonNull View view, @NonNull MotionEvent e1,
            @NonNull MotionEvent e2, float velocityX, float velocityY) {
        super(view);
        this.e1 = e1;
        this.e2 = e2;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    @NonNull public MotionEvent e1() {
        return e1;
    }

    @NonNull public MotionEvent e2() {
        return e2;
    }

    public float velocityX() {
        return velocityX;
    }

    public float velocityY() {
        return velocityY;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ViewGestureFlingEvent)) return false;
        ViewGestureFlingEvent other = (ViewGestureFlingEvent) o;
        return other.view() == view() && other.e1.equals(e1) && other.e2.equals(e2)
                && Float.compare(other.velocityX, velocityX) == 0
                && Float.compare(other.velocityY, velocityY) == 0;
    }

    @Override public int hashCode() {
        int result = 17;
        result = result * 37 + view().hashCode();
        result = result * 37 + e1.hashCode();
        result = result * 37 + e2.hashCode();
        result = result * 37 + (velocityX != +0.0f ? Float.floatToIntBits(velocityX) : 0);
        result = result * 37 + (velocityY != +0.0f ? Float.floatToIntBits(velocityY) : 0);
        return result;
    }

    @Override public String toString() {
        return "ViewScrollGestureEvent{view=" + view() + ", e1=" + e1 + ", e2=" + e2 +
                ", velocityX=" + velocityX + ", velocityY=" + velocityY + '}';
    }
}
