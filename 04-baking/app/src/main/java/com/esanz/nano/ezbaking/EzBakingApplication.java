package com.esanz.nano.ezbaking;

import android.app.Application;

import timber.log.Timber;

public class EzBakingApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
