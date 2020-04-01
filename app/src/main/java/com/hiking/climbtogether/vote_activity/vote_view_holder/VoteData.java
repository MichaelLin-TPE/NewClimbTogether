package com.hiking.climbtogether.vote_activity.vote_view_holder;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class VoteData implements Serializable {

    @SerializedName("title")
    private String title;
    @SerializedName("time")
    private String time;
    @SerializedName("item_array")
    private ArrayList<String> itemArray;
    @SerializedName("creator")
    private String creator;

    @SerializedName("vote_already")
    private ArrayList<String> voteAlreadyArray;


    public ArrayList<String> getVoteAlreadyArray() {
        return voteAlreadyArray;
    }

    public void setVoteAlreadyArray(ArrayList<String> voteAlreadyArray) {
        this.voteAlreadyArray = voteAlreadyArray;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ArrayList<String> getItemArray() {
        return itemArray;
    }

    public void setItemArray(ArrayList<String> itemArray) {
        this.itemArray = itemArray;
    }
}
