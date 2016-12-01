package com.jakewharton.rxbinding.widget;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TimePicker;

public final class RxTimePickerTestActivity extends Activity {

    TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        timePicker = new TimePicker(this);
        setContentView(timePicker);
    }
}
