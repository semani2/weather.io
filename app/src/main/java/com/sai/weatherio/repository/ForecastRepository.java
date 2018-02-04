package com.sai.weatherio.repository;

import com.sai.weatherio.R;
import com.sai.weatherio.api.WeatherApiService;
import com.sai.weatherio.api.model.ForecastDay;
import com.sai.weatherio.api.model.Weather;
import com.sai.weatherio.localization_service.ILocalizationService;
import com.sai.weatherio.model.SingleDayForecast;
import com.sai.weatherio.network_service.INetworkService;
import com.sai.weatherio.room.ForecastDao;

import java.util.ArrayList;
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

    private final INetworkService mNetworkService;

    private final ILocalizationService mLocalizationService;

    private long mTimestamp = 0;

    private static final long STALE_TIME = 15 * 1000;

    @Inject
    public ForecastRepository(ForecastDao mForecastDao, WeatherApiService weatherApiService,
                              INetworkService networkService, ILocalizationService localizationService) {
        this.mForecastDao = mForecastDao;
        this.mWeatherApiService = weatherApiService;
        this.mNetworkService = networkService;
        this.mLocalizationService = localizationService;
    }

    @Override
    public Flowable<List<SingleDayForecast>> loadWeather(String city, String state) {
        return loadWeatherFromCache(city, state).switchIfEmpty(loadWeatherFromApi(city, state));
    }

    private Flowable<List<SingleDayForecast>> loadWeatherFromApi(final String city, final String state) {
        if(!isNetworkAvailable()) {
            return Flowable.error(new Exception("No network connection"));
        }

        return mWeatherApiService.getWeatherForecast(state, city)
                .map(weather -> weather.getForecast().getTxt_forecast().getForecastday())
                .map(forecastDays -> {
                    List<SingleDayForecast> list = new ArrayList<>();
                    for(int i = 0; i < forecastDays.length - 1; i+=2) {
                        ForecastDay forecastDay = forecastDays[i];
                        String title = forecastDay.getTitle();
                        if(i == 0) {
                            title = mLocalizationService.getString(R.string.str_today);
                        } else if( i == 2) {
                            title = mLocalizationService.getString(R.string.str_tomorrow);
                        }
                        list.add(new SingleDayForecast(
                               0, title, forecastDay.getIcon_url(),
                                forecastDay.getFcttext(), city, state
                        ));
                    }
                    return list;
                })
                .doOnNext(singleDayForecasts -> {
                    mForecastDao.deleteWeatherData(city, state);
                    mForecastDao.insertForecasts(singleDayForecasts);
                });
    }

    private Flowable<List<SingleDayForecast>> loadWeatherFromCache(String city, String state) {
        if(isDataUptoDate()) {
            return mForecastDao.getWeatherForecast(city, state);
        }
        mTimestamp = System.currentTimeMillis();
        return Flowable.empty();
    }

    private boolean isDataUptoDate() {
        return !isNetworkAvailable() || System.currentTimeMillis() - mTimestamp < STALE_TIME;

    }

    private boolean isNetworkAvailable() {
        return mNetworkService.isNetworkAvailable();
    }
}
