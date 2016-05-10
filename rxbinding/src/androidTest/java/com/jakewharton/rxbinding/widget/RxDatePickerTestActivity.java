package com.jakewharton.rxbinding.widget;

import android.app.Activity;
import android.os.Bundle;
import android.widget.DatePicker;

public class RxDatePickerTestActivity extends Activity {

    DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        datePicker = new DatePicker(this);
        setContentView(datePicker);
    }

}
