package com.jakewharton.rxbinding2.internal;

import java.util.concurrent.Callable;
import io.reactivex.functions.Predicate;

public final class Functions {
  private static final Always ALWAYS_TRUE = new Always(true);
  public static final Callable<Boolean> CALLABLE_ALWAYS_TRUE = ALWAYS_TRUE;
  public static final Predicate<Object> PREDICATE_ALWAYS_TRUE = ALWAYS_TRUE;

  private static final class Always implements Callable<Boolean>, Predicate<Object> {
    private final Boolean value;

    Always(Boolean value) {
      this.value = value;
    }

    @Override public Boolean call() {
      return value;
    }

    @Override
    public boolean test(Object t) throws Exception {
      return value;
    }
  }

  private Functions() {
    throw new AssertionError("No instances.");
  }
}
