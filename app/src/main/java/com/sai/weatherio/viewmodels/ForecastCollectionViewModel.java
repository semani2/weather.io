package com.sai.weatherio.viewmodels;

import android.arch.lifecycle.ViewModel;

import com.sai.weatherio.repository.IForecastRepository;

/**
 * Created by sai on 2/2/18.
 */

public class ForecastCollectionViewModel extends ViewModel{

    private final IForecastRepository mRepository;

    public ForecastCollectionViewModel(IForecastRepository repository) {
        this.mRepository = repository;
    }
}
