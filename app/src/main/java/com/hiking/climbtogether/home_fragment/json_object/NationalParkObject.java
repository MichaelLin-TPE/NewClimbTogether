package com.hiking.climbtogether.home_fragment.json_object;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NationalParkObject implements Serializable {
    @SerializedName("cwbopendata")
    private NationalParkCwbOpenData cwbopendata;

    public NationalParkCwbOpenData getCwbopendata() {
        return cwbopendata;
    }

    public void setCwbopendata(NationalParkCwbOpenData cwbopendata) {
        this.cwbopendata = cwbopendata;
    }
}
