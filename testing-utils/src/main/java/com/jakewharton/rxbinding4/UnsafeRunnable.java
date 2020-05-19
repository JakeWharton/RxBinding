package com.jakewharton.rxbinding4;

public abstract class UnsafeRunnable implements Runnable {

  @Override
  public final void run() {
    try {
      unsafeRun();
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  protected abstract void unsafeRun() throws Throwable;
}
