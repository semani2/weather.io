package com.sai.weatherio.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.sai.weatherio.localization_service.ILocalizationService;
import com.sai.weatherio.repository.IForecastRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by sai on 2/2/18.
 */

@Singleton
public class ForecastViewModelFactory implements ViewModelProvider.Factory {

    private final IForecastRepository mRepository;

    private final ILocalizationService mLocalizationService;

    @Inject
    public ForecastViewModelFactory(IForecastRepository repository, ILocalizationService localizationService) {
        this.mRepository = repository;
        this.mLocalizationService = localizationService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(ForecastCollectionViewModel.class)) return (T) new ForecastCollectionViewModel(mRepository,
                mLocalizationService);
        else throw new IllegalArgumentException("ViewModel not found");
    }
}
