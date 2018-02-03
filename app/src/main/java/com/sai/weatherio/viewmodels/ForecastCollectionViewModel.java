package com.sai.weatherio.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.sai.weatherio.model.Resource;
import com.sai.weatherio.model.SingleDayForecast;
import com.sai.weatherio.repository.IForecastRepository;

import java.util.List;

/**
 * Created by sai on 2/2/18.
 */

public class ForecastCollectionViewModel extends ViewModel{

    private final IForecastRepository mRepository;

    private MutableLiveData<Resource<List<SingleDayForecast>>> forecast;

    public ForecastCollectionViewModel(IForecastRepository repository) {
        this.mRepository = repository;
    }

    public MutableLiveData<Resource<List<SingleDayForecast>>> getForecast(String location, boolean forceRefresh) {
        if(forecast == null || forceRefresh) {
            forecast = new MutableLiveData<>();
            loadWeather(location, forceRefresh);
        }

        return forecast;
    }

    private void loadWeather(String location, boolean forceRefresh) {
        String state = "NC";
        String city = "Raleigh";
        forecast = mRepository.loadWeather(city, state, forceRefresh);
    }
}
