package com.jakewharton.rxbinding2.view;

import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewTreeObserver;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkMainThread;

@RequiresApi(19)
final class ViewScrollChangeEventObservableCompat extends Observable<ViewScrollChangeEvent> {
  private final View view;

  ViewScrollChangeEventObservableCompat(View view) {
    this.view = view;
  }

  @Override
  protected void subscribeActual(Observer<? super ViewScrollChangeEvent> observer) {
    if (!checkMainThread(observer)) {
      return;
    }
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    setOnScrollChangeListenerWith(view, listener);
  }

  private void setOnScrollChangeListenerWith(final View v, final Listener listener) {
    ViewTreeObserver viewTreeObserver = v.getViewTreeObserver();
    viewTreeObserver.addOnScrollChangedListener(
            new ViewTreeObserver.OnScrollChangedListener() {
              private int oldl, oldt;

              @Override
              public void onScrollChanged() {
                listener.onScrollChange(v, v.getScrollX(), v.getScrollY(), oldl, oldt);
                oldl = v.getScrollX();
                oldt = v.getScrollY();
              }
            }
    );
  }

  static final class Listener extends MainThreadDisposable implements OnScrollChangeListener {
    private final View view;
    private final Observer<? super ViewScrollChangeEvent> observer;

    Listener(View view, Observer<? super ViewScrollChangeEvent> observer) {
      this.view = view;
      this.observer = observer;
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
      if (!isDisposed()) {
        observer.onNext(ViewScrollChangeEvent.create(v, scrollX, scrollY, oldScrollX, oldScrollY));
      }
    }

    @Override protected void onDispose() {
      view.setOnClickListener(null);
    }
  }

  public interface OnScrollChangeListener {
    /**
     * Called when the scroll position of a view changes.
     *
     * @param v The view whose scroll position has changed.
     * @param scrollX Current horizontal scroll origin.
     * @param scrollY Current vertical scroll origin.
     * @param oldScrollX Previous horizontal scroll origin.
     * @param oldScrollY Previous vertical scroll origin.
     */
    void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY);
  }
}
