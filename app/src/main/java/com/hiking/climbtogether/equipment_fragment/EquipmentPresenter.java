package com.hiking.climbtogether.equipment_fragment;

public interface EquipmentPresenter {
    void onPrepareData();

    void onItemCheckListener(String name, int sid, String sort);

    void onButtonAddListClickListener();

    void onButtonGoListClickListener();

    void onClearView();

    void onNotLoginEvent();
}