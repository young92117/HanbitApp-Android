package org.sdhanbit.mobile.android;

import android.content.Context;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import org.sdhanbit.mobile.android.providers.AppProvider;

public class MainModule extends AbstractModule {

    private final MainApplication context;

    @Inject
    public MainModule(final Context context)
    {
        super();
        this.context = (MainApplication)context;
    }

    @Override
    protected void configure() {
        // bind(<interface>.class).to(<implementation>.class);
        // bind(IMainActivityController.class).to(MainActivityController.class);
        bind(AppProvider.class).toInstance(new AppProvider(context));
        bind(MainApplication.class).toProvider(AppProvider.class);
    }
}
