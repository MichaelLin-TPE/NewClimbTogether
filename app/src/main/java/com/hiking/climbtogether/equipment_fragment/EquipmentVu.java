package com.hiking.climbtogether.equipment_fragment;

import android.content.Context;

import com.hiking.climbtogether.db_modle.EquipmentDTO;
import com.hiking.climbtogether.db_modle.EquipmentListDTO;

import java.util.ArrayList;

public interface EquipmentVu {
    Context getVuContext();

    void setRecyclerView(ArrayList<EquipmentDTO> bodyArrayList, ArrayList<EquipmentDTO> moveArrayList, ArrayList<EquipmentDTO> campArrayList, ArrayList<EquipmentDTO> foodArrayList, ArrayList<EquipmentDTO> electronicArrayList, ArrayList<EquipmentDTO> drogArrayList, ArrayList<EquipmentDTO> otherArrayList);

    void setUpdateData(ArrayList<EquipmentDTO> bodyArrayList, ArrayList<EquipmentDTO> moveArrayList, ArrayList<EquipmentDTO> campArrayList, ArrayList<EquipmentDTO> foodArrayList, ArrayList<EquipmentDTO> electronicArrayList, ArrayList<EquipmentDTO> drogArrayList, ArrayList<EquipmentDTO> otherArrayList);

    void showAddListSuccessfulMessage(String message);
    void intentToMyEquipmentActivity();

    void saveMyEquipmentToFirebase(ArrayList<EquipmentListDTO> myList);

    void setGoToMyEquipmentEnable();

    void intentToLoginActivity();
}
