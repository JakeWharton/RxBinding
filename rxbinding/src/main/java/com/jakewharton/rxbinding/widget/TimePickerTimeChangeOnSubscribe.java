package com.jakewharton.rxbinding.widget;

import android.widget.TimePicker;

import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static rx.android.MainThreadSubscription.verifyMainThread;

/**
 * Observable that emits time change events
 */
public class TimePickerTimeChangeOnSubscribe
        implements Observable.OnSubscribe<TimePickerTimeChangeEvent> {

    /**
     * The time picker that emits events
     */
    private TimePicker timePicker;

    protected TimePickerTimeChangeOnSubscribe(TimePicker timePicker) {
        this.timePicker = timePicker;
    }

    @Override
    public void call(final Subscriber<? super TimePickerTimeChangeEvent> subscriber) {
        verifyMainThread();

        TimePicker.OnTimeChangedListener onTimeChangedListener
                = new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(TimePickerTimeChangeEvent.create(timePicker, hourOfDay,
                            minute));
                }
            }
        };
        timePicker.setOnTimeChangedListener(onTimeChangedListener);

        subscriber.add(new MainThreadSubscription() {
            @Override
            protected void onUnsubscribe() {
                timePicker.setOnTimeChangedListener(null);
            }
        });

        // Emit initial value
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            subscriber.onNext(TimePickerTimeChangeEvent.create(timePicker,
                    timePicker.getCurrentHour(), timePicker.getCurrentMinute()));
        } else {
            subscriber.onNext(TimePickerTimeChangeEvent.create(timePicker, timePicker.getHour(),
                    timePicker.getMinute()));
        }
    }
}
