package com.sai.weatherio.dependency_injection;

import android.app.Application;

import dagger.Module;

/**
 * Created by sai on 2/2/18.
 */

@Module
public abstract class ApplicationModule {

    private Application mApplication;

    public ApplicationModule(Application application) {
        this.mApplication = application;
    }
}
