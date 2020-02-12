package com.example.climbtogether.member_activity;

import android.content.Context;

import java.util.ArrayList;

public interface MemberActivityVu {
    Context getVuContext();

    void setRecyclerView(ArrayList<String> btnList);

    void changeView(boolean isShow);

    void intentToLoginActivity();

    void signOut();

    void showConfirmSignOutDialog();

    void intentToBrowser(String url);
}
