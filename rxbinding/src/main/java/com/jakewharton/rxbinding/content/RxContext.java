package com.jakewharton.rxbinding.content;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.NonNull;

import rx.Observable;

public class RxContext {

  /**
   * Create an observable which represents {@code android.content.ServiceConnection}:
   * emits a binder on acquiring connection, completes on disconnect
   * <p>
   * One for example map incoming binder to an appropriate component
   *
   * @see android.content.Context#bindService(Intent, ServiceConnection, int)
   */
  @NonNull
  public static Observable<IBinder> binder(@NonNull Context context,
                                           @NonNull Intent intent,
                                           int flags) {
    return Observable.create(new ServiceBinderOnSubscribe(context, intent, flags));
  }

  /**
   * Create an observable of intents received with {@code android.content.BroadcastReceiver}
   */
  @NonNull
  public static Observable<Intent> broadcasts(@NonNull Context context,
                                              @NonNull IntentFilter filter) {
    return Observable.create(new BroadcastIntentOnSubscribe(context, filter));
  }
}
