package com.sai.weatherio.repository;

import com.sai.weatherio.api.WeatherApiService;
import com.sai.weatherio.api.model.ForecastDay;
import com.sai.weatherio.api.model.Weather;
import com.sai.weatherio.model.SingleDayForecast;
import com.sai.weatherio.network_service.INetworkService;
import com.sai.weatherio.room.ForecastDao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by sai on 2/3/18.
 */

@Singleton
public class ForecastRepository implements IForecastRepository {

    private final ForecastDao mForecastDao;

    private final WeatherApiService mWeatherApiService;

    private final INetworkService mNetworkService;

    private long mTimestamp = 0;

    private static final long STALE_TIME = 15 * 1000;

    @Inject
    public ForecastRepository(ForecastDao mForecastDao, WeatherApiService weatherApiService, INetworkService networkService) {
        this.mForecastDao = mForecastDao;
        this.mWeatherApiService = weatherApiService;
        mNetworkService = networkService;
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
                .map(new Function<Weather, ForecastDay[]>() {
                    @Override
                    public ForecastDay[] apply(Weather weather) throws Exception {
                        return weather.getForecast().getTxt_forecast().getForecastday();
                    }
                })
                .map(new Function<ForecastDay[], List<SingleDayForecast>>() {
                    @Override
                    public List<SingleDayForecast> apply(ForecastDay[] forecastDays) throws Exception {
                        List<SingleDayForecast> list = new ArrayList<>();
                        for(int i = 0; i < forecastDays.length - 1; i+=2) {
                            ForecastDay forecastDay = forecastDays[i];
                            String title = forecastDay.getTitle();
                            if(i == 0) {
                                title = "Today";
                            } else if( i == 2) {
                                title = "Tomorrow";
                            }
                            list.add(new SingleDayForecast(
                                   0, title, forecastDay.getIcon_url(),
                                    forecastDay.getFcttext(), city, state
                            ));
                        }
                        return list;
                    }
                })
                .doOnNext(new Consumer<List<SingleDayForecast>>() {
                    @Override
                    public void accept(List<SingleDayForecast> singleDayForecasts) throws Exception {
                        mForecastDao.deleteWeatherData(city, state);
                        mForecastDao.insertForecasts(singleDayForecasts);
                    }
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
