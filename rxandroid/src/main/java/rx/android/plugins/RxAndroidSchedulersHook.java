package rx.android.plugins;

import rx.Scheduler;
import rx.android.schedulers.HandlerSchedulers;
import rx.functions.Action0;

public class RxAndroidSchedulersHook {
  private static final RxAndroidSchedulersHook DEFAULT_INSTANCE = new RxAndroidSchedulersHook();

  public static RxAndroidSchedulersHook getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  /**
   * Scheduler to return from {@link HandlerSchedulers#mainThread()} or {@code null} if default
   * should be used.
   * <p>
   * This instance should be or behave like a stateless singleton.
   */
  public Scheduler getMainThreadScheduler() {
    return null;
  }

  /**
   * Invoked before the Action is handed over to the scheduler.  Can be used for
   * wrapping/decorating/logging. The default is just a passthrough.
   *
   * @param action action to schedule
   * @return wrapped action to schedule
   */
  public Action0 onSchedule(Action0 action) {
    return action;
  }
}
