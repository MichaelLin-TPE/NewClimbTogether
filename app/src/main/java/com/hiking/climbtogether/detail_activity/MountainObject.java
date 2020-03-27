package com.hiking.climbtogether.detail_activity;

import com.google.gson.annotations.SerializedName;
import com.hiking.climbtogether.db_modle.DataDTO;

import java.io.Serializable;
import java.util.ArrayList;

public class MountainObject implements Serializable {

    @SerializedName("mt_data")
    private ArrayList<MountainFavoriteData> dataArrayList;

    public ArrayList<MountainFavoriteData> getDataArrayList() {
        return dataArrayList;
    }

    public void setDataArrayList(ArrayList<MountainFavoriteData> dataArrayList) {
        this.dataArrayList = dataArrayList;
    }
}
