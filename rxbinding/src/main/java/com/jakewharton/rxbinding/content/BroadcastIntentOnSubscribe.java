package com.jakewharton.rxbinding.content;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

final class BroadcastIntentOnSubscribe implements Observable.OnSubscribe<Intent> {

  final Context mContext;
  final IntentFilter mIntentFilter;

  BroadcastIntentOnSubscribe(@NonNull Context context,
                             @NonNull IntentFilter intentFilter) {
    mContext = context;
    mIntentFilter = intentFilter;
  }

  @Override public void call(final Subscriber<? super Intent> subscriber) {
    final BroadcastReceiver receiver = new BroadcastReceiver() {
      @Override public void onReceive(Context context, Intent intent) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(intent);
        }
      }
    };
    mContext.registerReceiver(receiver, mIntentFilter);
    subscriber.add(Subscriptions.create(new Action0() {
      @Override public void call() {
        mContext.unregisterReceiver(receiver);
      }
    }));
  }
}
