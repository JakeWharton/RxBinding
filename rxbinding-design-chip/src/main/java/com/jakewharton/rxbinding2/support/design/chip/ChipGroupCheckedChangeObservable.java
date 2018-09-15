package com.jakewharton.rxbinding2.support.design.chip;

import android.support.design.chip.ChipGroup;
import android.support.design.chip.ChipGroup.OnCheckedChangeListener;
import com.jakewharton.rxbinding2.InitialValueObservable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkMainThread;

final class ChipGroupCheckedChangeObservable extends InitialValueObservable<Integer> {
    private final ChipGroup view;

    ChipGroupCheckedChangeObservable(ChipGroup view) {
        this.view = view;
    }

    @Override
    protected void subscribeListener(Observer<? super Integer> observer) {
        if (!checkMainThread(observer)) {
            return;
        }
        Listener listener = new Listener(view, observer);
        view.setOnCheckedChangeListener(listener);
        observer.onSubscribe(listener);
    }

    @Override
    protected Integer getInitialValue() {
        return view.getCheckedChipId();
    }

    static final class Listener extends MainThreadDisposable implements OnCheckedChangeListener {
        private final ChipGroup view;
        private final Observer<? super Integer> observer;
        private int lastChecked = -1;

        Listener(ChipGroup view, Observer<? super Integer> observer) {
            this.view = view;
            this.observer = observer;
        }

        @Override
        public void onCheckedChanged(ChipGroup chipGroup, int checkedId) {
            if (!isDisposed() && checkedId != lastChecked) {
                lastChecked = checkedId;
                observer.onNext(checkedId);
            }
        }

        @Override
        protected void onDispose() {
            view.setOnCheckedChangeListener(null);
        }
    }
}
