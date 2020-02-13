@file:JvmName("RxBottomNavigationView")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.material

import android.view.MenuItem
import androidx.annotation.CheckResult
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jakewharton.rxbinding3.internal.checkMainThread
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

/**
 * Create an observable which emits the reselected item in `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 */
@CheckResult
fun BottomNavigationView.itemReselections(): Observable<MenuItem> {
    return BottomNavigationViewItemReselectionsObservable(this)
}

private class BottomNavigationViewItemReselectionsObservable(
    private val view: BottomNavigationView
) : Observable<MenuItem>() {

    override fun subscribeActual(observer: Observer<in MenuItem>) {
        if (!checkMainThread(observer)) {
            return
        }
        val listener = Listener(view, observer)
        observer.onSubscribe(listener)
        view.setOnNavigationItemReselectedListener(listener)
    }

    private class Listener(
        private val bottomNavigationView: BottomNavigationView,
        private val observer: Observer<in MenuItem>
    ) : MainThreadDisposable(), BottomNavigationView.OnNavigationItemReselectedListener {

        override fun onNavigationItemReselected(item: MenuItem) {
            if (!isDisposed) {
                observer.onNext(item)
            }
        }

        override fun onDispose() {
            bottomNavigationView.setOnNavigationItemReselectedListener(null)
        }
    }
}