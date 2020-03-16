package com.hiking.climbtogether.home_fragment.json_object;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NationalParkCwbOpenData implements Serializable {

    @SerializedName("dataset")
    private NationalParkDataSet dataset;

    public NationalParkDataSet getDataset() {
        return dataset;
    }

    public void setDataset(NationalParkDataSet dataset) {
        this.dataset = dataset;
    }
}
