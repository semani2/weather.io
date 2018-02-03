package com.sai.weatherio.dependency_injection;

import com.sai.weatherio.api.WeatherApiModule;
import com.sai.weatherio.app.WeatherApplication;
import com.sai.weatherio.room.RoomModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;

/**
 * Created by sai on 2/2/18.
 */

@Singleton
@Component(modules = {AndroidInjectionModule.class, ApplicationModule.class,
        WeatherApiModule.class, RoomModule.class})
public interface ApplicationComponent {

    void inject(WeatherApplication application);
}
