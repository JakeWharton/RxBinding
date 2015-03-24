package rx.android.internal;

import rx.functions.Func1;

public final class Functions {
  public static Func1<Object, Boolean> ALWAYS_TRUE = new Always<>(true);

  private static final class Always<T> implements Func1<Object, T> {
    private final T value;

    private Always(T value) {
      this.value = value;
    }

    @Override public T call(Object o) {
      return value;
    }
  }

  private Functions() {
    throw new AssertionError("No instances.");
  }
}
