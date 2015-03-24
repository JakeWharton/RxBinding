package rx.android;

import java.util.Locale;
import java.util.concurrent.TimeUnit;
import rx.android.plugins.RxAndroidClockHook;

public final class FakeClock extends RxAndroidClockHook {
  private long timeMs;

  @Override public long uptimeMillis() {
    return timeMs;
  }

  public void advance(long amount, TimeUnit unit) {
    if (unit == null) {
      throw new NullPointerException("unit == null");
    }
    if (amount < 0) {
      throw new IllegalStateException(
          "Can only advance forward: " + amount + " " + unit.name().toLowerCase(Locale.US));
    }
    timeMs += unit.toMillis(amount);
  }
}
