package com.sai.weatherio.app;

import android.app.Activity;
import android.app.Application;

import com.facebook.stetho.Stetho;
import com.sai.weatherio.dependency_injection.DaggerApplicationComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import timber.log.Timber;

/**
 * Created by sai on 2/2/18.
 */

public class WeatherApplication extends Application implements HasActivityInjector{

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();

        initializeDagger();
        initializeStetho();
        initializeTimber();
    }

    private void initializeStetho() {
        Stetho.initializeWithDefaults(this);
    }

    private void initializeDagger() {
        DaggerApplicationComponent.create().inject(this);
    }

    private void initializeTimber() {
        Timber.plant(new Timber.DebugTree());
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
