package com.example.climbtogether.personal_fragment;

import android.content.Context;

import java.util.ArrayList;

public interface PersonalFragmentVu {
    void showLoginInformation(boolean isShow);

    void intentToLoginActivity();

    void showProgress(boolean isShow);

    void setRecyclerView(ArrayList<PersonalChatDTO> chatDataArrayList);

    void intentToPersonalChatActivity(String displayName, String friendEmail, String photoUrl);

    void showNoChatDataView(boolean isShow);

    void showDeleteConfirmDialog(String documentPath);

    Context getVuContext();

    String getUserEmail();
}
