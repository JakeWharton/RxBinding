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
fun <T : Adapter> AdapterView<T>.itemSelections(view: AdapterView<T>): Observable<Int> = RxAdapterView.itemSelections(view)

/**
 * Create an observable of selection events for {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
fun <T : Adapter> AdapterView<T>.selectionEvents(view: AdapterView<T>): Observable<AdapterViewSelectionEvent> = RxAdapterView.selectionEvents(view)

/**
 * Create an observable of the position of item clicks for {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
fun <T : Adapter> AdapterView<T>.itemClicks(view: AdapterView<T>): Observable<Int> = RxAdapterView.itemClicks(view)

/**
 * Create an observable of the item click events for {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
fun <T : Adapter> AdapterView<T>.itemClickEvents(view: AdapterView<T>): Observable<AdapterViewItemClickEvent> = RxAdapterView.itemClickEvents(view)

/**
 * Create an observable of the position of item long-clicks for {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
fun <T : Adapter> AdapterView<T>.itemLongClicks(view: AdapterView<T>): Observable<Int> = RxAdapterView.itemLongClicks(view)
// TODO overload with Func

/**
 * Create an observable of the item long-click events for {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
fun <T : Adapter> AdapterView<T>.itemLongClickEvents(view: AdapterView<T>): Observable<AdapterViewItemLongClickEvent> = RxAdapterView.itemLongClickEvents(view)
// TODO overload with Func

/**
 * An action which sets the selected position of {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
fun <T : Adapter> AdapterView<T>.selection(view: AdapterView<T>): Action1<in Int> = RxAdapterView.setSelection(view)
