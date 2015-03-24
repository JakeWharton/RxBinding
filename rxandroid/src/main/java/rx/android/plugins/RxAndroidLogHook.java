package rx.android.plugins;

import android.util.Log;

public class RxAndroidLogHook {
  private static final RxAndroidLogHook DEFAULT_INSTANCE = new RxAndroidLogHook();

  public static RxAndroidLogHook getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  /**
   * Invoked for any logging events. The default delegates to {@link android.util.Log}.
   *
   * @return the number of bytes written
   */
  public int log(int priority, String tag, String msg) {
    return Log.println(priority, tag, msg);
  }

  /**
   * Invoked for any logging events. The default delegates to {@link android.util.Log}.
   *
   * @return the number of bytes written
   */
  public int log(int priority, String tag, String msg, Throwable tr) {
    return Log.println(priority, tag, msg + '\n' + Log.getStackTraceString(tr));
  }
}
