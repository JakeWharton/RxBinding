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

  final Context context;
  final IntentFilter intentFilter;

  BroadcastIntentOnSubscribe(@NonNull Context context,
                             @NonNull IntentFilter intentFilter) {
    this.context = context;
    this.intentFilter = intentFilter;
  }

  @Override public void call(final Subscriber<? super Intent> subscriber) {
    final BroadcastReceiver receiver = new BroadcastReceiver() {
      @Override public void onReceive(Context context, Intent intent) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(intent);
        }
      }
    };
    context.registerReceiver(receiver, intentFilter);
    subscriber.add(Subscriptions.create(new Action0() {
      @Override public void call() {
        context.unregisterReceiver(receiver);
      }
    }));
  }
}
