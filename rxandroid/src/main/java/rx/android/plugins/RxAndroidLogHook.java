package rx.android.plugins;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class RxAndroidLogHook {
  private static final RxAndroidLogHook DEFAULT_INSTANCE = new RxAndroidLogHook();

  public static RxAndroidLogHook getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  /** Invoked for any logging events. The default delegates to {@link Log}. */
  public void log(int priority, @NonNull String tag, @Nullable String msg, @Nullable Throwable tr) {
    if (msg == null || msg.isEmpty()) {
      if (tr == null) {
        return; // Empty
      }
      msg = Log.getStackTraceString(tr);
    } else if (tr != null) {
      msg += '\n' + Log.getStackTraceString(tr);
    }
    Log.println(priority, tag, msg);
  }
}
