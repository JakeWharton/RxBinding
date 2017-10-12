package com.jakewharton.rxbinding2.widget;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import io.reactivex.functions.Consumer;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkNotNull;

/**
 * Static factory methods for creatng {@linkplain Consumer actions} for {@link ImageView}
 */
public final class RxImageView {
  /**
   * An action which sets the src property of {@code view} with drawable.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Consumer<? super Drawable> src(@NonNull final ImageView view) {
    checkNotNull(view, "view == null");
    return new Consumer<Drawable>() {
      @Override public void accept(Drawable drawable) throws Exception {
        view.setImageDrawable(drawable);
      }
    };
  }

  /**
   * An action which sets the src property of {@code view} src resource IDs.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Consumer<? super Integer> srcRes(@NonNull final ImageView view) {
    checkNotNull(view, "view == null");
    return new Consumer<Integer>() {
      @Override public void accept(Integer srcRes) throws Exception {
        view.setImageResource(srcRes);
      }
    };
  }

  /**
   * An action which sets the src property of {@code view} with bitmap.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Consumer<? super Bitmap> srcBitmap(@NonNull final ImageView view) {
    checkNotNull(view, "view == null");
    return new Consumer<Bitmap>() {
      @Override public void accept(Bitmap bitmap) throws Exception {
        view.setImageBitmap(bitmap);
      }
    };
  }

  private RxImageView() {
    throw new AssertionError("No instances.");
  }
}