package com.hiking.climbtogether;

import com.hiking.climbtogether.tool.FirestoreUserData;

public interface MainActivityVu {

    void applyToken();

    void saveCurrentUserData(FirestoreUserData data);

    void showErrorDialog(String errorCode);

    void intentToHomeActivity();
}
