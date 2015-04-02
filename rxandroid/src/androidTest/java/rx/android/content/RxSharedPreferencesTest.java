package rx.android.content;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import rx.Subscription;
import rx.android.RecordingObserver;

import static android.content.Context.MODE_PRIVATE;
import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public final class RxSharedPreferencesTest {
  private final Context context = InstrumentationRegistry.getContext();
  private final SharedPreferences prefs = context.getSharedPreferences("test", MODE_PRIVATE);
  private final RxSharedPreferences rxPrefs = RxSharedPreferences.create(prefs);

  @Before public void setUp() {
    prefs.edit().clear().apply();
  }

  @Test public void initialTrigger() {
    prefs.edit().putString("key", "value").apply();
    RecordingObserver<String> o = new RecordingObserver<>();
    rxPrefs.getString("key").subscribe(o);
    assertThat(o.takeNext()).isEqualTo("value");
    o.assertNoMoreEvents();
  }

  @Test public void changeTrigger() {
    RecordingObserver<String> o = new RecordingObserver<>();
    rxPrefs.getString("key").subscribe(o);
    assertThat(o.takeNext()).isNull();
    prefs.edit().putString("key", "value").apply();
    assertThat(o.takeNext()).isEqualTo("value");
    o.assertNoMoreEvents();
  }

  @Test public void multipleChangesTrigger() {
    RecordingObserver<String> o1 = new RecordingObserver<>();
    rxPrefs.getString("key1").subscribe(o1);
    assertThat(o1.takeNext()).isNull();
    RecordingObserver<String> o2 = new RecordingObserver<>();
    rxPrefs.getString("key2").subscribe(o2);
    assertThat(o2.takeNext()).isNull();

    prefs.edit().putString("key1", "value1").putString("key2", "value2").apply();

    assertThat(o1.takeNext()).isEqualTo("value1");
    o1.assertNoMoreEvents();
    assertThat(o2.takeNext()).isEqualTo("value2");
    o2.assertNoMoreEvents();
  }

  @Test public void unsubscribeDoesNotTrigger() {
    RecordingObserver<String> o = new RecordingObserver<>();
    Subscription s = rxPrefs.getString("key").subscribe(o);
    assertThat(o.takeNext()).isNull();
    s.unsubscribe();
    prefs.edit().putString("key", "value").apply();
    o.assertNoMoreEvents();
  }

  @Test public void observeBoolean() {
    prefs.edit().putBoolean("key", true).apply();
    RecordingObserver<Boolean> o = new RecordingObserver<>();
    rxPrefs.getBoolean("key").subscribe(o);
    assertThat(o.takeNext()).isTrue();
    o.assertNoMoreEvents();
  }

  @Test public void observeBooleanWithDefault() {
    RecordingObserver<Boolean> o = new RecordingObserver<>();
    rxPrefs.getBoolean("key", true).subscribe(o);
    assertThat(o.takeNext()).isTrue();
    o.assertNoMoreEvents();
  }

  @Test public void subscribeBoolean() {
    rxPrefs.setBoolean("key").call(true);
    assertThat(prefs.getBoolean("key", false)).isTrue();
  }

  @Test public void observeFloat() {
    prefs.edit().putFloat("key", 1f).apply();
    RecordingObserver<Float> o = new RecordingObserver<>();
    rxPrefs.getFloat("key").subscribe(o);
    assertThat(o.takeNext()).isEqualTo(1f);
    o.assertNoMoreEvents();
  }

  @Test public void observeFloatWithDefault() {
    RecordingObserver<Float> o = new RecordingObserver<>();
    rxPrefs.getFloat("key", 1f).subscribe(o);
    assertThat(o.takeNext()).isEqualTo(1f);
    o.assertNoMoreEvents();
  }

  @Test public void subscribeFloat() {
    rxPrefs.setFloat("key").call(1f);
    assertThat(prefs.getFloat("key", 0f)).isEqualTo(1f);
  }

  @Test public void observeInt() {
    prefs.edit().putInt("key", 1).apply();
    RecordingObserver<Integer> o = new RecordingObserver<>();
    rxPrefs.getInt("key").subscribe(o);
    assertThat(o.takeNext()).isEqualTo(1);
    o.assertNoMoreEvents();
  }

  @Test public void observeIntWithDefault() {
    RecordingObserver<Integer> o = new RecordingObserver<>();
    rxPrefs.getInt("key", 1).subscribe(o);
    assertThat(o.takeNext()).isEqualTo(1);
    o.assertNoMoreEvents();
  }

  @Test public void subscribeInt() {
    rxPrefs.setInt("key").call(1);
    assertThat(prefs.getInt("key", 0)).isEqualTo(1);
  }

  @Test public void observeString() {
    prefs.edit().putString("key", "value").apply();
    RecordingObserver<String> o = new RecordingObserver<>();
    rxPrefs.getString("key").subscribe(o);
    assertThat(o.takeNext()).isEqualTo("value");
    o.assertNoMoreEvents();
  }

  @Test public void observeStringWithDefault() {
    RecordingObserver<String> o = new RecordingObserver<>();
    rxPrefs.getString("key", "default").subscribe(o);
    assertThat(o.takeNext()).isEqualTo("default");
    o.assertNoMoreEvents();
  }

  @Test public void subscribeString() {
    rxPrefs.setString("key").call("value");
    assertThat(prefs.getString("key", null)).isEqualTo("value");
  }
}
