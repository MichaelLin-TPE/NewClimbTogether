package com.example.climbtogether.my_equipment_activity;

import com.example.climbtogether.db_modle.DataBaseApi;
import com.example.climbtogether.db_modle.DataBaseImpl;
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
    public void onCatchDataSuccessful(ArrayList<EquipmentListDTO> dataArrayList) {
        mView.setRecyclerView(dataArrayList);
    }


}
