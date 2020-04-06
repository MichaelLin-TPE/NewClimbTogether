package com.hiking.climbtogether.weather_parser;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class WeatherLocationNew implements Serializable {
    @SerializedName("locationName")
    private String locationName;
    @SerializedName("geocode")
    private String geoCode;
    @SerializedName("lat")
    private String lat;
    @SerializedName("lon")
    private String lon;
    @SerializedName("weatherElement")
    private ArrayList<WeatherElement> weatherElement;

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getGeoCode() {
        return geoCode;
    }

    public void setGeoCode(String geoCode) {
        this.geoCode = geoCode;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public ArrayList<WeatherElement> getWeatherElement() {
        return weatherElement;
    }

    public void setWeatherElement(ArrayList<WeatherElement> weatherElement) {
        this.weatherElement = weatherElement;
    }
}
