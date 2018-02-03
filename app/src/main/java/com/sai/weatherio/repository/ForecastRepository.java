package com.sai.weatherio.repository;

import com.sai.weatherio.room.ForecastDao;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by sai on 2/3/18.
 */

@Singleton
public class ForecastRepository implements IForecastRepository {

    private final ForecastDao mForecastDao;

    @Inject
    public ForecastRepository(ForecastDao mForecastDao) {
        this.mForecastDao = mForecastDao;
    }
}
