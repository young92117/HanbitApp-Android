package org.sdhanbit.mobile.android.controllers;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import org.sdhanbit.mobile.android.activities.NextActivity;
import org.sdhanbit.mobile.android.activities.TopActivity;

public class MainActivityController {

    public void OnNextButtonClicked(Context context) {
        Intent intent = new Intent(context, NextActivity.class);
        context.startActivity(intent);
    }

    public void OnOkButtonClicked(Context context) {
        Toast.makeText(context, "OK button clicked", Toast.LENGTH_LONG).show();
    }

    public void OnTopButtonClicked(Context context) {
        Intent intent = new Intent(context, TopActivity.class);
        context.startActivity(intent);
    }
}
