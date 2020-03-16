package com.hiking.climbtogether.disscuss_fragment;

import android.content.Context;

import java.util.ArrayList;

public interface DiscussFragmentVu {

    void setViewMaintain(boolean isShow);

    void intentToLoginActivity();

    void setRecyclerView(ArrayList<String> listArrayList);

    void showProgressbar(boolean isShow);

    void clearView();

    void intentToChatActivity(String listName);

    void intentToShareActivity(String listName);

    Context getVuContext();
}
