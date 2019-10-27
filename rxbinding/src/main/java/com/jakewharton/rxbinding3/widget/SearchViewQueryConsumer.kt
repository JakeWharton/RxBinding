@file:JvmName("RxSearchView")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.widget

import androidx.annotation.CheckResult
import android.widget.SearchView
import io.reactivex.rxjava3.functions.Consumer

/**
 * An action which sets the query property of `view` with character sequences.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * @param submit whether to submit query right after updating query text
 */
@CheckResult
fun SearchView.query(submit: Boolean): Consumer<in CharSequence> {
  return Consumer { text -> setQuery(text, submit) }
}
