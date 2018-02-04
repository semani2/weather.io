package com.sai.weatherio.dependency_injection;

import com.sai.weatherio.app.WeatherApplication;
import com.sai.weatherio.localization_service.ILocalizationService;
import com.sai.weatherio.localization_service.LocalizationService;
import com.sai.weatherio.network_service.INetworkService;
import com.sai.weatherio.network_service.NetworkService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sai on 2/2/18.
 */

@Module
public class ApplicationModule {

    private WeatherApplication mApplication;

    public ApplicationModule(WeatherApplication application) {
        this.mApplication = application;
    }

    @Provides
    @Singleton
    public WeatherApplication provideApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    public INetworkService provideNetworkService() {
        return new NetworkService(mApplication);
    }

    @Provides
    @Singleton
    public ILocalizationService provideLocalizationService() {
        return new LocalizationService(mApplication);
    }
}
