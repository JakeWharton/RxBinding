package com.jakewharton.rxbinding.view;

import android.content.Intent;
import android.support.annotation.Nullable;

public class ActivityResultEvent {

    private final int mResultCode;
    private final Intent mData;

    public ActivityResultEvent(int resultCode, Intent data) {
        mResultCode = resultCode;
        mData = data;
    }

    public int getResultCode() {
        return mResultCode;
    }

    @Nullable
    public Intent getData() {
        return mData;
    }

}
