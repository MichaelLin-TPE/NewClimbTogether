package com.hiking.climbtogether.equipment_fragment;

import android.content.Context;

import com.hiking.climbtogether.db_modle.EquipmentDTO;
import com.hiking.climbtogether.db_modle.EquipmentListDTO;

import java.util.ArrayList;

public interface EquipmentVu {
    Context getVuContext();

    void setRecyclerView(ArrayList<EquipmentDTO> bodyArrayList, ArrayList<EquipmentDTO> moveArrayList, ArrayList<EquipmentDTO> campArrayList, ArrayList<EquipmentDTO> foodArrayList, ArrayList<EquipmentDTO> electronicArrayList, ArrayList<EquipmentDTO> drogArrayList, ArrayList<EquipmentDTO> otherArrayList);

    void setUpdateData(ArrayList<EquipmentDTO> bodyArrayList, ArrayList<EquipmentDTO> moveArrayList, ArrayList<EquipmentDTO> campArrayList, ArrayList<EquipmentDTO> foodArrayList, ArrayList<EquipmentDTO> electronicArrayList, ArrayList<EquipmentDTO> drogArrayList, ArrayList<EquipmentDTO> otherArrayList);
    void intentToMyEquipmentActivity();

    void intentToLoginActivity();

    void deleteToFirebase(EquipmentDTO data);

    void insertToFirebase(EquipmentDTO data);
}
