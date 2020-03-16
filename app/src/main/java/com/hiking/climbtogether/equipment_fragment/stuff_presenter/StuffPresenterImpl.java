package com.hiking.climbtogether.equipment_fragment.stuff_presenter;

import com.hiking.climbtogether.db_modle.EquipmentDTO;
import com.hiking.climbtogether.equipment_fragment.EquipmentViewHolder;
import com.hiking.climbtogether.equipment_fragment.StuffItemAdapter;

import java.util.ArrayList;

public class StuffPresenterImpl implements StuffPresenter {

    private ArrayList<EquipmentDTO> bodyArrayList,moveArrayList,campArrayList,foodArrayList,electronicArrayList,drogArrayList,otherArrayList;

    private boolean isBody,isMove,isCamp,isFood,isElectronic,isDrog,isOther;

    public static final int BODY = 0;

    public static final int MOVE = 1;

    public static final int CAMP = 2;

    public static final int FOOD = 3;

    public static final int ELECTRONIC = 4;

    public static final int DROG = 5;

    public static final int OTHER = 6;

    @Override
    public int getItemViewType(int position) {
        if (isBody && position == 0){
            return BODY;
        }
        if (isMove && position ==1){
            return MOVE;
        }
        if (isCamp && position == 2){
            return CAMP;
        }
        if (isFood && position == 3){
            return FOOD;
        }
        if (isElectronic && position == 4){
            return ELECTRONIC;
        }
        if (isDrog && position == 5){
            return DROG;
        }
        if (isOther && position == 6){
            return OTHER;
        }

        return 0;
    }

    @Override
    public int getItemCount() {

        return 7;
    }

    @Override
    public void setBodyData(ArrayList<EquipmentDTO> bodyArrayList) {
        this.bodyArrayList = bodyArrayList;
        isBody = true;
    }

    @Override
    public void setMoveData(ArrayList<EquipmentDTO> moveArrayList) {
        this.moveArrayList = moveArrayList;
        isMove = true;
    }

    @Override
    public void setCampData(ArrayList<EquipmentDTO> campArrayList) {
        this.campArrayList = campArrayList;
        isCamp = true;
    }

    @Override
    public void setFoodData(ArrayList<EquipmentDTO> foodArrayList) {
        this.foodArrayList = foodArrayList;
        isFood = true;
    }

    @Override
    public void setElectronicData(ArrayList<EquipmentDTO> electronicArrayList) {
        this.electronicArrayList = electronicArrayList;
        isElectronic = true;
    }

    @Override
    public void setDrogData(ArrayList<EquipmentDTO> drogArrayList) {
        this.drogArrayList = drogArrayList;
        isDrog = true;
    }

    @Override
    public void setOtherData(ArrayList<EquipmentDTO> otherArrayList) {
        this.otherArrayList = otherArrayList;
        isOther = true;
    }

    @Override
    public void onBindViewHolder(EquipmentViewHolder holder, int position) {
        String title;
        if (position == 0){
            title = "人身物品";
            holder.setData(bodyArrayList,title);
        }
        if (position == 1){
            title = "行動裝備";
            holder.setData(moveArrayList,title);
        }
        if (position == 2){
            title = "住宿露營";
            holder.setData(campArrayList,title);
        }
        if (position == 3){
            title = "飲食餐具";
            holder.setData(foodArrayList,title);
        }
        if (position == 4){
            title = "電子產品";
            holder.setData(electronicArrayList,title);
        }
        if (position == 5){
            title = "衛生藥品";
            holder.setData(drogArrayList,title);
        }
        if (position == 6){
            title = "其他配件";
            holder.setData(otherArrayList,title);
        }
    }

    @Override
    public void setOnItemCheckBoxListener(EquipmentViewHolder holder, StuffItemAdapter.OnItemCheckBoxClickListener listener) {
        holder.setOnItemCheckBoxClickListener(listener);
    }


}
