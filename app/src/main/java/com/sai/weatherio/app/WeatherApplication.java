package com.sai.weatherio.app;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.sai.weatherio.api.WeatherApiModule;
import com.sai.weatherio.dependency_injection.ApplicationComponent;
import com.sai.weatherio.dependency_injection.ApplicationModule;
import com.sai.weatherio.dependency_injection.DaggerApplicationComponent;
import com.sai.weatherio.main.MainModule;
import com.sai.weatherio.room.RoomModule;

import timber.log.Timber;

/**
 * Created by sai on 2/2/18.
 */

public class WeatherApplication extends Application{

    private ApplicationComponent mApplicationComponent;

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
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .weatherApiModule(new WeatherApiModule())
                .roomModule(new RoomModule(this))
                .mainModule(new MainModule())
                .build();

    }

    private void initializeTimber() {
        Timber.plant(new Timber.DebugTree());
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }
}
