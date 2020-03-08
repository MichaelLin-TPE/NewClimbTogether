package com.example.climbtogether.my_equipment_activity.sort_presenter;

import com.example.climbtogether.db_modle.EquipmentListDTO;
import com.example.climbtogether.my_equipment_activity.view.NotPrepareViewHolder;
import com.example.climbtogether.my_equipment_activity.view.PreparedViewHolder;
import com.example.climbtogether.my_equipment_activity.view.SortAdapter;
import com.example.climbtogether.my_equipment_activity.view.SortPreparedAdapter;

import java.util.ArrayList;

public class SortPresenterImpl implements SortPresenter {

    public static final int PREPARED = 0;

    public static final int NOT_PREPARE = 1;

    private boolean isShowNotPrepare, isShowPrepared;

    private ArrayList<EquipmentListDTO> notPrepareArrayList, preparedArrayList;


    @Override
    public int getItemCount() {
        int count = 0;
        if (isShowNotPrepare){
            count ++;
        }
        if (isShowPrepared){
            count++;
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowNotPrepare && position == 0){
            return NOT_PREPARE;
        }
        if (isShowPrepared && position == 1){
            return PREPARED;
        }
        return 0;
    }

    @Override
    public void setNotPrepareData(ArrayList<EquipmentListDTO> notPrepareArrayList) {
        this.notPrepareArrayList = notPrepareArrayList;
        isShowNotPrepare = true;
    }

    @Override
    public void setPreparedData(ArrayList<EquipmentListDTO> preparedArrayList) {
        if (preparedArrayList.size() != 0) {
            this.preparedArrayList = preparedArrayList;
            isShowPrepared = true;
        }else {
            isShowPrepared = false;
        }
    }

    @Override
    public void onBindViewHolder(NotPrepareViewHolder holder, int position) {
        holder.setData(notPrepareArrayList);
    }

    @Override
    public void onBindPrepareViewHolder(PreparedViewHolder holder, int position) {
        holder.setData(preparedArrayList);
    }

    @Override
    public void setOnSortItemClickListener(PreparedViewHolder holder, SortPreparedAdapter.OnSortPreparedItemClickListener listener) {
        holder.setOnSortItemClickListener(listener);
    }

    @Override
    public void setOnSortItemClickListener(NotPrepareViewHolder holder, SortAdapter.OnSortItemClickListener listener) {
        holder.setOnSortItemClickListener(listener);
    }
}
