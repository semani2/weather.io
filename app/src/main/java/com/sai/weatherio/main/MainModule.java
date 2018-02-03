package com.sai.weatherio.main;

import android.arch.lifecycle.ViewModelProvider;

import com.sai.weatherio.repository.IForecastRepository;
import com.sai.weatherio.viewmodels.ForecastViewModelFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by sai on 2/2/18.
 */

@Module
public class MainModule {

    @Provides
    @Singleton
    public ViewModelProvider.Factory provideViewModelFactory(IForecastRepository logRepository) {
        return new ForecastViewModelFactory(logRepository);
    }
}
