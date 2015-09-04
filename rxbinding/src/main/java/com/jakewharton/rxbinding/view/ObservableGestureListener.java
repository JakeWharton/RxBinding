package com.jakewharton.rxbinding.view;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import rx.subjects.PublishSubject;

final class ObservableGestureListener implements GestureDetector.OnGestureListener {

    final View view;

    PublishSubject<ViewGestureEvent> downGestureObservable = PublishSubject.create();
    PublishSubject<ViewGestureEvent> showPressGestureObservable = PublishSubject.create();
    PublishSubject<ViewGestureEvent> singleTapUpGestureObservable = PublishSubject.create();
    PublishSubject<ViewGestureScrollEvent> scrollGestureObservable = PublishSubject.create();
    PublishSubject<ViewGestureEvent> longPressGestureObservable = PublishSubject.create();
    PublishSubject<ViewGestureFlingEvent> flingGestureObservable = PublishSubject.create();

    ObservableGestureListener(View view) {
        this.view = view;
    }

    @Override public boolean onDown(MotionEvent e) {
        downGestureObservable.onNext(ViewGestureEvent.create(view, e));
        return false;
    }

    @Override public void onShowPress(MotionEvent e) {
        showPressGestureObservable.onNext(ViewGestureEvent.create(view, e));
    }

    @Override public boolean onSingleTapUp(MotionEvent e) {
        singleTapUpGestureObservable.onNext(ViewGestureEvent.create(view, e));
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        scrollGestureObservable.onNext(
                ViewGestureScrollEvent.create(view, e1, e2, distanceX, distanceY));
        return false;
    }

    @Override public void onLongPress(MotionEvent e) {
        longPressGestureObservable.onNext(ViewGestureEvent.create(view, e));
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        flingGestureObservable.onNext(
                ViewGestureFlingEvent.create(view, e1, e2, velocityX, velocityY));
        return false;
    }
}