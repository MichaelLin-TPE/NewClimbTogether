package com.example.climbtogether.equipment_fragment;

public interface EquipmentPresenter {
    void onPrepareData();

    void onItemCheckListener(String name, int sid, String sort);
}
