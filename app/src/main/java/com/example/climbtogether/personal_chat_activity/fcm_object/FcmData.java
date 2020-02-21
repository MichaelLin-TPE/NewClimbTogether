package com.example.climbtogether.personal_chat_activity.fcm_object;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FcmData implements Serializable {
    @SerializedName("data_title")
    private String dataTitle;
    @SerializedName("data_content")
    private String dataContent;


    public String getDataTitle() {
        return dataTitle;
    }

    public void setDataTitle(String dataTitle) {
        this.dataTitle = dataTitle;
    }

    public String getDataContent() {
        return dataContent;
    }

    public void setDataContent(String dataContent) {
        this.dataContent = dataContent;
    }
}
