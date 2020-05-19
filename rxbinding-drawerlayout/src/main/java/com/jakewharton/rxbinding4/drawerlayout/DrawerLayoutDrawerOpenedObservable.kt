@file:JvmName("RxDrawerLayout")
@file:JvmMultifileClass

package com.jakewharton.rxbinding4.drawerlayout

import android.view.View
import androidx.annotation.CheckResult
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import com.jakewharton.rxbinding4.InitialValueObservable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.android.MainThreadDisposable

import com.jakewharton.rxbinding4.internal.checkMainThread

/**
 * Create an observable of the open state of the drawer of `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
@CheckResult
fun DrawerLayout.drawerOpen(gravity: Int): InitialValueObservable<Boolean> {
  return DrawerLayoutDrawerOpenedObservable(this, gravity)
}

private class DrawerLayoutDrawerOpenedObservable(
  private val view: DrawerLayout,
  private val gravity: Int
) : InitialValueObservable<Boolean>() {

  override fun subscribeListener(observer: Observer<in Boolean>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, gravity, observer)
    observer.onSubscribe(listener)
    view.addDrawerListener(listener)
  }

  override val initialValue get() = view.isDrawerOpen(gravity)

  private class Listener(
    private val view: DrawerLayout,
    private val gravity: Int,
    private val observer: Observer<in Boolean>
  ) : MainThreadDisposable(), DrawerListener {

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
    }

    override fun onDrawerOpened(drawerView: View) {
      if (!isDisposed) {
        val drawerGravity = (drawerView.layoutParams as DrawerLayout.LayoutParams).gravity
        if (drawerGravity == gravity) {
          observer.onNext(true)
        }
      }
    }

    override fun onDrawerClosed(drawerView: View) {
      if (!isDisposed) {
        val drawerGravity = (drawerView.layoutParams as DrawerLayout.LayoutParams).gravity
        if (drawerGravity == gravity) {
          observer.onNext(false)
        }
      }
    }

    override fun onDrawerStateChanged(newState: Int) {
    }

    override fun onDispose() {
      view.removeDrawerListener(this)
    }
  }
}
