package com.hiking.climbtogether.home_fragment.json_object;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

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
