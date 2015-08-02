package com.jakewharton.rxbinding.internal;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import rx.Subscription;

public abstract class MainThreadSubscription implements Subscription, Runnable {
  private static final Handler mainThread = new Handler(Looper.getMainLooper());

  @SuppressWarnings("unused") // Updated by 'unsubscribedUpdater' object.
  private volatile int unsubscribed;
  private static final AtomicIntegerFieldUpdater<MainThreadSubscription> unsubscribedUpdater =
      AtomicIntegerFieldUpdater.newUpdater(MainThreadSubscription.class, "unsubscribed");

  @Override public final boolean isUnsubscribed() {
    return unsubscribed != 0;
  }

  @Override public final void unsubscribe() {
    if (unsubscribedUpdater.compareAndSet(this, 0, 1)) {
      if (Looper.getMainLooper() == Looper.myLooper()) {
        onUnsubscribe();
      } else {
        mainThread.post(this);
      }
    }
  }

  @Override public final void run() {
    onUnsubscribe();
  }

  protected abstract void onUnsubscribe();
}
