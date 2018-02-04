package com.sai.weatherio.api;

import com.sai.weatherio.api.model.Weather;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by sai on 2/2/18.
 */

public interface WeatherApiService {

    @GET("/api/24f0b7e4ed53f605/forecast10day/q/{stateCode}/{cityCode}.json")
    Flowable<Weather> getWeatherForecast(@Path("stateCode") String stateCode, @Path("cityCode") String cityCode);
}
