package com.jakewharton.rxbinding.widget;

import android.annotation.TargetApi;
import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.DatePicker;

import com.jakewharton.rxbinding.RecordingObserver;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public class RxDatePickerTest {

    @Rule
    public final ActivityTestRule<RxDatePickerTestActivity> activityRule =
            new ActivityTestRule<>(RxDatePickerTestActivity.class);

    /**
     * The time picker view
     */
    private DatePicker datePicker;

    /**
     * The instrumentation object
     */
    private final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();

    @Before
    public void setUp() {
        datePicker = activityRule.getActivity().datePicker;
    }

    @Test
    public void testDateChanges() {
        RecordingObserver<DatePickerChangeEvent> o = new RecordingObserver<>();
        Subscription subscription = RxDatePicker.init(datePicker, 2016, 5, 10) //
                .subscribeOn(AndroidSchedulers.mainThread()) //
                .subscribe(o);
        assertThat(o.takeNext()).isEqualTo(DatePickerChangeEvent.create(datePicker, 2016, 5, 10));

        instrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                datePicker.updateDate(2016, 1, 1);
                datePicker.updateDate(2016, 1, 2);
                datePicker.updateDate(2016, 2, 2);
                datePicker.updateDate(2017, 11, 25);
            }
        });
        assertThat(o.takeNext()).isEqualTo(DatePickerChangeEvent.create(datePicker, 2016, 1, 1));
        assertThat(o.takeNext()).isEqualTo(DatePickerChangeEvent.create(datePicker, 2016, 1, 2));
        assertThat(o.takeNext()).isEqualTo(DatePickerChangeEvent.create(datePicker, 2016, 2, 2));
        assertThat(o.takeNext()).isEqualTo(DatePickerChangeEvent.create(datePicker, 2017, 11, 25));

        subscription.unsubscribe();
    }

    @Test
    @UiThreadTest
    public void testUpdateDate(){
        Action1<? super Integer> action = RxDatePicker.updateYear(datePicker);
        action.call(1);
        assertThat(datePicker.getYear()).isEqualTo(1);
        action.call(12);
        assertThat(datePicker.getYear()).isEqualTo(12);

        Action1<? super Integer> action2 = RxDatePicker.updateDayOfMonth(datePicker);
        action2.call(25);
        assertThat(datePicker.getDayOfMonth()).isEqualTo(25);
        action2.call(2);
        assertThat(datePicker.getDayOfMonth()).isEqualTo(2);

        Action1<? super Integer> action3 = RxDatePicker.updateMonthOfYear(datePicker);
        action3.call(11);
        assertThat(datePicker.getMonth()).isEqualTo(11);
        action3.call(5);
        assertThat(datePicker.getMonth()).isEqualTo(5);
    }

    @Test
    @UiThreadTest
    @TargetApi(21)
    public void testSetFirstDayOfWeek(){
        Action1<? super Integer> action = RxDatePicker.firstDayOfWeek(datePicker);
        action.call(1);
        assertThat(datePicker.getFirstDayOfWeek()).isEqualTo(1);
        action.call(7);
        assertThat(datePicker.getFirstDayOfWeek()).isEqualTo(7);
        assertThat(datePicker.getFirstDayOfWeek()).isNotEqualTo(5);
    }

    @Test
    @UiThreadTest
    public void testMaxMinDate(){
        Action1<? super Long> action = RxDatePicker.maxDate(datePicker);
        action.call(1L);
        assertThat(datePicker.getMaxDate()).isEqualTo(1L);
        action.call(1232131232131L);
        assertThat(datePicker.getMaxDate()).isEqualTo(1232131232131L);

        Action1<? super Long> action2 = RxDatePicker.minDate(datePicker);
        action2.call(12L);
        assertThat(datePicker.getMinDate()).isEqualTo(12L);
        action2.call(3232L);
        assertThat(datePicker.getMinDate()).isEqualTo(3232L);
    }

    @Test
    @UiThreadTest
    public void testShownSpinners(){
        Action1<? super Boolean> action = RxDatePicker.spinnersShown(datePicker);
//        action.call(true);
        datePicker.setSpinnersShown(true);
        //it is false because the spinners are not shown on calendar mode
        assertThat(datePicker.getSpinnersShown()).isFalse();
        action.call(false);
        assertThat(datePicker.getSpinnersShown()).isFalse();

    }

}
