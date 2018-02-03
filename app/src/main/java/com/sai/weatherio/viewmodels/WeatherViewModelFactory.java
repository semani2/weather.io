package com.sai.weatherio.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.sai.weatherio.repository.IWeatherRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by sai on 2/2/18.
 */

@Singleton
public class WeatherViewModelFactory implements ViewModelProvider.Factory {

    private final IWeatherRepository mRepository;

    @Inject
    public WeatherViewModelFactory(IWeatherRepository repository) {
        this.mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(WeatherCollectionViewModel.class)) return (T) new WeatherCollectionViewModel(mRepository);
        else throw new IllegalArgumentException("ViewModel not found");
    }
}
