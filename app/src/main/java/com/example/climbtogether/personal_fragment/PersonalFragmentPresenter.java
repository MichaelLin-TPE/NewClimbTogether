package com.example.climbtogether.personal_fragment;

import java.util.ArrayList;

public interface PersonalFragmentPresenter {
    void onNoUserEvent();

    void onLoginClickListener();


    void onShowProgress(boolean isShow);

    void onItemClickListener(String displayName, String friendEmail, String photoUrl);

    void onShowDeleteMessageConfirmDialog(String documentPath);

    void onCatchallData(ArrayList<String> jsonArray);
}
