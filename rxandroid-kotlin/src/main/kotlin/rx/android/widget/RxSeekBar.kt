package rx.android.widget

import android.widget.SeekBar
import rx.Observable

/**
 * Create an observable of progress value changes on {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
fun SeekBar.changes(view: SeekBar): Observable<Int> = RxSeekBar.changes(view)

/**
 * Create an observable of progress change events for {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
fun SeekBar.changeEvents(view: SeekBar): Observable<SeekBarChangeEvent> = RxSeekBar.changeEvents(view)
