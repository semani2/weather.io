package com.sai.weatherio.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.sai.weatherio.model.SingleDayForecast;

import java.util.List;

/**
 * Created by sai on 2/3/18.
 */

@Dao
public interface ForecastDao {

    @Insert
    public void insertForecasts(List<SingleDayForecast> forecasts);

    @Query("SELECT * FROM SingleDayForecast where city = :city AND state = :state")
    public List<SingleDayForecast> getWeatherForecast(String city, String state);

    @Query("DELETE FROM SingleDayForecast where city = :city AND state = :state")
    public void deleteWeatherData(String city, String state);
}
