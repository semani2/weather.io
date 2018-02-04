package com.sai.weatherio.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.sai.weatherio.model.Resource;
import com.sai.weatherio.model.SingleDayForecast;
import com.sai.weatherio.repository.IForecastRepository;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import timber.log.Timber;

/**
 * Created by sai on 2/2/18.
 */

public class ForecastCollectionViewModel extends ViewModel{

    private final IForecastRepository mRepository;

    public MutableLiveData<Resource<List<SingleDayForecast>>> forecast = new MutableLiveData<>();

    public ForecastCollectionViewModel(IForecastRepository repository) {
        this.mRepository = repository;
    }

    public void getForecast(String location) {
        loadWeather(location);
    }

    private void loadWeather(String location) {
        if(location.trim().isEmpty()) {
            setError("Please enter a valid city and state, ex: San Francisco, CA");
            return;
        }

        String[] locationSplit = location.split(",");
        if(location.trim().split(",").length < 2 || locationSplit[0].trim().isEmpty()
                || locationSplit[1].trim().isEmpty() || locationSplit[1].trim().length() > 2) {
            setError("Please enter a valid city and state, ex: San Francisco, CA");
        } else {
            String city = locationSplit[0].trim().toLowerCase();
            String state = locationSplit[1].trim().toUpperCase();

            final DisposableSubscriber<List<SingleDayForecast>> subscriber = new DisposableSubscriber<List<SingleDayForecast>>() {
                @Override
                public void onNext(List<SingleDayForecast> singleDayForecasts) {
                    Resource<List<SingleDayForecast>> listResource = Resource.success(singleDayForecasts);
                    forecast.setValue(listResource);
                }

                @Override
                public void onError(Throwable t) {
                    Resource<List<SingleDayForecast>> errorResource = Resource.error("There was an error fetching weather forecast. " +
                            "Please try again!");
                    forecast.setValue(errorResource);
                }

                @Override
                public void onComplete() {
                    Timber.d("Completed subscribing to weather api");
                }
            };

            mRepository.loadWeather(city, state)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(subscriber);
        }
    }

    private void setError(String msg) {
        forecast.setValue(Resource.<List<SingleDayForecast>>error(msg));
    }
}
