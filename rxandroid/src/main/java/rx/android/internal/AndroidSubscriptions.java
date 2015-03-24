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
package rx.android.internal;

import android.os.Looper;
import rx.Scheduler.Worker;
import rx.Subscription;
import rx.android.schedulers.HandlerSchedulers;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

public final class AndroidSubscriptions {
  /**
   * Create a {@link Subscription} that always runs {@code unsubscribe} on {@link
   * HandlerSchedulers#mainThread()}.
   */
  public static Subscription unsubscribeOnMainThread(final Action0 unsubscribe) {
    return Subscriptions.create(new Action0() {
      @Override public void call() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
          unsubscribe.call();
        } else {
          final Worker inner = HandlerSchedulers.mainThread().createWorker();
          inner.schedule(new Action0() {
            @Override public void call() {
              unsubscribe.call();
              inner.unsubscribe();
            }
          });
        }
      }
    });
  }

  private AndroidSubscriptions() {
    throw new AssertionError("No instances.");
  }
}
