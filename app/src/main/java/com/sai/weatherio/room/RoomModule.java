package com.sai.weatherio.room;

import android.arch.persistence.room.Room;

import com.sai.weatherio.app.WeatherApplication;
import com.sai.weatherio.repository.ForecastRepository;
import com.sai.weatherio.repository.IForecastRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sai on 2/3/18.
 */

@Module
public class RoomModule {

    private final ForecastDatabase mForecastDatabase;

    public RoomModule(WeatherApplication weatherApplication) {
        this.mForecastDatabase = Room.databaseBuilder(weatherApplication,
                ForecastDatabase.class, "Forecast.db").build();
    }

    @Provides
    @Singleton
    public IForecastRepository provideWeatherRepository(ForecastDao forecastDao) {
        return new ForecastRepository(forecastDao);
    }

    @Provides
    @Singleton
    public ForecastDao provideForecastDao(ForecastDatabase forecastDatabase) {
        return forecastDatabase.forecastDao();
    }

    @Provides
    @Singleton
    public ForecastDatabase provideForecastDatabase() {
        return mForecastDatabase;
    }

}
