package com.jakewharton.rxbinding4.recyclerview;

import android.app.Activity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public final class RxRecyclerViewTestActivity extends Activity {
  RecyclerView recyclerView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    recyclerView = new RecyclerView(this);
    recyclerView.setId(android.R.id.primary);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    setContentView(recyclerView);
  }
}
