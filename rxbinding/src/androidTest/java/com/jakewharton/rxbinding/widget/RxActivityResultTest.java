package com.jakewharton.rxbinding.widget;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.jakewharton.rxbinding.ActivityMonitor;
import com.jakewharton.rxbinding.RecordingObserver;
import com.jakewharton.rxbinding.view.ActivityResultEvent;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.jakewharton.rxbinding.widget.RxActivityResultTestActivity.KEY_IDENTIFIER;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

@RunWith(AndroidJUnit4.class)
public class RxActivityResultTest {

    private Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
    private RxActivityResultTestActivity activity;
    private ActivityMonitor activityMonitor;

    @Rule
    public final UiThreadTestRule uiThread = new UiThreadTestRule();

    @Rule
    public final ActivityTestRule<RxActivityResultTestActivity> activityRule =
            new ActivityTestRule<RxActivityResultTestActivity>(RxActivityResultTestActivity.class) {

                @Override
                protected void beforeActivityLaunched() {
                    activityMonitor = new ActivityMonitor(instrumentation.getTargetContext());
                }

                @Override
                protected Intent getActivityIntent() {
                    return RxActivityResultTestActivity.createActivityStartIntent(instrumentation.getTargetContext(), "TestActivity");
                }
            };

    @Before
    public void setup() {
        activity = activityRule.getActivity();
    }

    @Test
    public void control() throws Exception {
        final Activity createdActivity = activityMonitor.waitForActivityCreationWithIntentStringExtra(KEY_IDENTIFIER, "TestActivity");
        assertNotNull(createdActivity);
        assertSame(activity, createdActivity);
    }

    @Test
    public void startActivityForResult() throws Exception {
        final RecordingObserver<ActivityResultEvent> o = new RecordingObserver<>();
        final Intent intent = RxActivityResultTestActivity.createActivityStartIntent(activity, "ResultActivity");
        RxActivityResult.startActivityForResult(activity, intent).subscribe(o);

        final RxActivityResultTestActivity resultActivity = activityMonitor.waitForActivityCreationWithIntentStringExtra(KEY_IDENTIFIER, "ResultActivity");
        instrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                Intent data = new Intent();
                data.putExtra("key", "value");
                resultActivity.setResult(7979, data);
                resultActivity.finish();
            }
        });
        activityMonitor.waitForActivityDestructionWithIntentStringExtra(KEY_IDENTIFIER, "ResultActivity");

        ActivityResultEvent resultEvent = o.takeNextWait();
        o.assertOnCompleted();
        assertNotNull(resultEvent);
        assertEquals(7979, resultEvent.getResultCode());
        assertNotNull(resultEvent.getData());
        assertEquals("value", resultEvent.getData().getStringExtra("key"));

        instrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                activity.getFragmentManager().executePendingTransactions();
                assertNull(activity.getFragmentManager().findFragmentByTag(RxActivityResult.TAG));
            }
        });

    }

}