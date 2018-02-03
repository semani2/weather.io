package com.sai.weatherio.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sai on 2/2/18.
 */

public class Weather {

    @SerializedName("forecast")
    @Expose
    private Forecast forecast;

    public Weather() {}

    public Forecast getForecast ()
    {
        return forecast;
    }

    public void setForecast (Forecast forecast)
    {
        this.forecast = forecast;
    }
}
