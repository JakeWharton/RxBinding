Change Log
==========

Version 0.4.0 *(2016-02-18)*
----------------------------

 * New bindings!
    * `rxbinding`:
       * `RxAbsListView`:
          * `scrollEvents` - List scroll events.
       * `RxAutoCompleteTextView`:
          * `completionHint` - Sets the hint text at the bottom of the suggestion list.
          * `threshold` - Sets the minimum number of characters before suggestions are shown.
       * `RxPopupMenu`:
          * `itemClicks` - Menu item clicks.
          * `dismisses` - Menu item dismissal.
    * `rxbinding-appcompat-v7`:
       * `RxActionMenuView`:
          * `itemClicks` - Menu item clicks.
       * `RxPopupMenu`:
          * `itemClicks` - Menu item clicks.
          * `dismisses` - Menu item dismissal.
    * `rxbinding-design`:
       * `RxTextInputLayout`:
          * `error` - Sets the error text for the text input.
          * `errorRes` - Sets the error text resource for the text input.
    * `rxbinding-support-v4`:
       * `RxMenuItemCompat`:
          * `actionViewEvents`- Menu item action view events.
 * Update Kotlin modules dependency to v1.0.0.
 * Fix: Remove 76 needless synthetic accessor methods.


Version 0.3.0 *(2015-10-22)*
----------------------------

 * New modules!
    * `rxbinding-leanback-v17` (and `rxbinding-leanback-v17-kotlin`) for the 'Leanback Support Library':
       * `RxSearchBar`:
          * `searchQuery` - Sets the query text.
          * `searchQueryChanges` - Query text changes.
          * `searchQueryChangeEvents` - Query text change events.
       * `RxSearchEditText`:
          * `keyboardDismisses` - Keyboard dismisses.
 * New bindings!
    * `rxbinding`:
       * `RxAutoCompleteTextView`:
          * `itemClickEvents` - Suggestion item clicks.
       * `RxCheckedTextView`:
          * `checked` - Sets the checked state.
       * `RxMenuItem`:
          * `actionViewEvents` - Action view expand and collapse events.
          * `checked` - Sets the checked state.
          * `clicks` - Item clicks.
          * `enabled` - Sets the enabled state.
          * `icon` - Sets the icon.
          * `iconRes` - Sets the icon using a resource.
          * `title` - Sets the title.
          * `titleRes` - Sets the title using a resources.
          * `visible` - Sets the visible state.
       * `RxSeekBar`:
          * `userChanges` - Only user value changes.
          * `systemChanges` - Only system value changes.
       * `RxTextView`:
          * `afterTextChangeEvents` - After text change events.
          * `beforeTextChangeEvents` - Before text change events.
          * `color` - Sets the text color.
          * `error` - Sets the error text.
          * `errorRes` - Sets the error text using a resource.
          * `hint` - Sets the hint text.
          * `hintRes` - Sets the hint text using a resource.
       * `RxToolbar`:
          * `navigationClicks` - Clicks on the navigation view.
       * `RxView`:
          * `draws` - ViewTreeObserver draw events.
          * `globalLayouts` - ViewTreeObserver global layout events.
          * `hovers` - View hover `MotionEvent`.
          * `hoverEvents` - View hover `MotionEvent` events.
          * `layoutChanges` - ViewTreeObserver layout changes.
          * `layoutChangeEvents` - ViewTreeObserver layout change events.
          * `preDraws` - ViewTreeObserver pre-draw events.
          * `scrollChangeEvents` - Scroll offset change events.
          * `systemUiVisibilityChanges` - System UI visibility changes.
       * `RxViewGroup`:
          * `changeEvents` - Child add and remove events.
    * `rxbinding-appcompat-v7`:
       * `RxToolbar`:
          * `navigationClicks` - Clicks on the navigation view.
    * `rxbinding-design`:
       * `RxAppBarLayout`:
          * `offsetChanges` - Offset value changes.
       * `RxSwipeDismissBehavior`:
          * `dismisses` - Dismiss events.
       * `RxTextInputLayoutTest`:
          * `counterEnabled` - Sets the enabled state of the counter.
          * `counterMaxLength` - Sets the maximum length of the counter.
          * `hint` - Sets the hint text.
          * `hintRest` - Sets the hint text using a resource.
    * `rxbinding-recyclerview-v7`:
       * `RxRecyclerView`:
          * `scrollStateChanges` - Scroll state changes (dragging, settling, idling).
       * `RxRecyclerViewAdapter`:
          * `childAttachStateChangeEvents` - Child attach and detach events.
          * `dataChanges` - Adapter data changes.
    * `rxbinding-support-v4`:
       * `RxViewPager`:
          * `pageSelections` - Page index selections.
 * Update Kotlin modules dependency to v1.0.0-beta-1038.
 * Update support library dependencies to v23.1.0.
 * Fix: Bindings whose values are irrelevant now use `Void` instead of `Object` (and `Unit` in
   Kotlin).
 * Fix: Remove binding overloads which provided event objects that included no additional
   information (aside from the source view). To include the source view for an event, add it with
   the `map` operator (e.g., `RxFoo.bar(sourceView).map(data -> new Pair<>(sourceView, data))`).
    * `RxCompoundButton.checkedChangeEvents`
    * `RxRadioGroup.checkedChangeEvents`
    * `RxRecyclerView.scrollStateChangeEvents`
    * `RxView.clickEvents`
    * `RxView.dragEvents`
    * `RxView.focusChangeEvents`
    * `RxView.hoverEvents`
    * `RxView.longClickEvents`
    * `RxView.touchEvents`


Version 0.2.0 *(2015-08-23)*
----------------------------

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
 * Added `@CheckResult` to methods which will generate lint warnings if the return value is ignored.
 * Added `@NonNull` to methods to indicate to lint that they will never return null and added to parameters
   to indicate to lint for which arguments is null not allowed. Explicit null checks have been removed.
 * Update Kotlin modules to Kotlin v0.12.1230.
 * Update support library dependencies to v23.0.0.
 * Minimum SDK version is now 14 because reasons.
 * Fix: Kotlin modules now have correct dependencies on the corresponding Java modules.


Version 0.1.0 *(2015-08-02)*
----------------------------

Initial release.
