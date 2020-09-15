package com.hiking.climbtogether.equipment_fragment;

import android.content.Context;

import com.hiking.climbtogether.db_modle.EquipmentDTO;

import java.util.ArrayList;

public interface EquipmentVu {

    void intentToMyEquipmentActivity();

    void intentToLoginActivity();

    void showErrorCode(String errorCode);

    void setRecyclerViewList(ArrayList<EquipmentDTO> equipmentArrayList);

    void updateListData(ArrayList<EquipmentDTO> equipmentArrayList);
}
