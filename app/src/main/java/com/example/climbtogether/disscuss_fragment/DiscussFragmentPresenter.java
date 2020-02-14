package com.example.climbtogether.disscuss_fragment;

import java.util.ArrayList;

public interface DiscussFragmentPresenter {
    void onSearchFirestoreData();

    void onNotLoginEvent();

    void onBtnLoginClickListener();

    void onCatchDataSuccessful(ArrayList<String> listArrayList);

    void onShowProgressbar();

    void onClearView();

    void onDiscussItemClickListener(String listName);
}
