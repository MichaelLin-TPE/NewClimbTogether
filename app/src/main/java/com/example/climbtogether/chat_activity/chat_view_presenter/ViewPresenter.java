package com.example.climbtogether.chat_activity.chat_view_presenter;

import com.example.climbtogether.chat_activity.ChatData;
import com.example.climbtogether.chat_activity.ChatLeftViewHolder;
import com.example.climbtogether.chat_activity.ChatRightViewHolder;

import java.util.ArrayList;

public interface ViewPresenter {
    void setData(ArrayList<ChatData> chatDataArrayList, String email);

    int getItemViewType(int position);

    int getItemCount();

    void onBindRightViewHolder(ChatRightViewHolder holder, int position);

    void onBindLeftViewHolder(ChatLeftViewHolder holder, int position);

    void setOnUserPhotoClickListener(ChatLeftViewHolder.OnUserPhotoClickListener listener, ChatLeftViewHolder holder);
}
