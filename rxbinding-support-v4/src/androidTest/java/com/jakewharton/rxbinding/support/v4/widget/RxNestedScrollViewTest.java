package com.jakewharton.rxbinding.support.v4.widget;

import android.support.test.annotation.UiThreadTest;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ScrollToAction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.FrameLayout;

import com.jakewharton.rxbinding.RecordingObserver;
import com.jakewharton.rxbinding.view.ViewScrollChangeEvent;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static android.support.test.espresso.Espresso.onView;

@RunWith(AndroidJUnit4.class)
public final class RxNestedScrollViewTest {
    @Rule
    public final ActivityTestRule<RxNestedScrollViewTestActivity> activityRule =
            new ActivityTestRule<>(RxNestedScrollViewTestActivity.class);

    private NestedScrollView view;
    private FrameLayout emptyView;

    @Before
    public void setUp() {
        RxNestedScrollViewTestActivity activity = activityRule.getActivity();
        view = activity.nestedScrollView;
        emptyView = activity.emptyView;
    }

    @Test
    @UiThreadTest
    public void refreshes() {
        RecordingObserver<ViewScrollChangeEvent> o = new RecordingObserver<>();
        Subscription subscription = RxNestedScrollView.scrollChangeEvents(view)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(o);
        o.assertNoMoreEvents();

        onView(Matchers.is((View) emptyView)).perform(scrollTo());
        o.takeNext();

        subscription.unsubscribe();
        onView(Matchers.is((View) emptyView)).perform(scrollTo());
        o.assertNoMoreEvents();
    }

    private static ViewAction scrollTo() {
        return new ScrollToAction();
    }
}
