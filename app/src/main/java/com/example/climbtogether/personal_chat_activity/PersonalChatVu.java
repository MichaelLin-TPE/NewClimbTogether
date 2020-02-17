package com.example.climbtogether.personal_chat_activity;

public interface PersonalChatVu {
    void closePage();

    void showToast(String message);

    void createDataToFirestroe(String message, long time);
}
