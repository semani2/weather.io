package com.sai.weatherio.localization_service;

/**
 * Created by sai on 2/4/18.
 */

public interface ILocalizationService {

    String getString(int key);

    String getString(int key, Object... formatArgs);
}
