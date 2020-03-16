package com.hiking.climbtogether.home_fragment.json_object;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class NationalParkLocations implements Serializable {


    @SerializedName("locationName")
    private String locationName;
    @SerializedName("geocode")
    private String geocode;
    @SerializedName("lat")
    private String lat;
    @SerializedName("lon")
    private String lon;
    @SerializedName("weatherElement")
    private ArrayList<NationalParkWeatherElement> weatherElement;

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getGeocode() {
        return geocode;
    }

    public void setGeocode(String geocode) {
        this.geocode = geocode;
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

    public ArrayList<NationalParkWeatherElement> getWeatherElement() {
        return weatherElement;
    }

    public void setWeatherElement(ArrayList<NationalParkWeatherElement> weatherElement) {
        this.weatherElement = weatherElement;
    }

}
