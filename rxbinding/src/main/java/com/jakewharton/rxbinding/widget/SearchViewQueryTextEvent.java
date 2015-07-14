package com.jakewharton.rxbinding.widget;

import android.support.annotation.NonNull;
import android.widget.SearchView;

import com.jakewharton.rxbinding.view.ViewEvent;

public final class SearchViewQueryTextEvent extends ViewEvent<SearchView> {
    private final CharSequence queryText;
    private final boolean submitted;

    protected SearchViewQueryTextEvent(@NonNull SearchView view,
                                       @NonNull CharSequence queryText,
                                       boolean submitted) {
        super(view);
        this.queryText = queryText;
        this.submitted = submitted;
    }

    public CharSequence queryText() {
        return queryText;
    }

    public boolean isSubmitted() {
        return submitted;
    }

    @Override public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof SearchViewQueryTextEvent)) return false;
        SearchViewQueryTextEvent other = (SearchViewQueryTextEvent) o;
        return other.view() == view() && other.queryText.equals(queryText) && other.submitted == submitted;
    }

    @Override public int hashCode() {
        int result = 17;
        result = result * 37 + view().hashCode();
        result = result * 37 + queryText.hashCode();
        result = result * 37 + (submitted ? 1 : 0);
        return result;
    }

    @Override public String toString() {
        return "TextViewEditorActionEvent{view="
                + view()
                + ", queryText="
                + queryText
                + ", submitted="
                + submitted
                + '}';
    }
}
