package com.jakewharton.rxbinding.support.v7.widget;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

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
