package rx.android.widget

import android.widget.Adapter
import android.widget.AdapterView
import rx.Observable
import rx.functions.Action1

/**
 * Create an observable of the selected position of {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
public inline fun <T : Adapter> AdapterView<T>.itemSelections(): Observable<Int> = RxAdapterView.itemSelections(this)

/**
 * Create an observable of selection events for {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
public inline fun <T : Adapter> AdapterView<T>.selectionEvents(): Observable<AdapterViewSelectionEvent> = RxAdapterView.selectionEvents(this)

/**
 * Create an observable of the position of item clicks for {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
public inline fun <T : Adapter> AdapterView<T>.itemClicks(): Observable<Int> = RxAdapterView.itemClicks(this)

/**
 * Create an observable of the item click events for {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
public inline fun <T : Adapter> AdapterView<T>.itemClickEvents(): Observable<AdapterViewItemClickEvent> = RxAdapterView.itemClickEvents(this)

/**
 * Create an observable of the position of item long-clicks for {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
public inline fun <T : Adapter> AdapterView<T>.itemLongClicks(): Observable<Int> = RxAdapterView.itemLongClicks(this)
// TODO overload with Func

/**
 * Create an observable of the item long-click events for {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
public inline fun <T : Adapter> AdapterView<T>.itemLongClickEvents(): Observable<AdapterViewItemLongClickEvent> = RxAdapterView.itemLongClickEvents(this)
// TODO overload with Func

/**
 * An action which sets the selected position of {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
public inline fun <T : Adapter> AdapterView<T>.selection(): Action1<in Int> = RxAdapterView.selection(this)
