package com.jakewharton.rxbinding.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.DatePicker;

import com.jakewharton.rxbinding.view.ViewEvent;

public class DatePickerChangeEvent extends ViewEvent<DatePicker> {

    @CheckResult
    @NonNull
    public static DatePickerChangeEvent create(@NonNull DatePicker view, int year, int monthOfYear,
                                               int dayOfMonth) {
        return new DatePickerChangeEvent(view, year, monthOfYear, dayOfMonth);
    }

    private int year;

    private int monthOfYear;

    private int dayOfMonth;

    private DatePickerChangeEvent(@NonNull DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
        super(view);
        this.year = year;
        this.monthOfYear = monthOfYear;
        this.dayOfMonth = dayOfMonth;
    }

    public int getYear() {
        return year;
    }

    public int getMonthOfYear() {
        return monthOfYear;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = result * 37 + view().hashCode();
        result = result * 37 + year;
        result = result * 37 + monthOfYear;
        result = result * 37 + dayOfMonth;
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DatePickerChangeEvent) {
            DatePickerChangeEvent other = (DatePickerChangeEvent) o;
            return this == other || this.year == other.getYear()
                    && this.monthOfYear == other.getMonthOfYear()
                    && this.dayOfMonth == other.getDayOfMonth();
        }
        return false;
    }

    @Override
    public String toString() {
        return "DatePickerChangeEvent{view="
                + view()
                + ", year="
                + year
                + ", monthOfYear="
                + monthOfYear
                + ", dayOfMonth "
                + dayOfMonth
                + '}';
    }
}
