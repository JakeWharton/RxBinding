package rx.android.plugins;

import android.support.test.runner.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public final class RxAndroidPluginsTest {
  @Test public void schedulersHookIsUsed() {
    RxAndroidPlugins plugins = new RxAndroidPlugins();
    RxAndroidSchedulersHook hook = new RxAndroidSchedulersHook();
    plugins.registerSchedulersHook(hook);
    assertThat(plugins.getSchedulersHook()).isSameAs(hook);
  }

  public static class MyRxAndroidSchedulersHook extends RxAndroidSchedulersHook {
  }

  @Test public void schedulersHookViaSystemPropertyIsUsed() {
    String name = "rxandroid.plugin.RxAndroidSchedulersHook.implementation";
    try {
      System.setProperty(name, MyRxAndroidSchedulersHook.class.getName());
      RxAndroidPlugins plugins = new RxAndroidPlugins();
      assertThat(plugins.getSchedulersHook()).isInstanceOf(MyRxAndroidSchedulersHook.class);
    } finally {
      System.clearProperty(name);
    }
  }

  @Test public void schedulersHookTwiceFails() {
    RxAndroidPlugins plugins = new RxAndroidPlugins();
    RxAndroidSchedulersHook hook = new RxAndroidSchedulersHook();
    plugins.registerSchedulersHook(hook);
    try {
      plugins.registerSchedulersHook(hook);
      fail();
    } catch (IllegalStateException e) {
      assertThat(e.getMessage()).startsWith("Another strategy was already registered:");
    }
  }

  @Test public void logHookIsUsed() {
    RxAndroidPlugins plugins = new RxAndroidPlugins();
    RxAndroidLogHook hook = new RxAndroidLogHook();
    plugins.registerLogHook(hook);
    assertThat(plugins.getLogHook()).isSameAs(hook);
  }

  public static class MyRxAndroidLogHook extends RxAndroidLogHook {
  }

  @Test public void logHookViaSystemPropertyIsUsed() {
    String name = "rxandroid.plugin.RxAndroidLogHook.implementation";
    try {
      System.setProperty(name, MyRxAndroidLogHook.class.getName());
      RxAndroidPlugins plugins = new RxAndroidPlugins();
      assertThat(plugins.getLogHook()).isInstanceOf(MyRxAndroidLogHook.class);
    } finally {
      System.clearProperty(name);
    }
  }

  @Test public void logHookTwiceFails() {
    RxAndroidPlugins plugins = new RxAndroidPlugins();
    RxAndroidLogHook hook = new RxAndroidLogHook();
    plugins.registerLogHook(hook);
    try {
      plugins.registerLogHook(hook);
      fail();
    } catch (IllegalStateException e) {
      assertThat(e.getMessage()).startsWith("Another strategy was already registered:");
    }
  }
}
