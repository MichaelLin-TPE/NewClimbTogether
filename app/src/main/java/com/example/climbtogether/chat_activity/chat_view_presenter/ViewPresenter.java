package com.example.climbtogether.chat_activity.chat_view_presenter;

import com.example.climbtogether.chat_activity.ChatData;

import java.util.ArrayList;

public interface ViewPresenter {
    void setData(ArrayList<ChatData> chatDataArrayList, String email);

    int getItemViewType(int position);

    int getItemCount();
}
