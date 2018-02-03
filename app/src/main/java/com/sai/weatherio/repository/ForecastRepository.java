package com.sai.weatherio.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.sai.weatherio.api.WeatherApiService;
import com.sai.weatherio.app.WeatherApplication;
import com.sai.weatherio.model.Resource;
import com.sai.weatherio.model.SingleDayForecast;
import com.sai.weatherio.room.ForecastDao;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;

/**
 * Created by sai on 2/3/18.
 */

@Singleton
public class ForecastRepository implements IForecastRepository {

    private final ForecastDao mForecastDao;

    private final WeatherApiService mWeatherApiService;

    @Inject
    public ForecastRepository(ForecastDao mForecastDao, WeatherApiService weatherApiService) {
        this.mForecastDao = mForecastDao;
        this.mWeatherApiService = weatherApiService;
    }

    @Override
    public MutableLiveData<Resource<List<SingleDayForecast>>> loadWeather(String city, String state, boolean forceAPI) {
        if(forceAPI) {
            loadWeatherFromApi(city, state);
        }
        loadWeatherFromCache(city, state).switchIfEmpty(loadWeatherFromApi(city, state));

        return null;
    }

    private Flowable<List<SingleDayForecast>> loadWeatherFromApi(String city, String state) {
        return null;
    }

    private Flowable<List<SingleDayForecast>> loadWeatherFromCache(String city, String state) {
        List<SingleDayForecast> forecastList = mForecastDao.getWeatherForecast(city, state);

        if(forecastList == null || forecastList.size() == 0) return Flowable.empty();
        return Flowable.just(forecastList);
    }
}
