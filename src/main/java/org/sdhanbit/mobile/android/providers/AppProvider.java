package org.sdhanbit.mobile.android.providers;

import android.content.Context;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.sdhanbit.mobile.android.MainApplication;

public class AppProvider implements Provider<MainApplication> {

    private MainApplication application;

    @Inject
    public AppProvider(Context context) {
        this.application = (MainApplication)context;
    }

    @Override
    public MainApplication get() {
        return this.application;
    }
}
