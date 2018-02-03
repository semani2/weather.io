package com.sai.weatherio.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.sai.weatherio.model.SingleDayForecast;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by sai on 2/3/18.
 */

@Dao
public interface ForecastDao {

    @Insert
    public void insertForecasts(List<SingleDayForecast> forecasts);

    @Query("SELECT * FROM SingleDayForecast where location = :location")
    public Flowable<SingleDayForecast> getWeatherForecast(String location);

    @Query("DELETE FROM SingleDayForecast")
    public void deleteWeatherData();
}
