package com.sai.weatherio.main;

import android.arch.lifecycle.ViewModelProvider;

import com.sai.weatherio.repository.IWeatherRepository;
import com.sai.weatherio.viewmodels.WeatherViewModelFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sai on 2/2/18.
 */

@Module
public abstract class MainModule {

    @Provides
    @Singleton
    public ViewModelProvider.Factory provideViewModelFactory(IWeatherRepository logRepository) {
        return new WeatherViewModelFactory(logRepository);
    }
}
