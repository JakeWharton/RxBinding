package com.jakewharton.rxbinding.content;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.NonNull;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

final class ServiceBinderOnSubscribe implements Observable.OnSubscribe<IBinder> {

  final Context mContext;
  final Intent mIntent;
  final int mFlags;

  ServiceBinderOnSubscribe(@NonNull Context context,
                           @NonNull Intent intent,
                           int flags) {
    mContext = context;
    mIntent = intent;
    mFlags = flags;
  }

  @Override public void call(final Subscriber<? super IBinder> subscriber) {
    final ServiceConnection connection = new ServiceConnection() {
      @Override public void onServiceConnected(ComponentName name, IBinder binder) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(binder);
        }
      }

      @Override public void onServiceDisconnected(ComponentName name) {
        subscriber.onCompleted();
      }
    };

    subscriber.add(Subscriptions.create(new Action0() {
      @Override public void call() {
        mContext.unbindService(connection);
      }
    }));

    mContext.bindService(mIntent, connection, mFlags);
  }
}
