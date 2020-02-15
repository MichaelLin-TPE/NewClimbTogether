package com.example.climbtogether.chat_activity.chat_view_presenter;

import android.util.Log;

import com.example.climbtogether.chat_activity.ChatData;
import com.example.climbtogether.chat_activity.ChatLeftViewHolder;
import com.example.climbtogether.chat_activity.ChatRightViewHolder;

import java.util.ArrayList;

public class ViewPresenterImpl implements ViewPresenter {

    public static final int LEFT = 0;

    public static final int RIGHT = 1;

    private ArrayList<ChatData> chatDataArrayList;
    private String email;

    private int dataSize;

    @Override
    public void setData(ArrayList<ChatData> chatDataArrayList, String email) {
        this.chatDataArrayList = chatDataArrayList;
        this.email = email;
        dataSize = chatDataArrayList.size();
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

    @Override
    public void onBindRightViewHolder(ChatRightViewHolder holder, int position) {
//        dataSize --;
//        if (dataSize < 0){
//            dataSize = 0;
//        }
        holder.setData(chatDataArrayList.get(position));
    }

    @Override
    public void onBindLeftViewHolder(ChatLeftViewHolder holder, int position) {
//        dataSize --;
//        if (dataSize < 0){
//            dataSize = 0;
//        }
        holder.setData(chatDataArrayList.get(position));
    }
}
