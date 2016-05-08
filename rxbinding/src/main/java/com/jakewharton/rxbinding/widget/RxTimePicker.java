package com.jakewharton.rxbinding.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.TimePicker;

import rx.Observable;
import rx.functions.Action1;

import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

/**
 * Static factory methods for creating {@linkplain Observable observables} for {@link android.widget.TimePicker}.
 */
public final class RxTimePicker {

    private RxTimePicker() {
        throw new AssertionError("No instances.");
    }

    /**
     * Create an observable of the time change events on {@code view}.
     * <p>
     * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
     * to free this reference.
     * <p>
     * <em>Note:</em> A value will be emitted immediately on subscribe.
     */
    @CheckResult
    @NonNull
    public static Observable<TimePickerTimeChangeEvent> timeChange(@NonNull TimePicker view) {
        checkNotNull(view, "view == null");
        return Observable.create(new TimePickerTimeChangeOnSubscribe(view));
    }

    /**
     * An action which sets whether {@code view} is a 24 hour view.
     * <p>
     * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
     * to free this reference.
     */
    @CheckResult
    @NonNull
    public static Action1<? super Boolean> is24HourView(@NonNull final TimePicker view) {
        checkNotNull(view, "view == null");
        return new Action1<Boolean>() {
            @Override
            public void call(Boolean value) {
                view.setIs24HourView(value);
            }
        };
    }

    /**
     * An action which sets the hour of {@code view}.
     * <p>
     * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
     * to free this reference.
     */
    @CheckResult
    @NonNull
    public static Action1<? super Integer> hour(@NonNull final TimePicker view) {
        checkNotNull(view, "view == null");
        return new Action1<Integer>() {
            @Override
            public void call(Integer value) {
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
                    view.setCurrentHour(value);
                } else {
                    view.setHour(value);
                }
            }
        };
    }

    /**
     * An action which sets the minute of {@code view}.
     * <p>
     * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
     * to free this reference.
     */
    @CheckResult
    @NonNull
    public static Action1<? super Integer> minute(@NonNull final TimePicker view) {
        checkNotNull(view, "view == null");
        return new Action1<Integer>() {
            @Override
            public void call(Integer value) {
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
                    view.setCurrentMinute(value);
                } else {
                    view.setMinute(value);
                }
            }
        };
    }

}
