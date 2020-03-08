package com.example.climbtogether.my_equipment_activity;

import com.example.climbtogether.db_modle.EquipmentListDTO;

import java.util.ArrayList;

public class MyEquipmentPresenterImpl implements MyEquipmentPresenter {

    private MyEquipmentVu mView;


    public MyEquipmentPresenterImpl(MyEquipmentVu mView) {
        this.mView = mView;

    }

    @Override
    public void onBackButtonClick() {
        mView.closePage();
    }

    @Override
    public void onCatchDataSuccessful(ArrayList<EquipmentListDTO> notPrepareArrayList, ArrayList<EquipmentListDTO> preparedArrayList) {
        mView.setRecyclerView(notPrepareArrayList,preparedArrayList);
    }

    @Override
    public void onViewMaintain() {
        mView.setViewMaintain(true);
    }


}
