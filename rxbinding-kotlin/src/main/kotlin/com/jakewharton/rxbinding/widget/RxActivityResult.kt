package com.jakewharton.rxbinding.widget

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.jakewharton.rxbinding.view.ActivityResultEvent
import rx.Observable

/**
 * Start an activity and get an {@link Observable<ActivityResultEvent>} that only emits the result
 * of the intent started.
 *
 * @param intent   The intent to start.
 */
inline fun Activity.startActivityForResult(intent: Intent): Observable<ActivityResultEvent> = RxActivityResult.startActivityForResult(this, intent)

/**
 * Start an activity and get an {@link Observable<ActivityResultEvent>} that only emits the result
 * of the intent started.
 *
 * @param intent   The intent to start.
 * @param options  Additional options for how the Activity should be started.
 */
inline fun Activity.startActivityForResult(intent: Intent, options: Bundle): Observable<ActivityResultEvent> = RxActivityResult.startActivityForResult(this, intent, options)
