package org.sdhanbit.mobile.android.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.VideoView;
import org.sdhanbit.mobile.android.R;

public class SplashActivity extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 5000;

    private boolean alreadyGone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (Integer.valueOf(android.os.Build.VERSION.SDK_INT) < 11) {
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);
            // close this activity
            finish();
        } else {
            final VideoView videoView = (VideoView) findViewById(R.id.splash_video_view);
            String videoName = "splash_video";
            int id = getResources().getIdentifier(videoName, "raw", "org.sdhanbit.mobile.android");
            final String path = "android.resource://" + "org.sdhanbit.mobile.android/" + id;
            videoView.setVideoURI(Uri.parse(path));
            videoView.start();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    closeSplash();
                }
            }, SPLASH_TIME_OUT);
        }
    }

    private void closeSplash() {
        if(!alreadyGone) {
            alreadyGone = true;
            // Start your app main activity
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);
            // close this activity
            finish();

            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            closeSplash();
        }
        return true;
    }
}