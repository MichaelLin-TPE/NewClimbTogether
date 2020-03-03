package com.example.climbtogether.my_equipment_activity;

import com.example.climbtogether.db_modle.DataBaseApi;
import com.example.climbtogether.db_modle.DataBaseImpl;

public class MyEquipmentPresenterImpl implements MyEquipmentPresenter {

    private MyEquipmentVu mView;

    private DataBaseApi dataBaseApi;

    public MyEquipmentPresenterImpl(MyEquipmentVu mView) {
        this.mView = mView;
        dataBaseApi = new DataBaseImpl(mView.getVuContext());
    }

    @Override
    public void onBackButtonClick() {
        mView.closePage();
    }

    @Override
    public void onPrepareData() {
        if (dataBaseApi.getAllMyEquipment().size() == 0){
            mView.setViewMaintain(true);
            return;
        }
        mView.setRecyclerView(dataBaseApi.getAllMyEquipment());
    }
}
