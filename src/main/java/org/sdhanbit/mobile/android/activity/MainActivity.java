package org.sdhanbit.mobile.android.activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import com.google.inject.Inject;
import org.sdhanbit.mobile.android.R;
import org.sdhanbit.mobile.android.controller.MainActivityController;
import org.sdhanbit.mobile.android.rss.RssReaderScheduler;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

public class MainActivity extends RoboActivity {

    @Inject
    private MainActivityController controller;
    @Inject
    private RssReaderScheduler mRssReaderScheduler;

    @InjectView(R.id.textGreeting)
    private TextView txtGreeting;
    @InjectView(R.id.btnOk)
    private Button btnOk;
    @InjectView(R.id.btnNext)
    private Button btnNext;
    @InjectResource(R.string.greeting)
    private String greeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the activity title
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        txtGreeting.setText(greeting);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                controller.OnNextButtonClicked(view.getContext());
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                controller.OnOkButtonClicked(view.getContext());
            }
        });

        mRssReaderScheduler.setAlarm(this);
    }

    @Override
    protected void onStop() {
        mRssReaderScheduler.cancelAlarm(this);
        super.onStop();
    }
}
