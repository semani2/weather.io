package com.sai.weatherio.viewmodels;

import android.arch.lifecycle.ViewModel;

import com.sai.weatherio.repository.IWeatherRepository;

/**
 * Created by sai on 2/2/18.
 */

public class WeatherCollectionViewModel extends ViewModel{

    private final IWeatherRepository mRepository;

    public WeatherCollectionViewModel(IWeatherRepository repository) {
        this.mRepository = repository;
    }
}
