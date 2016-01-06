package com.jakewharton.rxbinding.content;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import rx.Observable;

import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

public class RxContext {

  /**
   * Create an observable which represents {@code android.content.ServiceConnection}:
   * emits a binder on acquiring connection, completes on disconnect
   * <p>
   * One for example can map incoming binder to an appropriate component
   */
  @CheckResult @NonNull
  public static Observable<IBinder> binder(@NonNull Context context,
                                           @NonNull Intent intent,
                                           int flags) {
    checkNotNull(context, "context == null");
    checkNotNull(intent, "intent == null");
    return Observable.create(new ServiceBinderOnSubscribe(context, intent, flags));
  }

  /**
   * Create an observable of intents received with {@code android.content.BroadcastReceiver}
   */
  @CheckResult @NonNull
  public static Observable<Intent> broadcasts(@NonNull Context context,
                                              @NonNull IntentFilter intentFilter) {
    checkNotNull(context, "context == null");
    checkNotNull(intentFilter, "intentFilter == null");
    return Observable.create(new BroadcastIntentOnSubscribe(context, intentFilter));
  }

  private RxContext() {
    throw new AssertionError("No instances.");
  }
}
