package com.example.climbtogether.personal_chat_activity.chat_room_object;

import java.io.Serializable;

public class ChatRoomDTO implements Serializable {

    private String document;

    private String user1;

    private String user2;

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getUser1() {
        return user1;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public String getUser2() {
        return user2;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
    }
}
