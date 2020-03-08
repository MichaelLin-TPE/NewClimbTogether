package com.example.climbtogether.my_equipment_activity.sort_presenter;

import com.example.climbtogether.db_modle.EquipmentListDTO;
import com.example.climbtogether.my_equipment_activity.view.NotPrepareViewHolder;
import com.example.climbtogether.my_equipment_activity.view.PreparedViewHolder;
import com.example.climbtogether.my_equipment_activity.view.SortAdapter;
import com.example.climbtogether.my_equipment_activity.view.SortPreparedAdapter;

import java.util.ArrayList;

public interface SortPresenter {
    int getItemCount();

    int getItemViewType(int position);

    void setNotPrepareData(ArrayList<EquipmentListDTO> notPrepareArrayList);

    void setPreparedData(ArrayList<EquipmentListDTO> preparedArrayList);

    void onBindViewHolder(NotPrepareViewHolder holder, int position);

    void onBindPrepareViewHolder(PreparedViewHolder holder, int position);

    void setOnSortItemClickListener(PreparedViewHolder holder, SortPreparedAdapter.OnSortPreparedItemClickListener listener);

    void setOnSortItemClickListener(NotPrepareViewHolder holder, SortAdapter.OnSortItemClickListener listener);
}
