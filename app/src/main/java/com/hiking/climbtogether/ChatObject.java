package com.hiking.climbtogether;

import com.google.gson.annotations.SerializedName;
import com.hiking.climbtogether.chat_activity.ChatData;

import java.io.Serializable;
import java.util.ArrayList;

public class ChatObject implements Serializable {

    @SerializedName("chat_data")
    private ArrayList<ChatData> chatData;

    public ArrayList<ChatData> getChatData() {
        return chatData;
    }

    public void setChatData(ArrayList<ChatData> chatData) {
        this.chatData = chatData;
    }
}
