package com.example.climbtogether.home_fragment.json_object;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class NationalParkWeatherElement implements Serializable {
    @SerializedName("elementName")
    private String elementName;
    @SerializedName("description")
    private String description;
    @SerializedName("time")
    private ArrayList<NationalParkTime> time;

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<NationalParkTime> getTime() {
        return time;
    }

    public void setTime(ArrayList<NationalParkTime> time) {
        this.time = time;
    }
}
