package com.example.climbtogether.chat_activity.chat_view_presenter;

import com.example.climbtogether.chat_activity.ChatData;

import java.util.ArrayList;

public class ViewPresenterImpl implements ViewPresenter {

    public static final int LEFT = 0;

    public static final int RIGHT = 1;

    private ArrayList<ChatData> chatDataArrayList;
    private String email;

    @Override
    public void setData(ArrayList<ChatData> chatDataArrayList, String email) {
        this.chatDataArrayList = chatDataArrayList;
        this.email = email;
    }

    @Override
    public int getItemViewType(int position) {

        if (email.equals(chatDataArrayList.get(position).getEmail())){
            return RIGHT;
        }else {
            return LEFT;
        }
    }

    @Override
    public int getItemCount() {
        return chatDataArrayList == null ? 0 : chatDataArrayList.size();
    }
}
