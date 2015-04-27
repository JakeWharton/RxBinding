package rx.android.widget;

import android.support.annotation.NonNull;
import android.widget.SeekBar;
import rx.android.view.ViewEvent;

public abstract class SeekBarChangeEvent extends ViewEvent<SeekBar> {
  SeekBarChangeEvent(@NonNull SeekBar view) {
    super(view);
  }
}
