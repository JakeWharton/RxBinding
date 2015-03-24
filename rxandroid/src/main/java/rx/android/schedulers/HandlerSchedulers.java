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
import android.os.Looper;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;

/** Static factory methods for creating a {@link Scheduler} from a {@link Handler}. */
public final class HandlerSchedulers {
  private static final Scheduler MAIN_THREAD_SCHEDULER =
      new HandlerScheduler(new Handler(Looper.getMainLooper()));

  /** Converts a {@link Handler} into a new {@link Scheduler} instance. */
  public static Scheduler from(final Handler handler) {
    if (handler == null) {
      throw new NullPointerException("handler == null");
    }
    return new HandlerScheduler(handler);
  }

  /** {@link Scheduler} which will execute actions on the Android main thread. */
  public static Scheduler mainThread() {
    Scheduler scheduler =
        RxAndroidPlugins.getInstance().getSchedulersHook().getMainThreadScheduler();
    return scheduler != null ? scheduler : MAIN_THREAD_SCHEDULER;
  }

  private HandlerSchedulers() {
    throw new AssertionError("No instances.");
  }
}
