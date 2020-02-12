package com.example.climbtogether.home_fragment.json_object;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class NationalParkDataSet implements Serializable {

    @SerializedName("locations")
    private NationalParkLocation locations;

    public NationalParkLocation getLocation() {
        return locations;
    }

    public void setLocation(NationalParkLocation locations) {
        this.locations = locations;
    }
}
