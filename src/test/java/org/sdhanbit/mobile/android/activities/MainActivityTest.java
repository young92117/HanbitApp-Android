package org.sdhanbit.mobile.android.activities;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;
import org.sdhanbit.mobile.android.test.TestGuiceModule;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.sdhanbit.mobile.android.R;
import roboguice.RoboGuice;
import roboguice.activity.RoboActivity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {

    private Application application = mock(Application.class, RETURNS_DEEP_STUBS);
    private Context context = mock(RoboActivity.class, RETURNS_DEEP_STUBS);

    private Activity activity;

    @Before
    public void setup() {

        when(context.getApplicationContext()).thenReturn(application);
        when(application.getApplicationContext()).thenReturn(application);

        TestGuiceModule module = new TestGuiceModule();
        TestGuiceModule.setUp(this, module);

        activity = Robolectric.buildActivity(MainActivity.class).create().get();
    }

    @After
    public void teardown() {
        // Don't forget to tear down our custom injector to avoid polluting other test classes
        RoboGuice.util.reset();
        TestGuiceModule.tearDown();
    }

    @org.junit.Test
    public void testSomething() throws Exception {
        assertTrue(activity != null);
    }

    @org.junit.Test
    public void greetingIsSetUponInitialization() throws Exception {
        // get a reference to the greeting textview
        TextView txtGreeting = (TextView) activity.findViewById(R.id.textGreeting);
        String greetingInResource = activity.getResources().getString(R.string.greeting);

        assertEquals(greetingInResource, txtGreeting.getText());
    }

    @org.junit.Test
    public void navigateToNextActivityWhenNextButtonIsClicked() throws Exception {
        Button btnNext = (Button) activity.findViewById(R.id.btnNext);
        btnNext.callOnClick();

        ShadowActivity shadowActivity = Robolectric.shadowOf(activity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = Robolectric.shadowOf(startedIntent);

        assertEquals(shadowIntent.getIntentClass(), NextActivity.class);
    }

}
