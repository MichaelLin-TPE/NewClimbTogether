package com.example.climbtogether.personal_chat_activity.chat_room_object;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class PersonalChatObject implements Serializable {

    @SerializedName("chat_data")
    private ArrayList<PersonalChatData> chatData;
    @SerializedName("user_one_data")
    private UserOneDataDTO userOneDataDTO;
    @SerializedName("user_two_data")
    private UserTwoDataDTO userTwoDataDTO;

    public UserOneDataDTO getUserOneDataDTO() {
        return userOneDataDTO;
    }

    public void setUserOneDataDTO(UserOneDataDTO userOneDataDTO) {
        this.userOneDataDTO = userOneDataDTO;
    }

    public UserTwoDataDTO getUserTwoDataDTO() {
        return userTwoDataDTO;
    }

    public void setUserTwoDataDTO(UserTwoDataDTO userTwoDataDTO) {
        this.userTwoDataDTO = userTwoDataDTO;
    }

    public ArrayList<PersonalChatData> getChatData() {
        return chatData;
    }

    public void setChatData(ArrayList<PersonalChatData> chatData) {
        this.chatData = chatData;
    }
}
