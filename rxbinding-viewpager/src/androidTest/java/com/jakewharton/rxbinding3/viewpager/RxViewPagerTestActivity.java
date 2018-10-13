package com.jakewharton.rxbinding3.viewpager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public final class RxViewPagerTestActivity extends Activity {
  ViewPager viewPager;

  @SuppressWarnings("ResourceType")
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    viewPager = new ViewPager(this);
    viewPager.setId(1);
    viewPager.setAdapter(new Adapter());

    setContentView(viewPager);
  }

  private static class Adapter extends PagerAdapter {
    Adapter() {
    }

    @Override public int getCount() {
      return 20;
    }

    @Override public boolean isViewFromObject(View view, Object object) {
      return view == object;
    }

    @Override public Object instantiateItem(ViewGroup container, int position) {
      FrameLayout frameLayout = new FrameLayout(container.getContext());
      container.addView(frameLayout);
      return frameLayout;
    }

    @Override public void destroyItem(ViewGroup container, int position, Object object) {
      container.removeView((View) object);
    }
  }
}
