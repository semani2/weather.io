package com.sai.weatherio.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sai on 2/2/18.
 */

public class ForecastDay {

    @SerializedName("icon")
    @Expose
    private String icon;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("icon_url")
    @Expose
    private String icon_url;

    @SerializedName("fcttext_metric")
    @Expose
    private String fcttext_metric;

    @SerializedName("fcttext")
    @Expose
    private String fcttext;

    @SerializedName("pop")
    @Expose
    private String pop;

    @SerializedName("period")
    @Expose
    private String period;

    public ForecastDay() {}

    public String getIcon ()
    {
        return icon;
    }

    public void setIcon (String icon)
    {
        this.icon = icon;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getIcon_url ()
    {
        return icon_url;
    }

    public void setIcon_url (String icon_url)
    {
        this.icon_url = icon_url;
    }

    public String getFcttext_metric ()
    {
        return fcttext_metric;
    }

    public void setFcttext_metric (String fcttext_metric)
    {
        this.fcttext_metric = fcttext_metric;
    }

    public String getFcttext ()
    {
        return fcttext;
    }

    public void setFcttext (String fcttext)
    {
        this.fcttext = fcttext;
    }

    public String getPop ()
    {
        return pop;
    }

    public void setPop (String pop)
    {
        this.pop = pop;
    }

    public String getPeriod ()
    {
        return period;
    }

    public void setPeriod (String period)
    {
        this.period = period;
    }

}
