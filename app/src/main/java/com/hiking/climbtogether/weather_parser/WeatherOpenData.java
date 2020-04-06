package com.hiking.climbtogether.weather_parser;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WeatherOpenData implements Serializable {
    @SerializedName("dataset")
    private WeatherDataSet dataSet;

    public WeatherDataSet getDataSet() {
        return dataSet;
    }

    public void setDataSet(WeatherDataSet dataSet) {
        this.dataSet = dataSet;
    }
}
