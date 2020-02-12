package com.example.climbtogether.home_fragment.json_object;

import android.widget.ArrayAdapter;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

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
