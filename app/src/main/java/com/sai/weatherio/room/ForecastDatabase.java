package com.sai.weatherio.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.sai.weatherio.model.SingleDayForecast;

/**
 * Created by sai on 2/3/18.
 */

@Database(entities = {SingleDayForecast.class}, version = 1, exportSchema = false)
public abstract class ForecastDatabase extends RoomDatabase{

    abstract ForecastDao forecastDao();
}
