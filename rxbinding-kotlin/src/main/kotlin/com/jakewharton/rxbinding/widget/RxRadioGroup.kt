package com.jakewharton.rxbinding.widget

import android.widget.RadioGroup
import rx.Observable
import rx.functions.Action1

/**
 * Create an observable of the checked view ID changes in `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
public inline fun RadioGroup.checkedChanges(): Observable<Int> = RxRadioGroup.checkedChanges(this)

/**
 * An action which sets the checked child of `view` with ID. Passing {@code -1} will clear
 * any checked view.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun RadioGroup.checked(): Action1<in Int> = RxRadioGroup.checked(this)
