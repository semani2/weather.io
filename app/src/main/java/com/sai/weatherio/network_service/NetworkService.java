package com.sai.weatherio.network_service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by sai on 2/4/18.
 */

public class NetworkService implements INetworkService {

    private final Context mContext;

    @Inject
    public NetworkService(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public boolean isNetworkAvailable() {
        try {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        catch(Exception e) {
            Timber.e(e);
            return true;
        }
    }
}
