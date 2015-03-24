/*
 * Copyright (C) 2015 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package rx.android;

import android.os.Handler;
import android.os.Looper;
import android.test.UiThreadTest;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * A JUnit {@linkplain Rule rule} which runs tests on the Android UI thread.
 * <p>
 * Usage:
 * <pre>{@code
 * &#064;Rule
 * public final UiThreadRule uiThread = UiThreadRule.createWithTimeout(10, TimeUnit.SECONDS);
 * }</pre>
 *
 * Annotate individual tests with {@link UiThreadTest @UiThreadTest} in order to have them run on
 * the UI thread.
 */
public final class UiThreadRule implements TestRule {
  /** Create with no timeout for test method execution on the UI thread. */
  public static UiThreadRule create() {
    return new UiThreadRule(Long.MAX_VALUE, MILLISECONDS);
  }

  /** Create with a timeout for the length of time a test method can run on the UI thread. */
  public static UiThreadRule createWithTimeout(long amount, TimeUnit unit) {
    return new UiThreadRule(amount, unit);
  }

  private final Handler mainThread = new Handler(Looper.getMainLooper());
  private final long timeoutAmount;
  private final TimeUnit timeoutUnit;

  private UiThreadRule(long timeoutAmount, TimeUnit timeoutUnit) {
    this.timeoutAmount = timeoutAmount;
    this.timeoutUnit = timeoutUnit;
  }

  @Override public Statement apply(final Statement base, Description description) {
    if (description.getAnnotation(UiThreadTest.class) == null) {
      return base;
    }
    return new Statement() {
      @Override public void evaluate() throws Throwable {
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<Throwable> throwableRef = new AtomicReference<>();
        mainThread.post(new Runnable() {
          @Override public void run() {
            try {
              base.evaluate();
            } catch (Throwable throwable) {
              throwableRef.set(throwable);
            } finally {
              latch.countDown();
            }
          }
        });
        if (!latch.await(timeoutAmount, timeoutUnit)) {
          throw new TimeoutException(
              "Test took longer than " + timeoutAmount + " " + timeoutUnit.name()
                  .toLowerCase(Locale.US));
        }
        Throwable thrown = throwableRef.get();
        if (thrown != null) {
          throw thrown;
        }
      }
    };
  }
}
