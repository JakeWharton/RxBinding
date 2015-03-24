/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package rx.android.schedulers;

import android.os.Handler;
import java.lang.NullPointerException;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscription;
import rx.android.schedulers.HandlerSchedulers;
import rx.functions.Action0;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
public final class HandlerSchedulersTest {
  @Test public void nullThrows() {
    try {
      HandlerSchedulers.from(null);
      fail();
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("handler == null");
    }
  }

  @Test public void schedulePostsImmediately() {
    Handler handler = mock(Handler.class);
    Action0 action = mock(Action0.class);

    Scheduler scheduler = HandlerSchedulers.from(handler);
    Worker inner = scheduler.createWorker();
    inner.schedule(action);

    // Verify that we post to the given Handler.
    ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
    verify(handler).postDelayed(runnableCaptor.capture(), eq(0L));

    // Verify that the given handler delegates to our action.
    runnableCaptor.getValue().run();
    verify(action).call();
  }

  @Test public void scheduleDelayedPostsDelayed() {
    Handler handler = mock(Handler.class);
    Action0 action = mock(Action0.class);

    Scheduler scheduler = HandlerSchedulers.from(handler);
    Worker inner = scheduler.createWorker();
    inner.schedule(action, 1L, TimeUnit.SECONDS);

    // Verify that we post to the given Handler.
    ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
    verify(handler).postDelayed(runnableCaptor.capture(), eq(1000L));

    // Verify that the given handler delegates to our action.
    runnableCaptor.getValue().run();
    verify(action).call();
  }

  @Test public void unsubscribeRemoveCallbacks() {
    Handler handler = mock(Handler.class);
    Action0 action = mock(Action0.class);

    Scheduler scheduler = HandlerSchedulers.from(handler);
    Worker inner = scheduler.createWorker();
    Subscription subscription = inner.schedule(action);

    // Verify that we post to the given Handler.
    verify(handler).postDelayed(any(Runnable.class), eq(0L));

    subscription.unsubscribe();

    ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
    verify(handler).removeCallbacks(runnableCaptor.capture());

    // Verify that removed callback delegates to our action.
    runnableCaptor.getValue().run();
    verify(action).call();
  }
}
