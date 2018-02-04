package com.sai.weatherio.localization_service;

import android.content.Context;

/**
 * Created by sai on 2/4/18.
 */

public class LocalizationService implements ILocalizationService {

    private final Context mContext;

    public LocalizationService(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public String getString(int key) {
        return mContext.getString(key);
    }

    @Override
    public String getString(int key, Object... formatArgs) {
        return mContext.getString(key, formatArgs);
    }
}
