package rx.android.widget

import android.widget.ProgressBar
import rx.functions.Action1

/**
 * An action which increments the progress value of {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
fun ProgressBar.incrementProgress(view: ProgressBar): Action1<in Int> = RxProgressBar.incrementProgressBy(view)

/**
 * An action which increments the secondary progress value of {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
fun ProgressBar.incrementSecondaryProgress(view: ProgressBar): Action1<in Int> = RxProgressBar.incrementSecondaryProgressBy(view)

/**
 * An action which sets whether {@code view} is indeterminate.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
fun ProgressBar.indeterminate(view: ProgressBar): Action1<in Boolean> = RxProgressBar.setIndeterminate(view)

/**
 * An action which sets the max value of {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
fun ProgressBar.max(view: ProgressBar): Action1<in Int> = RxProgressBar.setMax(view)

/**
 * An action which sets the progress value of {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
fun ProgressBar.progress(view: ProgressBar): Action1<in Int> = RxProgressBar.setProgress(view)

/**
 * An action which sets the secondary progress value of {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
fun ProgressBar.secondaryProgress(view: ProgressBar): Action1<in Int> = RxProgressBar.setSecondaryProgress(view)
