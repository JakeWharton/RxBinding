package com.jakewharton.rxbinding.widget;

import android.widget.DatePicker;

import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static rx.android.MainThreadSubscription.verifyMainThread;

/**
 * Observable that emits date change events
 */
public class DatePickerChangeOnSubscribe implements Observable.OnSubscribe<DatePickerChangeEvent> {

    /**
     * The current year
     */
    private int year;

    /**
     * The current month of the year
     */
    private int monthOfYear;

    /**
     * The current day of the month
     */
    private int dayOfMonth;

    /**
     * The DatePicker object that emits
     */
    private DatePicker datePicker;

    public DatePickerChangeOnSubscribe(DatePicker datePicker, int year, int monthOfYear,
                                       int dayOfMonth) {
        this.datePicker = datePicker;
        this.year = year;
        this.monthOfYear = monthOfYear;
        this.dayOfMonth = dayOfMonth;
    }

    @Override
    public void call(final Subscriber<? super DatePickerChangeEvent> subscriber) {
        verifyMainThread();

        DatePicker.OnDateChangedListener onDateChangedListener
                = new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(DatePickerChangeEvent.create(view, year, monthOfYear,
                            dayOfMonth));
                }
            }
        };
        datePicker.init(year, monthOfYear, dayOfMonth, onDateChangedListener);

        subscriber.add(new MainThreadSubscription() {
            @Override
            protected void onUnsubscribe() {
                datePicker.init(datePicker.getYear(), datePicker.getMonth(),
                        datePicker.getDayOfMonth(), null);
            }
        });

        // Emit initial value
        subscriber.onNext(DatePickerChangeEvent.create(datePicker, datePicker.getYear(),
                datePicker.getMonth(), datePicker.getDayOfMonth()));
    }
}
