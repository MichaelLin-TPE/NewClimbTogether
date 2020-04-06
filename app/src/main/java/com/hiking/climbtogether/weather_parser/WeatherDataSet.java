package com.hiking.climbtogether.weather_parser;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class WeatherDataSet implements Serializable {

    @SerializedName("locations")
    private WeatherLocation location;

    public WeatherLocation getLocation() {
        return location;
    }

    public void setLocation(WeatherLocation location) {
        this.location = location;
    }


}
