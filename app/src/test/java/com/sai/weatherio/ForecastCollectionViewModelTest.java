package com.sai.weatherio;

import android.arch.core.executor.testing.InstantTaskExecutorRule;

import com.sai.weatherio.localization_service.ILocalizationService;
import com.sai.weatherio.model.SingleDayForecast;
import com.sai.weatherio.repository.IForecastRepository;
import com.sai.weatherio.viewmodels.ForecastCollectionViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by sai on 2/4/18.
 */

public class ForecastCollectionViewModelTest {

    @Mock
    private ILocalizationService mLocalizationService;

    @Mock
    private IForecastRepository mForecastRepository;

    private ForecastCollectionViewModel mViewModel;

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    private final String CITY = "city";
    private final String STATE = "ST";

    private final String INCOMPLETE_STATE = "S";
    private final String WRONG_STATE = "STA";

    private final String LOCATION = CITY + ", " + STATE;

    private final List<SingleDayForecast> mList = new ArrayList<>();

    private final SingleDayForecast mForecast = new SingleDayForecast();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        mList.add(mForecast);
        mViewModel = new ForecastCollectionViewModel(mForecastRepository, mLocalizationService);

        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> Schedulers.trampoline());
    }

    @Test
    public void test_EmptyLocation_ForecastRequested() {
        mViewModel.getForecast(" ");

        verify(mLocalizationService, times(1)).getString(R.string.str_enter_valid_city_state);
        verify(mForecastRepository, times(0)).loadWeather(CITY, STATE);
    }

    @Test
    public void test_OnlyCity_ForecastRequested() {
        mViewModel.getForecast(CITY);

        verify(mLocalizationService, times(1)).getString(R.string.str_enter_valid_city_state);
        verify(mForecastRepository, times(0)).loadWeather(CITY, STATE);
    }

    @Test
    public void test_OnlyState_ForecastRequested() {
        mViewModel.getForecast(STATE);

        verify(mLocalizationService, times(1)).getString(R.string.str_enter_valid_city_state);
        verify(mForecastRepository, times(0)).loadWeather(CITY, STATE);
    }

    @Test
    public void test_IncompleteStateCode_ForecastRequested() {
        mViewModel.getForecast(CITY + "," + INCOMPLETE_STATE);

        verify(mLocalizationService, times(1)).getString(R.string.str_enter_valid_city_state);
        verify(mForecastRepository, times(0)).loadWeather(CITY, INCOMPLETE_STATE);
    }

    @Test
    public void test_WrongStateCode_ForecastRequested() {
        mViewModel.getForecast(CITY + "," + WRONG_STATE);

        verify(mLocalizationService, times(1)).getString(R.string.str_enter_valid_city_state);
        verify(mForecastRepository, times(0)).loadWeather(CITY, WRONG_STATE);
    }

    @Test
    public void test_ForecastRequested() {
        when(mForecastRepository.loadWeather(CITY, STATE)).thenReturn(Flowable.just(mList));

        mViewModel.getForecast(LOCATION);

        verify(mLocalizationService, times(0)).getString(R.string.str_enter_valid_city_state);
        verify(mForecastRepository, times(1)).loadWeather(CITY, STATE);
    }
}
