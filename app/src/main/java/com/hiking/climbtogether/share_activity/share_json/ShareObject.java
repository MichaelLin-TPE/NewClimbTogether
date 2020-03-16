package com.hiking.climbtogether.share_activity.share_json;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ShareObject implements Serializable {
    @SerializedName("result")
    private ArrayList<ShareArticleJson> result;

    public ArrayList<ShareArticleJson> getResult() {
        return result;
    }

    public void setResult(ArrayList<ShareArticleJson> result) {
        this.result = result;
    }
}
