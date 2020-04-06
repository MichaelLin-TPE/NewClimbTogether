package com.hiking.climbtogether.weather_parser;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WeatherObject implements Serializable {

    @SerializedName("cwbopendata")
    private WeatherOpenData cwbOpenData;

    public WeatherOpenData getCwbOpenData() {
        return cwbOpenData;
    }

    public void setCwbOpenData(WeatherOpenData cwbOpenData) {
        this.cwbOpenData = cwbOpenData;
    }
}
