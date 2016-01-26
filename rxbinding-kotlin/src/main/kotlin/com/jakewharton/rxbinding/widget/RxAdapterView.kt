package com.jakewharton.rxbinding.widget

import android.widget.Adapter
import android.widget.AdapterView
import com.jakewharton.rxbinding.internal.Functions
import rx.Observable
import rx.functions.Action1
import rx.functions.Func0
import rx.functions.Func1

/**
 * Create an observable of the selected position of `view`. If nothing is selected,
 * [AdapterView.INVALID_POSITION] will be emitted.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
public inline fun <T : Adapter> AdapterView<T>.itemSelections(): Observable<Int> = RxAdapterView.itemSelections(this)

/**
 * Create an observable of selection events for `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
public inline fun <T : Adapter> AdapterView<T>.selectionEvents(): Observable<AdapterViewSelectionEvent> = RxAdapterView.selectionEvents(this)

/**
 * Create an observable of the position of item clicks for `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun <T : Adapter> AdapterView<T>.itemClicks(): Observable<Int> = RxAdapterView.itemClicks(this)

/**
 * Create an observable of the item click events for `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun <T : Adapter> AdapterView<T>.itemClickEvents(): Observable<AdapterViewItemClickEvent> = RxAdapterView.itemClickEvents(this)

/**
 * Create an observable of the position of item long-clicks for `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun <T : Adapter> AdapterView<T>.itemLongClicks(): Observable<Int> = RxAdapterView.itemLongClicks(this)

/**
 * Create an observable of the position of item long-clicks for `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * @param handled Function invoked each occurrence to determine the return value of the
 * underlying [AdapterView.OnItemLongClickListener].
 */
public inline fun <T : Adapter> AdapterView<T>.itemLongClicks(handled: Func0<Boolean>): Observable<Int> = RxAdapterView.itemLongClicks(this, handled)

/**
 * Create an observable of the item long-click events for `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun <T : Adapter> AdapterView<T>.itemLongClickEvents(): Observable<AdapterViewItemLongClickEvent> = RxAdapterView.itemLongClickEvents(this)

/**
 * Create an observable of the item long-click events for `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * @param handled Function invoked with each value to determine the return value of the
 * underlying [AdapterView.OnItemLongClickListener].
 */
public inline fun <T : Adapter> AdapterView<T>.itemLongClickEvents(handled: Func1<in AdapterViewItemLongClickEvent, Boolean>): Observable<AdapterViewItemLongClickEvent> = RxAdapterView.itemLongClickEvents(this, handled)

/**
 * An action which sets the selected position of `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun <T : Adapter> AdapterView<T>.selection(): Action1<in Int> = RxAdapterView.selection(this)
