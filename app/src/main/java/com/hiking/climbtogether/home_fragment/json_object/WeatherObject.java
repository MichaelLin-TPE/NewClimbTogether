package com.hiking.climbtogether.home_fragment.json_object;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WeatherObject implements Serializable {

    @SerializedName("records")
    private WeatherRecord records;

    public WeatherRecord getRecords() {
        return records;
    }

    public void setRecords(WeatherRecord records) {
        this.records = records;
    }
}
