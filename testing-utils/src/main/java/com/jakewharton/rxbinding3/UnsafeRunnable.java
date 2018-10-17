package com.jakewharton.rxbinding3;

public abstract class UnsafeRunnable implements Runnable {

  @Override
  public final void run() {
    try {
      unsafeRun();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  protected abstract void unsafeRun() throws Exception;
}
