package com.example.climbtogether.my_equipment_activity;

import android.content.Context;

import com.example.climbtogether.db_modle.EquipmentListDTO;

import java.util.ArrayList;

public interface MyEquipmentVu {
    void closePage();

    Context getVuContext();

    void setViewMaintain( boolean isShow);

    void setRecyclerView(ArrayList<EquipmentListDTO> allMyEquipment);
}
