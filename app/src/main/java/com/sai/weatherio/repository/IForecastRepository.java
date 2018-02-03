package com.sai.weatherio.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.sai.weatherio.model.Resource;
import com.sai.weatherio.model.SingleDayForecast;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by sai on 2/2/18.
 */

public interface IForecastRepository {

    <T extends Object> MutableLiveData<Resource<T>> loadWeather(String city, String state, boolean forceAPI);
}
