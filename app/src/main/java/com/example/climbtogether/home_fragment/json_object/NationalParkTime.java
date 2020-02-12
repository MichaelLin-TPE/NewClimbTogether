package com.example.climbtogether.home_fragment.json_object;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class NationalParkTime implements Serializable {
    @SerializedName("startTime")
    private String startTime;
    @SerializedName("endTime")
    private String endTime;

    @SerializedName("elementValue")
    private NationalParkElementValue elementValue;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public NationalParkElementValue getElementValue() {
        return elementValue;
    }

    public void setElementValue(NationalParkElementValue elementValue) {
        this.elementValue = elementValue;
    }
}
