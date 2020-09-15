package com.hiking.climbtogether.equipment_fragment.stuff_presenter;

import com.hiking.climbtogether.db_modle.EquipmentDTO;
import com.hiking.climbtogether.equipment_fragment.EquipmentViewHolder;
import com.hiking.climbtogether.equipment_fragment.StuffItemAdapter;

import java.util.ArrayList;

public interface StuffPresenter {
    int getItemViewType(int position);

    int getItemCount();

    void onBindViewHolder(EquipmentViewHolder holder, int position);

    void setOnItemCheckBoxListener(EquipmentViewHolder holder, StuffItemAdapter.OnItemCheckBoxClickListener listener);

    void setListData(ArrayList<EquipmentDTO> equipmentArrayList);
}
