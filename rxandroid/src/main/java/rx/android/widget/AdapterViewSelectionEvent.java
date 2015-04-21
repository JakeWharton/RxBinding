package rx.android.widget;

import android.support.annotation.NonNull;
import android.widget.AdapterView;
import rx.android.view.ViewEvent;

public abstract class AdapterViewSelectionEvent extends ViewEvent<AdapterView<?>> {
  AdapterViewSelectionEvent(@NonNull AdapterView<?> view) {
    super(view);
  }
}
