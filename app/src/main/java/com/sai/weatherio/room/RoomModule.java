package com.sai.weatherio.room;

import android.arch.persistence.room.Room;

import com.sai.weatherio.api.WeatherApiService;
import com.sai.weatherio.app.WeatherApplication;
import com.sai.weatherio.localization_service.ILocalizationService;
import com.sai.weatherio.network_service.INetworkService;
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
    public IForecastRepository provideWeatherRepository(ForecastDao forecastDao, WeatherApiService weatherApiService,
                                                        INetworkService networkService, ILocalizationService localizationService) {
        return new ForecastRepository(forecastDao, weatherApiService, networkService, localizationService);
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
