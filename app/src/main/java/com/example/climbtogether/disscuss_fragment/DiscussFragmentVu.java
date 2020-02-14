package com.example.climbtogether.disscuss_fragment;

import java.util.ArrayList;

public interface DiscussFragmentVu {
    void searchFirestoreData();

    void setViewMaintain(boolean isShow);

    void intentToLoginActivity();

    void setRecyclerView(ArrayList<String> listArrayList);

    void showProgressbar(boolean isShow);

    void clearView();

    void intentToChatActivity(String listName);
}
