package com.jakewharton.rxbinding2.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.RatingBar;
import com.jakewharton.rxbinding2.view.ViewEvent;

public final class RatingBarChangeEvent extends ViewEvent<RatingBar> {
  @CheckResult @NonNull
  public static RatingBarChangeEvent create(@NonNull RatingBar view, float rating,
      boolean fromUser) {
    return new RatingBarChangeEvent(view, rating, fromUser);
  }

  private final float rating;
  private final boolean fromUser;

  private RatingBarChangeEvent(@NonNull RatingBar view, float rating, boolean fromUser) {
    super(view);
    this.rating = rating;
    this.fromUser = fromUser;
  }

  public float rating() {
    return rating;
  }

  public boolean fromUser() {
    return fromUser;
  }

  @Override public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof RatingBarChangeEvent)) return false;
    RatingBarChangeEvent other = (RatingBarChangeEvent) o;
    return other.view() == view() && other.rating == rating && other.fromUser == fromUser;
  }

  @Override public int hashCode() {
    int result = 17;
    result = result * 37 + view().hashCode();
    result = result * 37 + Float.floatToIntBits(rating);
    result = result * 37 + (fromUser ? 1 : 0);
    return result;
  }

  @Override public String toString() {
    return "RatingBarChangeEvent{view="
        + view()
        + ", rating="
        + rating
        + ", fromUser="
        + fromUser
        + '}';
  }
}
