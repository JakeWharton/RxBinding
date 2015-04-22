package rx.android.plugins;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public final class PluginResetRule implements TestRule {
  @Override public Statement apply(final Statement base, Description description) {
    return new Statement() {
      @Override public void evaluate() throws Throwable {
        RxAndroidPlugins plugins = RxAndroidPlugins.getInstance();

        plugins.reset();
        try {
          base.evaluate();
        } finally {
          plugins.reset();
        }
      }
    };
  }
}
