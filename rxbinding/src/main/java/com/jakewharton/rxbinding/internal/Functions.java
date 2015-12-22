package com.jakewharton.rxbinding.internal;

import rx.functions.Func0;
import rx.functions.Func1;

public final class Functions {
  private static final Always<Boolean> ALWAYS_TRUE = new Always<>(true);
  public static final Func0<Boolean> FUNC0_ALWAYS_TRUE = ALWAYS_TRUE;
  public static final Func1<Object, Boolean> FUNC1_ALWAYS_TRUE = ALWAYS_TRUE;

  private static final class Always<T> implements Func1<Object, T>, Func0<T> {
    private final T value;

    Always(T value) {
      this.value = value;
    }

    @Override public T call(Object o) {
      return value;
    }

    @Override public T call() {
      return value;
    }
  }

  private Functions() {
    throw new AssertionError("No instances.");
  }
}
