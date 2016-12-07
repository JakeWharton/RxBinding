package com.jakewharton.rxbinding2.internal;

import io.reactivex.functions.Function;
import java.util.concurrent.Callable;

public final class Functions {
  private static final Always<Boolean> ALWAYS_TRUE = new Always<>(true);
  public static final Callable<Boolean> CALLABLE_ALWAYS_TRUE = ALWAYS_TRUE;
  public static final Function<Object, Boolean> FUNCTION_ALWAYS_TRUE = ALWAYS_TRUE;

  private static final class Always<T> implements Function<Object, T>, Callable<T> {
    private final T value;

    Always(T value) {
      this.value = value;
    }

    @Override public T apply(Object o) {
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
