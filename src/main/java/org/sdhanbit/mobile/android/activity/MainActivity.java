package org.sdhanbit.mobile.android.activity;

import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import org.sdhanbit.mobile.android.R;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

public class MainActivity extends RoboActivity {

    @InjectView(R.id.textGreeting)
    TextView txtGreeting;
    @InjectView(R.id.btnOk)
    Button btnOk;
    @InjectResource(R.string.greeting)
    String greeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the activity title
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        txtGreeting.setText(greeting);
    }
}
