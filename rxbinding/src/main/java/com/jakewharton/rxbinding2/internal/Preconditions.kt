/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:JvmName("Preconditions")

package com.jakewharton.rxbinding2.internal

import android.os.Looper
import io.reactivex.Observer
import io.reactivex.disposables.Disposables

inline fun checkNotNull(value: Any?, message: String) {
  checkNotNull(value) { message }
}

fun checkMainThread(observer: Observer<*>): Boolean {
  if (Looper.myLooper() != Looper.getMainLooper()) {
    observer.onSubscribe(Disposables.empty())
    observer.onError(IllegalStateException(
        "Expected to be called on the main thread but was " + Thread.currentThread().name))
    return false
  }
  return true
}
