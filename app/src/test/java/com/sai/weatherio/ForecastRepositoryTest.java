package com.sai.weatherio;

import com.sai.weatherio.api.WeatherApiService;
import com.sai.weatherio.api.model.Weather;
import com.sai.weatherio.localization_service.ILocalizationService;
import com.sai.weatherio.model.SingleDayForecast;
import com.sai.weatherio.network_service.INetworkService;
import com.sai.weatherio.repository.ForecastRepository;
import com.sai.weatherio.repository.IForecastRepository;
import com.sai.weatherio.room.ForecastDao;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.internal.operators.flowable.FlowableFromPublisher;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by sai on 2/4/18.
 */

public class ForecastRepositoryTest {

    @Mock ForecastDao mForecastDao;

    @Mock WeatherApiService mWeatherApiService;

    @Mock INetworkService mNetworkService;

    @Mock ILocalizationService mLocalizationService;

    private final String CITY = "city";

    private final String STATE = "state";

    private IForecastRepository mRepository;

    private final List<SingleDayForecast> mList = new ArrayList<>();

    private final SingleDayForecast mForecast = new SingleDayForecast();

    private final Weather mWeather = new Weather();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mRepository = new ForecastRepository(mForecastDao, mWeatherApiService, mNetworkService, mLocalizationService);

        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> Schedulers.trampoline());
    }

    @Test
    public void test_CacheWillBeAccessedWhenOffline() {
        when(mNetworkService.isNetworkAvailable()).thenReturn(false);

        mList.add(mForecast);
        when(mForecastDao.getWeatherForecast(CITY, STATE)).thenReturn(Flowable.just(mList));

        mRepository.loadWeather(CITY, STATE);

        verify(mNetworkService, atLeast(1)).isNetworkAvailable();
        verify(mForecastDao, times(1)).getWeatherForecast(CITY, STATE);
        verify(mWeatherApiService, times(0)).getWeatherForecast(STATE, CITY);
    }

    @Test
    public void test_APIWillBeAccessedWhenOnline_CacheNoData() {
        when(mNetworkService.isNetworkAvailable()).thenReturn(true);

        when(mWeatherApiService.getWeatherForecast(STATE, CITY)).thenReturn(Flowable.just(mWeather));

        mRepository.loadWeather(CITY, STATE);

        verify(mNetworkService, times(2)).isNetworkAvailable();
        verify(mForecastDao, times(0)).getWeatherForecast(CITY, STATE);
        verify(mWeatherApiService, times(1)).getWeatherForecast(STATE, CITY);
    }

    @Test
    public void test_CacheWillBeAccessed_LessThan_15Seconds() {
        when(mNetworkService.isNetworkAvailable()).thenReturn(true);

        when(mWeatherApiService.getWeatherForecast(STATE, CITY)).thenReturn(Flowable.just(mWeather));
        mRepository.loadWeather(CITY, STATE);

        verify(mNetworkService, atLeast(2)).isNetworkAvailable();
        verify(mForecastDao, times(0)).getWeatherForecast(CITY, STATE);
        verify(mWeatherApiService, times(1)).getWeatherForecast(STATE, CITY);

        mList.add(mForecast);
        when(mForecastDao.getWeatherForecast(CITY, STATE)).thenReturn(Flowable.just(mList));

        mRepository.loadWeather(CITY, STATE);

        verify(mForecastDao, times(1)).getWeatherForecast(CITY, STATE);
    }

    @Test
    public void test_CacheNotAccessed_MoreThan_15Seconds() {
        when(mNetworkService.isNetworkAvailable()).thenReturn(true);

        when(mWeatherApiService.getWeatherForecast(STATE, CITY)).thenReturn(Flowable.just(mWeather));
        mRepository.loadWeather(CITY, STATE);

        verify(mNetworkService, atLeast(2)).isNetworkAvailable();
        verify(mForecastDao, times(0)).getWeatherForecast(CITY, STATE);
        verify(mWeatherApiService, times(1)).getWeatherForecast(STATE, CITY);

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mRepository.loadWeather(CITY, STATE);

        verify(mForecastDao, times(0)).getWeatherForecast(CITY, STATE);
    }
}
