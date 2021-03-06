package com.sai.weatherio.repository;

import com.sai.weatherio.model.SingleDayForecast;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by sai on 2/2/18.
 */

public interface IForecastRepository {

    Flowable<List<SingleDayForecast>> loadWeather(String city, String state);
}
