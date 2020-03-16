package com.hiking.climbtogether.equipment_fragment.stuff_presenter;

import com.hiking.climbtogether.db_modle.EquipmentDTO;
import com.hiking.climbtogether.equipment_fragment.EquipmentViewHolder;
import com.hiking.climbtogether.equipment_fragment.StuffItemAdapter;

import java.util.ArrayList;

public interface StuffPresenter {
    int getItemViewType(int position);

    int getItemCount();

    void setBodyData(ArrayList<EquipmentDTO> bodyArrayList);

    void setMoveData(ArrayList<EquipmentDTO> moveArrayList);

    void setCampData(ArrayList<EquipmentDTO> campArrayList);

    void setFoodData(ArrayList<EquipmentDTO> foodArrayList);

    void setElectronicData(ArrayList<EquipmentDTO> electronicArrayList);

    void setDrogData(ArrayList<EquipmentDTO> drogArrayList);

    void setOtherData(ArrayList<EquipmentDTO> otherArrayList);

    void onBindViewHolder(EquipmentViewHolder holder, int position);

    void setOnItemCheckBoxListener(EquipmentViewHolder holder, StuffItemAdapter.OnItemCheckBoxClickListener listener);
}
