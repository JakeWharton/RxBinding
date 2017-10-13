@file:Suppress(
    names = "NOTHING_TO_INLINE"
)

package com.jakewharton.rxbinding2.widget

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import io.reactivex.functions.Consumer
import kotlin.Int
import kotlin.Suppress

/**
 * An action which sets the src property of `view` with drawable.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
inline fun ImageView.src(): Consumer<in Drawable> = RxImageView.src(this)

/**
 * An action which sets the src property of `view` with resource IDs.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
inline fun ImageView.srcRes(): Consumer<in Int> = RxImageView.srcRes(this)

/**
 * An action which sets the src property of `view` with bitmap.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
inline fun ImageView.srcBitmap(): Consumer<in Bitmap> = RxImageView.srcBitmap(this)
