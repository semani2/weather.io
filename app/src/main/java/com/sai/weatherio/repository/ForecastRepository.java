package com.sai.weatherio.repository;

import android.arch.lifecycle.MutableLiveData;

import com.sai.weatherio.api.WeatherApiService;
import com.sai.weatherio.api.model.ForecastDay;
import com.sai.weatherio.api.model.Weather;
import com.sai.weatherio.model.Resource;
import com.sai.weatherio.model.SingleDayForecast;
import com.sai.weatherio.room.ForecastDao;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by sai on 2/3/18.
 */

@Singleton
public class ForecastRepository implements IForecastRepository {

    private final ForecastDao mForecastDao;

    private final WeatherApiService mWeatherApiService;

    private long mTimestamp = 0;

    private static final long STALE_TIME = 15 * 1000;

    @Inject
    public ForecastRepository(ForecastDao mForecastDao, WeatherApiService weatherApiService) {
        this.mForecastDao = mForecastDao;
        this.mWeatherApiService = weatherApiService;
    }

    @Override
    public <T extends Object> MutableLiveData<Resource<T>> loadWeather(String city, String state, boolean forceAPI) {
        final MutableLiveData<Resource<T>> data = new MutableLiveData<>();

        final FlowableSubscriber<List<SingleDayForecast>> subscriber = new FlowableSubscriber<List<SingleDayForecast>>() {
            @Override
            public void onSubscribe(Subscription s) {
                Timber.d("Subscribed to weather api to fetch data");
            }

            @Override
            public void onNext(List<SingleDayForecast> singleDayForecasts) {
                Resource<List<SingleDayForecast>> listResource = Resource.success(singleDayForecasts);
                data.setValue((Resource<T>) listResource);
            }

            @Override
            public void onError(Throwable t) {
                Resource<Throwable> errorResource = Resource.error(t.getLocalizedMessage(), t);
                data.setValue((Resource<T>) errorResource);
            }

            @Override
            public void onComplete() {
                Timber.d("Completed subscribing to weather api");
            }
        };

        if(forceAPI) {
            loadWeatherFromApi(city, state)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(subscriber);
        }
        loadWeatherFromCache(city, state).switchIfEmpty(loadWeatherFromApi(city, state));

        return data;
    }

    private Flowable<List<SingleDayForecast>> loadWeatherFromApi(final String city, final String state) {
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
                            list.add(new SingleDayForecast(
                                   0, forecastDay.getTitle(), forecastDay.getIcon_url(),
                                    forecastDay.getFcttext(), city, state
                            ));
                        }
                        return list;
                    }
                });
    }

    private Flowable<List<SingleDayForecast>> loadWeatherFromCache(String city, String state) {
        if(isDataUptoDate()) {
            List<SingleDayForecast> forecastList = mForecastDao.getWeatherForecast(city, state);

            if (forecastList == null || forecastList.size() == 0) return Flowable.empty();
            return Flowable.just(forecastList);
        }
        mTimestamp = System.currentTimeMillis();
        return Flowable.empty();
    }

    private boolean isDataUptoDate() {
        return System.currentTimeMillis() - mTimestamp < STALE_TIME;
    }
}
