package com.sai.weatherio.network_service;

/**
 * Created by sai on 2/4/18.
 */

public interface INetworkService {

    /**
     * Interface method to check if there is a valid network connection
     * @return
     */
    boolean isNetworkAvailable();
}
