package com.sai.weatherio.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sai on 2/2/18.
 */

public class Forecast
{
    @SerializedName("txt_forecast")
    @Expose
    private TxtForecast txt_forecast;

    public TxtForecast getTxt_forecast ()
    {
        return txt_forecast;
    }

    public void setTxt_forecast (TxtForecast txt_forecast)
    {
        this.txt_forecast = txt_forecast;
    }
}
