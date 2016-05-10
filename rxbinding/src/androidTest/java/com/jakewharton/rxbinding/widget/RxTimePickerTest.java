package com.jakewharton.rxbinding.widget;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TimePicker;

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
public class RxTimePickerTest {
    @Rule
    public final ActivityTestRule<RxTimePickerTestActivity> activityRule =
            new ActivityTestRule<>(RxTimePickerTestActivity.class);

    /**
     * The time picker view
     */
    private TimePicker timePicker;

    /**
     * The instrumentation object
     */
    private final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();

    @Before
    public void setUp() {
        timePicker = activityRule.getActivity().timePicker;
    }

    @Test
    public void testTimeChanges() {
        RecordingObserver<TimePickerTimeChangeEvent> o = new RecordingObserver<>();
        Subscription subscription = RxTimePicker.timeChange(timePicker) //
                .subscribeOn(AndroidSchedulers.mainThread()) //
                .subscribe(o);
        assertThat(o.takeNext()).isNotNull();

        instrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                timePicker.setCurrentMinute(59);
                timePicker.setCurrentHour(11);
                timePicker.setCurrentMinute(23);
            }
        });
        TimePickerTimeChangeEvent event = o.takeNext();
        assertThat(event).isEqualTo(TimePickerTimeChangeEvent.create(timePicker, event.getHourOfDay(), 59));


        //there is a bug in the android system that calls onTimeChange twice
        o.takeNext();
        event = o.takeNext();
        assertThat(event).isEqualTo(TimePickerTimeChangeEvent.create(timePicker, 11, event.getMinute()));

        //there is a bug in the android system that calls onTimeChange twice
        o.takeNext();
        event = o.takeNext();
        assertThat(event).isEqualTo(TimePickerTimeChangeEvent.create(timePicker, event.getHourOfDay(), 23));

        subscription.unsubscribe();
    }

    @Test
    @UiThreadTest
    public void testIs24HourMode() {
        Action1<? super Boolean> action = RxTimePicker.is24HourView(timePicker);
        action.call(true);
        assertThat(timePicker.is24HourView()).isTrue();
        action.call(false);
        assertThat(timePicker.is24HourView()).isFalse();
    }


    @Test
    @UiThreadTest
    public void testHour() {
        Action1<? super Integer> action = RxTimePicker.hour(timePicker);
        action.call(12);
        assertThat(timePicker.getCurrentHour()).isEqualTo(12);
        action.call(13);
        assertThat(timePicker.getCurrentHour()).isEqualTo(13);
        action.call(1);
        assertThat(timePicker.getCurrentHour()).isEqualTo(1);
        action.call(23);
        assertThat(timePicker.getCurrentHour()).isEqualTo(23);
    }

    @Test
    @UiThreadTest
    public void testMinute() {
        Action1<? super Integer> action = RxTimePicker.minute(timePicker);
        action.call(59);
        assertThat(timePicker.getCurrentMinute()).isEqualTo(59);
        action.call(13);
        assertThat(timePicker.getCurrentMinute()).isEqualTo(13);
        action.call(45);
        assertThat(timePicker.getCurrentMinute()).isEqualTo(45);
        action.call(33);
        assertThat(timePicker.getCurrentMinute()).isEqualTo(33);
    }
}
