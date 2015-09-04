package com.jakewharton.rxbinding.view;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import rx.Observable;
import rx.subjects.PublishSubject;

final class ObservableGestureListener implements GestureDetector.OnGestureListener {

    final View view;

    private PublishSubject<ViewGestureEvent> downGestureObservable;
    private PublishSubject<ViewGestureEvent> showPressGestureObservable;
    private PublishSubject<ViewGestureEvent> singleTapUpGestureObservable;
    private PublishSubject<ViewGestureScrollEvent> scrollGestureObservable;
    private PublishSubject<ViewGestureEvent> longPressGestureObservable;
    private PublishSubject<ViewGestureFlingEvent> flingGestureObservable;

    ObservableGestureListener(View view) {
        this.view = view;
    }

    public Observable<ViewGestureEvent> downObservable() {
        if (downGestureObservable == null) {
            downGestureObservable = PublishSubject.create();
        }
        return downGestureObservable;
    }

    public Observable<ViewGestureEvent> showPressObservable() {
        if (showPressGestureObservable == null) {
            showPressGestureObservable = PublishSubject.create();
        }
        return showPressGestureObservable;
    }

    public Observable<ViewGestureEvent> singleTapUpObservable() {
        if (singleTapUpGestureObservable == null) {
            singleTapUpGestureObservable = PublishSubject.create();
        }
        return singleTapUpGestureObservable;
    }

    public Observable<ViewGestureScrollEvent> scrollObservable() {
        if (scrollGestureObservable == null) {
            scrollGestureObservable = PublishSubject.create();
        }
        return scrollGestureObservable;
    }

    public Observable<ViewGestureEvent> longPressObservable() {
        if (longPressGestureObservable == null) {
            longPressGestureObservable = PublishSubject.create();
        }
        return longPressGestureObservable;
    }

    public Observable<ViewGestureFlingEvent> flingObservable() {
        if (flingGestureObservable == null) {
            flingGestureObservable = PublishSubject.create();
        }
        return flingGestureObservable;
    }

    @Override public boolean onDown(MotionEvent e) {
        if (downGestureObservable != null) {
            downGestureObservable.onNext(ViewGestureEvent.create(view, e));
        }
        return false;
    }

    @Override public void onShowPress(MotionEvent e) {
        if (showPressGestureObservable != null) {
            showPressGestureObservable.onNext(ViewGestureEvent.create(view, e));
        }
    }

    @Override public boolean onSingleTapUp(MotionEvent e) {
        if (singleTapUpGestureObservable != null) {
            singleTapUpGestureObservable.onNext(ViewGestureEvent.create(view, e));
        }
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (scrollGestureObservable != null) {
            scrollGestureObservable.onNext(
                    ViewGestureScrollEvent.create(view, e1, e2, distanceX, distanceY));
        }
        return false;
    }

    @Override public void onLongPress(MotionEvent e) {
        if (longPressGestureObservable != null) {
            longPressGestureObservable.onNext(ViewGestureEvent.create(view, e));
        }
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (flingGestureObservable != null) {
            flingGestureObservable.onNext(
                    ViewGestureFlingEvent.create(view, e1, e2, velocityX, velocityY));
        }
        return false;
    }
}