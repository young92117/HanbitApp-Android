package org.sdhanbit.mobile.android.activity;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;
import com.google.inject.util.Modules;
import org.sdhanbit.mobile.android.test.TestGuiceModule;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.sdhanbit.mobile.android.R;
import org.sdhanbit.mobile.android.controller.MainActivityController;
import roboguice.RoboGuice;
import roboguice.activity.RoboActivity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {

    protected Application application = mock(Application.class, RETURNS_DEEP_STUBS);
    protected Context context = mock(RoboActivity.class, RETURNS_DEEP_STUBS);

    @MockitoAnnotations.Mock
    MainActivityController controller;

    Activity activity;

    @Before
    public void setup() {

        when(context.getApplicationContext()).thenReturn(application);
        when(application.getApplicationContext()).thenReturn(application);

        activity = Robolectric.buildActivity(MainActivity.class).create().get();

        MockitoAnnotations.initMocks(this);

        TestGuiceModule module = new TestGuiceModule();
        module.addBinding(MainActivityController.class, controller);
        TestGuiceModule.setUp(this, module);

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
    public void navigateToNextPageWhenNextButtonIsClicked() throws Exception {
        Button btnNext = (Button) activity.findViewById(R.id.btnNext);
        btnNext.callOnClick();

        // verify(controller).OnNextButtonClicked(activity);
    }

}
