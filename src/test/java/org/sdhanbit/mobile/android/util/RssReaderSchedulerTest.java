package org.sdhanbit.mobile.android.util;

import android.app.AlarmManager;
import android.app.Application;
import android.content.Context;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowAlarmManager;
import org.sdhanbit.mobile.android.rss.RssReaderScheduler;
import org.sdhanbit.mobile.android.test.TestGuiceModule;
import roboguice.RoboGuice;
import roboguice.activity.RoboActivity;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
public class RssReaderSchedulerTest {
    private Application application = mock(Application.class, RETURNS_DEEP_STUBS);
    private Context context = mock(RoboActivity.class, RETURNS_DEEP_STUBS);

    private AlarmManager mockAlarmManager;
    private ShadowAlarmManager shadowAlarmManager;

    private RssReaderScheduler rssReaderScheduler;

    @Before
    public void setup() {

        when(context.getApplicationContext()).thenReturn(application);
        when(application.getApplicationContext()).thenReturn(application);

        MockitoAnnotations.initMocks(this);

        mockAlarmManager = (AlarmManager) Robolectric.application.getSystemService(Context.ALARM_SERVICE);
        shadowAlarmManager = Robolectric.shadowOf(mockAlarmManager);

        rssReaderScheduler = new RssReaderScheduler();

        TestGuiceModule module = new TestGuiceModule();
        module.addBinding(AlarmManager.class, mockAlarmManager);

        TestGuiceModule.setUp(this, module);
        TestGuiceModule.setUp(rssReaderScheduler, module);
    }

    @After
    public void teardown() {
        // Don't forget to tear down our custom injector to avoid polluting other test classes
        RoboGuice.util.reset();
        TestGuiceModule.tearDown();
    }

    @Test
    public void schedulesAnAlarm() {
        assertNull(shadowAlarmManager.getNextScheduledAlarm());
        rssReaderScheduler.setAlarm(context);
        assertNotNull(shadowAlarmManager.getNextScheduledAlarm());
    }

    @Test
    public void cancelAnAlarm() {
        assertNull(shadowAlarmManager.getNextScheduledAlarm());
        rssReaderScheduler.setAlarm(context);
        assertNotNull(shadowAlarmManager.getNextScheduledAlarm());

        rssReaderScheduler.cancelAlarm(context);
        assertNull(shadowAlarmManager.getNextScheduledAlarm());
    }
}
