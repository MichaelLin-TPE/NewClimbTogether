package com.example.climbtogether.personal_chat_activity.personal_presenter;

import com.example.climbtogether.personal_chat_activity.PersonalChatData;
import com.example.climbtogether.personal_chat_activity.PersonalChatLeftViewHolder;
import com.example.climbtogether.personal_chat_activity.PersonalChatRightViewHolder;

import java.util.ArrayList;

public interface PersonalPresenter {
    void setData(ArrayList<PersonalChatData> chatArrayList);

    int getItemViewType(int position);

    int getItemCount();

    void setCurrentUserEmail(String email);

    void onBindLeftViewHolder(PersonalChatLeftViewHolder holder, int position);

    void setFriendData(String displayName, String friendPhotoUrl);

    void onBindRightViewHolder(PersonalChatRightViewHolder holder, int position);
}