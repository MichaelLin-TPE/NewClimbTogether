package com.hiking.climbtogether.equipment_fragment;

import com.hiking.climbtogether.db_modle.EquipmentDTO;

public interface EquipmentPresenter {
    void onPrepareData();

    void onItemCheckListener(EquipmentDTO data);

    void onButtonGoListClickListener();

    void onClearView();

    void onNotLoginEvent();
}
