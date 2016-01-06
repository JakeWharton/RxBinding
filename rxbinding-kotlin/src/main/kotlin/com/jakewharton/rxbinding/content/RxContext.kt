package com.jakewharton.rxbinding.content

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import rx.Observable

/**
 * Create an observable which represents `android.content.ServiceConnection`:
 * emits a binder on acquiring connection, completes on disconnect
 * 
 * One for example can map incoming binder to an appropriate component
 */
public inline fun Context.binder(intent: Intent, flags: Int): Observable<IBinder> = RxContext.binder(this, intent, flags)

/**
 * Create an observable of intents received with `android.content.BroadcastReceiver`
 */
public inline fun Context.broadcasts(intentFilter: IntentFilter): Observable<Intent> = RxContext.broadcasts(this, intentFilter)
