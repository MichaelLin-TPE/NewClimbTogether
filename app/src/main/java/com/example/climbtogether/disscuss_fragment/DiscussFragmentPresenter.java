package com.example.climbtogether.disscuss_fragment;

import java.util.ArrayList;

public interface DiscussFragmentPresenter {

    void onNotLoginEvent();

    void onBtnLoginClickListener();

    void onClearView();

    void onDiscussItemClickListener(String listName);

    void onPrepareData();
}
