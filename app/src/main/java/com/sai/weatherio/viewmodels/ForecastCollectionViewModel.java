package com.sai.weatherio.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.sai.weatherio.R;
import com.sai.weatherio.localization_service.ILocalizationService;
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

    private final ILocalizationService mLocalizationService;

    public MutableLiveData<Resource<List<SingleDayForecast>>> forecast = new MutableLiveData<>();

    public ForecastCollectionViewModel(IForecastRepository repository, ILocalizationService localizationService) {
        this.mRepository = repository;
        this.mLocalizationService = localizationService;
    }

    public void getForecast(String location) {
        loadWeather(location);
    }

    private void loadWeather(String location) {
        if(location.trim().isEmpty()) {
            setError(mLocalizationService.getString(R.string.str_enter_valid_city_state));
            return;
        }

        String[] locationSplit = location.split(",");
        if(location.trim().split(",").length < 2 || locationSplit[0].trim().isEmpty()
                || locationSplit[1].trim().isEmpty() || locationSplit[1].trim().length() > 2) {
            setError(mLocalizationService.getString(R.string.str_enter_valid_city_state));
        } else {
            String city = locationSplit[0].trim().toLowerCase();
            String state = locationSplit[1].trim().toUpperCase();

            final DisposableSubscriber<List<SingleDayForecast>> subscriber = new DisposableSubscriber<List<SingleDayForecast>>() {
                @Override
                public void onNext(List<SingleDayForecast> singleDayForecasts) {
                    if(singleDayForecasts.size() == 0) {
                        setError(mLocalizationService.getString(R.string.str_error_fetching_forecast));
                        return;
                    }
                    Resource<List<SingleDayForecast>> listResource = Resource.success(singleDayForecasts);
                    forecast.setValue(listResource);
                }

                @Override
                public void onError(Throwable t) {
                    setError(mLocalizationService.getString(R.string.str_error_fetching_forecast));
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
