package com.hiking.climbtogether.disscuss_fragment;

public interface DiscussFragmentPresenter {

    void onNotLoginEvent();

    void onBtnLoginClickListener();

    void onClearView();

    void onDiscussItemClickListener(String listName);

    void onPrepareData();
}
