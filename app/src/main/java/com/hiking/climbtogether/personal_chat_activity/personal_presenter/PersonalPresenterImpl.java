package com.hiking.climbtogether.personal_chat_activity.personal_presenter;

import com.hiking.climbtogether.personal_chat_activity.chat_room_object.PersonalChatData;
import com.hiking.climbtogether.personal_chat_activity.PersonalChatLeftViewHolder;
import com.hiking.climbtogether.personal_chat_activity.PersonalChatRightViewHolder;

import java.util.ArrayList;

public class PersonalPresenterImpl implements PersonalPresenter{

    public static final int LEFT = 0;

    public static final int RIGHT = 1;

    private ArrayList<PersonalChatData> chatArrayList;
    private String displayName;
    private String friendPhotoUrl;
    private String currentUserEmail;

    @Override
    public void setData(ArrayList<PersonalChatData> chatArrayList) {

        this.chatArrayList = chatArrayList;
    }

    @Override
    public int getItemViewType(int position) {
        PersonalChatData data = chatArrayList.get(position);
        if (currentUserEmail.equals(data.getEmail())){
            return RIGHT;
        }else {
            return LEFT;
        }
    }

    @Override
    public int getItemCount() {
        return chatArrayList == null ? 0 : chatArrayList.size();
    }

    @Override
    public void setCurrentUserEmail(String currentUserEmail) {
        this.currentUserEmail = currentUserEmail;
    }

    @Override
    public void onBindLeftViewHolder(PersonalChatLeftViewHolder holder, int position) {
        holder.setData(chatArrayList.get(position),displayName,friendPhotoUrl);
    }

    @Override
    public void setFriendData(String displayName, String friendPhotoUrl) {
        this.displayName = displayName;
        this.friendPhotoUrl = friendPhotoUrl;
    }

    @Override
    public void onBindRightViewHolder(PersonalChatRightViewHolder holder, int position) {
        holder.setData(chatArrayList.get(position));
    }

    @Override
    public void setOnPhotoClickListenr(PersonalChatLeftViewHolder holder, PersonalChatLeftViewHolder.OnPhotoClickListenr listener) {
        holder.setOnPhotoClickListenr(listener);
    }

    @Override
    public void setOnPhotoClickListenrRight(PersonalChatRightViewHolder holder, PersonalChatLeftViewHolder.OnPhotoClickListenr listener) {
        holder.setOnPhotoClicklistener(listener);
    }
}
