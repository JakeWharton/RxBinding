package com.jakewharton.rxbinding.view;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Attaches a GestureDetector to the supplied touch event observable to be notified of
 * gesture events.
 * <p>
 * {@code
 *  Observable<MotionEvent> touches = RxView.touches(view);
 *  RxGestures gestures = RxGestures.withTouches(view, touches);
 *  Observable<ViewGestureScrollEvent> scrolls = gestures.scroll();
 * }
 * <p>
 * <strong>Warning:</strong> Instances keep a strong reference to the view. Operators that
 * cache instances have the potential to leak the associated {@code Context}.
 */
public class RxGestures {

    private final Observable<MotionEvent> motionEventObservable;
    private final GestureDetector gestureDetector;
    private final ObservableGestureListener gestureListener;

    /**
     * Create an {@code RxGestures} object that listens to motion events.
     * @param motionEvents
     * @return
     */
    public static RxGestures withMotionEvents(View view, Observable<MotionEvent> motionEvents) {
        return new RxGestures(view, motionEvents);
    }

    /**
     * Create an {@code RxGestures} object that listens to touch events.
     *
     * @param viewTouchEvents An observable of {@link ViewTouchEvent}s returned from
     * {@code RxView#touchEvents(View view)}.
     * @return A new RxGestures object listening for gestures.
     */
    public static RxGestures withViewTouchEvents(View view,
            Observable<ViewTouchEvent> viewTouchEvents) {
        return new RxGestures(view, viewTouchEvents
                .map(new Func1<ViewTouchEvent, MotionEvent>() {
                    @Override public MotionEvent call(ViewTouchEvent viewTouchEvent) {
                        return viewTouchEvent.motionEvent();
                    }
                }));
    }

    private RxGestures(View view, Observable<MotionEvent> motionEventObservable) {
        this.motionEventObservable = motionEventObservable;
        this.gestureListener = new ObservableGestureListener(view);
        this.gestureDetector = new GestureDetector(view.getContext(), gestureListener);

        motionEventObservable.subscribe(new Action1<MotionEvent>() {
            @Override public void call(MotionEvent motionEvent) {
                gestureDetector.onTouchEvent(motionEvent);
            }
        });
    }

    /**
     * Create an observable of down gestures.
     */
    public Observable<ViewGestureEvent> down() {
        return gestureListener.downGestureObservable;
    }

    /**
     * Create an observable of show press gestures.
     */
    public Observable<ViewGestureEvent> showPress() {
        return gestureListener.showPressGestureObservable;
    }

    /**
     * Create an observable of single tap up gestures.
     */
    public Observable<ViewGestureEvent> singleTapUp() {
        return gestureListener.singleTapUpGestureObservable;
    }

    /**
     * Create an observable of scroll gestures.
     */
    public Observable<ViewGestureScrollEvent> scroll() {
        return gestureListener.scrollGestureObservable;
    }

    /**
     * Create an observable of long press gestures.
     */
    public Observable<ViewGestureEvent> longPress() {
        return gestureListener.longPressGestureObservable;
    }

    /**
     * Create an observable of fling gestures.
     */
    public Observable<ViewGestureFlingEvent> fling() {
        return gestureListener.flingGestureObservable;
    }

}
