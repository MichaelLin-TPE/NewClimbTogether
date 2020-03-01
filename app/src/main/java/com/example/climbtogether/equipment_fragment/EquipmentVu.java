package com.example.climbtogether.equipment_fragment;

import android.content.Context;

import com.example.climbtogether.db_modle.EquipmentDTO;

import java.util.ArrayList;

public interface EquipmentVu {
    Context getVuContext();

    void setRecyclerView(ArrayList<EquipmentDTO> bodyArrayList, ArrayList<EquipmentDTO> moveArrayList, ArrayList<EquipmentDTO> campArrayList, ArrayList<EquipmentDTO> foodArrayList, ArrayList<EquipmentDTO> electronicArrayList, ArrayList<EquipmentDTO> drogArrayList, ArrayList<EquipmentDTO> otherArrayList);
}
