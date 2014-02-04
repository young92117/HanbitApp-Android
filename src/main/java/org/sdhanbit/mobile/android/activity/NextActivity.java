package org.sdhanbit.mobile.android.activity;

import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import org.sdhanbit.mobile.android.R;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

public class NextActivity extends RoboActivity {

    @InjectView(R.id.txtHeader)
    TextView txtHeader;
    @InjectResource(R.string.nextPageHeader)
    String nextPageHeader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the activity title
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.next);

        txtHeader.setText(nextPageHeader);

    }
}
