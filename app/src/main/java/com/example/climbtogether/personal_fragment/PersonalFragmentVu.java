package com.example.climbtogether.personal_fragment;

import java.util.ArrayList;

public interface PersonalFragmentVu {
    void showLoginInformation(boolean isShow);

    void intentToLoginActivity();

    void showProgress(boolean isShow);

    void searchForChatData(String currentEmail, ArrayList<String> friendsArrayList);

    void setRecyclerView(ArrayList<PersonalChatDTO> chatDataArrayList);

    void intentToPersonalChatActivity(String displayName, String friendEmail, String photoUrl);
}
