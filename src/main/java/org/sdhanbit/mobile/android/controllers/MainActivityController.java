package org.sdhanbit.mobile.android.controllers;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import org.sdhanbit.mobile.android.activities.NextActivity;

public class MainActivityController {

    public void OnNextButtonClicked(Context context) {
        Intent intent = new Intent(context, NextActivity.class);
        context.startActivity(intent);
    }

    public void OnOkButtonClicked(Context context) {
        Toast.makeText(context, "OK button clicked", Toast.LENGTH_LONG).show();
    }
}
