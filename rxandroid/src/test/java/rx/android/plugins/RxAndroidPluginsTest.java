package rx.android.plugins;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;

public final class RxAndroidPluginsTest {
  @Test public void registeredSchedulersHookIsUsed() {
    RxAndroidPlugins plugins = new RxAndroidPlugins();
    RxAndroidSchedulersHook hook = new RxAndroidSchedulersHook();
    plugins.registerSchedulersHook(hook);
    assertThat(plugins.getSchedulersHook()).isSameAs(hook);
  }

  @Test public void registerSchedulersHookViaSystemProperty() {
    // TODO this test!
  }

  @Test public void registerSchedulersHookTwiceFails() {
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
}
