package com.example.climbtogether.personal_chat_activity.chat_room_object;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class PersonalChatObject implements Serializable {

    @SerializedName("chat_data")
    private ArrayList<PersonalChatData> chatData;

    public ArrayList<PersonalChatData> getChatData() {
        return chatData;
    }

    public void setChatData(ArrayList<PersonalChatData> chatData) {
        this.chatData = chatData;
    }
}
