Change Log
==========

Version 0.2.0 *(In Development)*
--------------------------------

 * New modules!
    * `rxbinding-appcompat-v7` (and `rxbinding-appcompat-v7-kotlin`) for the 'AppCompat Support Library':
       * `RxSearchView`:
          * `queryTextChanges` - Query text changes.
          * `queryTextChangeEvents` - Query text change events.
          * `query` - Sets the query text.
       * `RxToolbar`:
          * `itemClicks` - Item clicks.
    * `rxbinding-design` (and `rxbinding-design-kotlin`) for the 'Design Support Library':
       * `RxNavigationView`:
          * `itemSelections` - Item selections.
       * `RxSnackbar`:
          * `dismisses` - Dismiss events.
       * `RxTabLayout`:
          * `selections` - Tab selections.
          * `selectionEvents` - Tab selection, reselection, and unselection events.
          * `select` - Sets the selected tab.
    * `rxbinding-recyclerview-v7` (and `rxbinding-recyclerview-v7-kotlin`) for the 'RecyclerView Support Library':
       * `RxRecyclerView`:
          * `scrollEvents` - Scroll events.
          * `scrollStateChangeEvents` - Scroll state change events (dragging, settling, idling).
 * New bindings!
    * `rxbinding`:
       * `RxSearchView`:
          * `queryTextChanges` - Query text changes.
          * `queryTextChangeEvents` - Query text change events.
          * `query` - Sets the query text.
       * `RxSwipeRefreshLayout`:
          * `refreshes` - Refresh events.
          * `refreshing` - Whether showing the refreshing indicator.
       * `RxTextSwitcher`:
          * `text` - Sets the text to display.
          * `currentText` - Sets the current displayed text.
       * `RxToolbar`:
          * `itemClicks` - Item clicks.
       * `RxView`:
          * `attaches` - Attach indication.
          * `attachEvents` - Attach and detach events.
          * `detaches` - Detach indication.
 * Update Kotlin modules to Kotlin v0.12.1230
 * Fix: Kotlin modules now have correct dependencies on the corresponding Java modules.


Version 0.1.0 *(2015-08-02)*
----------------------------

Initial release.
