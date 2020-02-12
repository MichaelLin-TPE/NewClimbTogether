package com.example.climbtogether.home_fragment.json_object;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class NationalParkLocation implements Serializable {

    @SerializedName("location")
    private ArrayList<NationalParkLocations> location;

    public ArrayList<NationalParkLocations> getLocations() {
        return location;
    }

    public void setLocations(ArrayList<NationalParkLocations> location) {
        this.location = location;
    }

}
