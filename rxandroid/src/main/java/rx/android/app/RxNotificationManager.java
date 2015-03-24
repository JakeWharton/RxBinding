package rx.android.app;

import android.app.NotificationManager;
import rx.functions.Action1;

import static rx.android.internal.Preconditions.checkNotNull;

public final class RxNotificationManager {
  public static Action1<? extends NotificationAction> performAction(
      final NotificationManager notificationManager) {
    checkNotNull(notificationManager, "notificationManager == null");
    return new Action1<NotificationAction>() {
      @Override public void call(NotificationAction notificationAction) {
        notificationAction.apply(notificationManager);
      }
    };
  }

  private RxNotificationManager() {
    throw new AssertionError("No instances.");
  }
}
