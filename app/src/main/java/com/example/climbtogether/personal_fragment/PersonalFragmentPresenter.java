package com.example.climbtogether.personal_fragment;

import java.util.ArrayList;

public interface PersonalFragmentPresenter {
    void onNoUserEvent();

    void onLoginClickListener();

    void onSearchUserChatData(String email, ArrayList<String> friendsArrayList);

    void onShowProgress(boolean isShow);

    void onCatchAllDataSucessful(ArrayList<PersonalChatDTO> chatDataArrayList);

    void onItemClickListener(String displayName, String friendEmail, String photoUrl);

    void onNoMessageEvent();

    void onShowDeleteMessageConfirmDialog(String documentPath);
}
