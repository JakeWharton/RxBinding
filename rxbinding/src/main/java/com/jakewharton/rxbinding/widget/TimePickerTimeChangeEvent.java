package com.jakewharton.rxbinding.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.TimePicker;

import com.jakewharton.rxbinding.view.ViewEvent;

/**
 * Class that wraps a time change event
 */
public class TimePickerTimeChangeEvent extends ViewEvent<TimePicker> {

    @CheckResult
    @NonNull
    public static TimePickerTimeChangeEvent create(@NonNull TimePicker view, int hourOfDay,
                                                   int minute) {
        return new TimePickerTimeChangeEvent(view, hourOfDay, minute);
    }

    /**
     * The hour of the day that was selected
     */
    private int hourOfDay;

    /**
     * The minute in hour that was selected
     */
    private int minute;

    private TimePickerTimeChangeEvent(@NonNull TimePicker view, int hourOfDay, int minute) {
        super(view);
        this.hourOfDay = hourOfDay;
        this.minute = minute;
    }

    /**
     * Get the current hour
     * @return int
     */
    public int getHourOfDay() {
        return hourOfDay;
    }

    /**
     * Get the current minute
     * @return int
     */
    public int getMinute() {
        return minute;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = result * 37 + view().hashCode();
        result = result * 37 + hourOfDay;
        result = result * 37 + minute;
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof TimePickerTimeChangeEvent) {
            TimePickerTimeChangeEvent other = (TimePickerTimeChangeEvent) o;
            return this == other || this.hourOfDay == other.getHourOfDay()
                    && this.minute == other.getMinute();
        }
        return false;
    }

    @Override
    public String toString() {
        return "TimePickerTimeChangeEvenet{view="
                + view()
                + ", hourOfDay="
                + hourOfDay
                + ", minute="
                + minute
                + '}';
    }
}
