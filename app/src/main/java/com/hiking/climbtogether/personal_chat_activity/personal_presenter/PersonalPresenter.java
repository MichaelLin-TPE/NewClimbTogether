package com.hiking.climbtogether.personal_chat_activity.personal_presenter;

import com.hiking.climbtogether.personal_chat_activity.chat_room_object.PersonalChatData;
import com.hiking.climbtogether.personal_chat_activity.PersonalChatLeftViewHolder;
import com.hiking.climbtogether.personal_chat_activity.PersonalChatRightViewHolder;

import java.util.ArrayList;

public interface PersonalPresenter {
    void setData(ArrayList<PersonalChatData> chatArrayList);

    int getItemViewType(int position);

    int getItemCount();

    void setCurrentUserEmail(String email);

    void onBindLeftViewHolder(PersonalChatLeftViewHolder holder, int position);

    void setFriendData(String displayName, String friendPhotoUrl);

    void onBindRightViewHolder(PersonalChatRightViewHolder holder, int position);

    void setOnPhotoClickListenr(PersonalChatLeftViewHolder holder, PersonalChatLeftViewHolder.OnPhotoClickListenr listener);

    void setOnPhotoClickListenrRight(PersonalChatRightViewHolder holder, PersonalChatLeftViewHolder.OnPhotoClickListenr listener);
}
