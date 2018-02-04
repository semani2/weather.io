package com.sai.weatherio.dependency_injection;

import com.sai.weatherio.api.WeatherApiModule;
import com.sai.weatherio.main.MainActivity;
import com.sai.weatherio.main.MainModule;
import com.sai.weatherio.room.RoomModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by sai on 2/2/18.
 */

@Singleton
@Component(modules = {ApplicationModule.class,
        WeatherApiModule.class, RoomModule.class, MainModule.class})
public interface ApplicationComponent {

    void inject(MainActivity target);
}
