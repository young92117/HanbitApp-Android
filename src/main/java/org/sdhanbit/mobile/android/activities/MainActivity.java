package org.sdhanbit.mobile.android.activities;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import com.google.inject.Inject;
import org.sdhanbit.mobile.android.R;
import org.sdhanbit.mobile.android.controllers.MainActivityController;
import org.sdhanbit.mobile.android.schedulers.RssReaderScheduler;
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
    @InjectView(R.id.btnTop)
    private Button btnTop;
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

        btnTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                controller.OnTopButtonClicked(view.getContext());
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
