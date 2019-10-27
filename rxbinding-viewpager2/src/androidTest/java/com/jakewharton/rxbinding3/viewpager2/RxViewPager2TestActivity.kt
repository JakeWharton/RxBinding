package com.jakewharton.rxbinding3.viewpager2

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

class RxViewPager2TestActivity : Activity() {

  private lateinit var viewPager2: ViewPager2

  fun getViewPager2(): ViewPager2 =
    viewPager2

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewPager2 = ViewPager2(this).apply {
      id = PAGER_ID
      adapter =
        TestAdapter(RAINBOW)
    }
    setContentView(viewPager2)
  }

  private class TestAdapter(private val colors: List<Int>) : RecyclerView.Adapter<TestViewHolder>() {

    override fun onCreateViewHolder(
      parent: ViewGroup,
      viewType: Int
    ): TestViewHolder =
      TestViewHolder(View(parent.context).apply {
        layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
      })

    override fun getItemCount(): Int =
      colors.size

    override fun onBindViewHolder(
      holder: TestViewHolder,
      position: Int
    ) {
      holder.itemView.setBackgroundResource(colors[position])
    }
  }

  companion object {
    const val PAGER_ID = Int.MAX_VALUE
    val RAINBOW = listOf(
        android.R.color.holo_red_light,
        android.R.color.holo_orange_dark,
        android.R.color.holo_orange_light,
        android.R.color.holo_green_light,
        android.R.color.holo_blue_light,
        android.R.color.holo_blue_dark,
        android.R.color.holo_purple
    )
  }

  private class TestViewHolder(item: View) : RecyclerView.ViewHolder(item)
}
