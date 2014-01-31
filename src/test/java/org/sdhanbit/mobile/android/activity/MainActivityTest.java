package org.sdhanbit.mobile.android.activity;

import android.app.Activity;
import android.widget.TextView;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.sdhanbit.mobile.android.R;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {
    @org.junit.Test
    public void testSomething() throws Exception {
        Activity activity = Robolectric.buildActivity(MainActivity.class).create().get();
        assertTrue(activity != null);
    }

    @org.junit.Test
    public void greetingSet() throws Exception {
        Activity activity = Robolectric.buildActivity(MainActivity.class).create().get();

        // get a reference to the greeting textview
        TextView txtGreeting = (TextView) activity.findViewById(R.id.textGreeting);
        String greetingInResource = activity.getResources().getString(R.string.greeting);

        assertEquals(greetingInResource, txtGreeting.getText());
    }

}
