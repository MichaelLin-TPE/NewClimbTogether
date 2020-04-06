package com.hiking.climbtogether.weather_parser;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class WeatherLocation implements Serializable {
    @SerializedName("location")
    private ArrayList<WeatherLocationNew> location;

    public ArrayList<WeatherLocationNew> getLocation() {
        return location;
    }

    public void setLocation(ArrayList<WeatherLocationNew> location) {
        this.location = location;
    }
}
