package com.sai.weatherio.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sai on 2/2/18.
 */

public class TxtForecast {

    @SerializedName("forecastday")
    @Expose
    private ForecastDay[] forecastday;

    @SerializedName("date")
    @Expose
    private String date;

    public ForecastDay[] getForecastday ()
    {
        return forecastday;
    }

    public void setForecastday (ForecastDay[] forecastday)
    {
        this.forecastday = forecastday;
    }

    public String getDate ()
    {
        return date;
    }

    public void setDate (String date)
    {
        this.date = date;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [forecastday = "+forecastday+", date = "+date+"]";
    }
}
