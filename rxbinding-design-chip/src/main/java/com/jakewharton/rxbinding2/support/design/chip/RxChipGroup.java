package com.jakewharton.rxbinding2.support.design.chip;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.design.chip.ChipGroup;
import android.view.View;
import com.jakewharton.rxbinding2.InitialValueObservable;
import io.reactivex.functions.Consumer;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkNotNull;

public final class RxChipGroup {
    /**
     * Create an observable of the checked chip ID changes in {@code view}.
     * <p>
     * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
     * to free this reference.
     * </p>
     * <p>
     * <em>Note:</em> A value will be emitted immediately on subscribe
     * </p>
     */
    @CheckResult @NonNull
    public static InitialValueObservable<Integer> checkedChanges(@NonNull ChipGroup view) {
        checkNotNull(view, "view == null");
        return new ChipGroupCheckedChangeObservable(view);
    }

    /**
     * An action which sets the checked child of {@code view} with ID. Passing {@code View.NO_ID} will clear
     * any checked view.
     * <p>
     * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
     * to free this reference
     * </p>
     */
    @CheckResult @NonNull
    public static Consumer<? super Integer> checked(@NonNull final ChipGroup view) {
        checkNotNull(view, "view == null");
        return new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                if (value == View.NO_ID) {
                    view.clearCheck();
                } else {
                    view.check(value);
                }
            }
        };
    }

    private RxChipGroup() {
        throw new AssertionError("No instances.");
    }
}
