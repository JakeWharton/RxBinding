package com.jakewharton.rxbinding.widget

import android.widget.RadioGroup
import rx.Observable
import rx.functions.Action1

/**
 * Create an observable of the checked view ID changes in {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
public inline fun RadioGroup.checkedChanges(): Observable<Int> = RxRadioGroup.checkedChanges(this)

/**
 * Create an observable of the checked view ID change events in {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
public inline fun RadioGroup.checkedChangeEvents(): Observable<RadioGroupCheckedChangeEvent> = RxRadioGroup.checkedChangeEvents(this)

/**
 * An action which sets the checked child of {@code view} with ID. Passing {@code -1} will clear
 * any checked view.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
public inline fun RadioGroup.checked(): Action1<in Int> = RxRadioGroup.checked(this)
