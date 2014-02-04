package org.sdhanbit.mobile.android;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Module;
import org.sdhanbit.mobile.android.controller.IMainActivityController;
import org.sdhanbit.mobile.android.controller.MainActivityController;
import roboguice.RoboGuice;

public class MainModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IMainActivityController.class).to(MainActivityController.class);
    }

    /*
    @Override
    public void configure(Binder binder) {
        // custom binding here
        binder.bind(IMainActivityController.class).to(MainActivityController.class);
    }
    */
}
