package com.hiking.climbtogether.my_equipment_activity;

import com.hiking.climbtogether.db_modle.EquipmentListDTO;

import java.util.ArrayList;

public interface MyEquipmentPresenter {
    void onBackButtonClick();

    void onCatchDataSuccessful(ArrayList<EquipmentListDTO> dataArrayList, ArrayList<EquipmentListDTO> preparedArrayList);

    void onViewMaintain();
}
