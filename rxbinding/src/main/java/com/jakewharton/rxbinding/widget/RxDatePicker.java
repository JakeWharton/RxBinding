package com.jakewharton.rxbinding.widget;

import android.annotation.TargetApi;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.DatePicker;
import android.widget.TimePicker;

import rx.Observable;
import rx.functions.Action1;

import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

/**
 * Static factory methods for creating {@linkplain Observable observables} for {@link android.widget.DatePicker}.
 */
public class RxDatePicker {

    private RxDatePicker() {
        throw new AssertionError("No instances.");
    }


    /**
     * Create an observable of the time change events on {@code view}.
     * <p/>
     * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
     * to free this reference.
     * <p/>
     * <em>Note:</em> A value will be emitted immediately on subscribe.
     */
    @CheckResult
    @NonNull
    public static Observable<DatePickerChangeEvent> init(@NonNull DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        checkNotNull(view, "view == null");
        return Observable.create(new DatePickerChangeOnSubscribe(view, year, monthOfYear, dayOfMonth));
    }

    /**
     * An action which sets the first day of week
     * <p/>
     * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
     * to free this reference.
     */
    @CheckResult
    @NonNull
    @TargetApi(21)
    public static Action1<? super Integer> firstDayOfWeek(@NonNull final DatePicker view) {
        checkNotNull(view, "view == null");
        return new Action1<Integer>() {
            @Override
            public void call(Integer value) {
                view.setFirstDayOfWeek(value);
            }
        };
    }

    /**
     * An action which sets the maximal date supported by this {@link DatePicker} in
     * milliseconds since January 1, 1970 00:00:00 in
     * <p/>
     * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
     * to free this reference.
     */
    @CheckResult
    @NonNull
    public static Action1<? super Long> maxDate(@NonNull final DatePicker view) {
        checkNotNull(view, "view == null");
        return new Action1<Long>() {
            @Override
            public void call(Long value) {
                view.setMaxDate(value);
            }
        };
    }

    /**
     * An action which sets the minimal date supported by this {@link DatePicker} in
     * milliseconds since January 1, 1970 00:00:00 in
     * <p/>
     * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
     * to free this reference.
     */
    @CheckResult
    @NonNull
    public static Action1<? super Long> minDate(@NonNull final DatePicker view) {
        checkNotNull(view, "view == null");
        return new Action1<Long>() {
            @Override
            public void call(Long value) {
                view.setMinDate(value);
            }
        };
    }

    /**
     * An action which sets whether {@code view} show spinners.
     * <p/>
     * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
     * to free this reference.
     */
    @CheckResult
    @NonNull
    public static Action1<? super Boolean> areSpinnersShown(@NonNull final DatePicker view) {
        checkNotNull(view, "view == null");
        return new Action1<Boolean>() {
            @Override
            public void call(Boolean value) {
                view.setSpinnersShown(value);
            }
        };
    }
}
