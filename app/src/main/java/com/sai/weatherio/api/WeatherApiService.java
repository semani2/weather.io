package com.sai.weatherio.api;

import com.sai.weatherio.api.model.Weather;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by sai on 2/2/18.
 */

public interface WeatherApiService {

    @GET("/{stateCode}/{cityCode}.json")
    Observable<Weather> getWeatherForecast(@Path("stateCode") String stateCode, @Path("cityCode") String cityCode);
}
