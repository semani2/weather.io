package com.sai.weatherio.dependency_injection;

import com.sai.weatherio.api.WeatherApiModule;
import com.sai.weatherio.app.WeatherApplication;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;

/**
 * Created by sai on 2/2/18.
 */

@Singleton
@Component(modules = {AndroidInjectionModule.class, ApplicationModule.class, WeatherApiModule.class})
public interface ApplicationComponent {

    void inject(WeatherApplication application);
}
