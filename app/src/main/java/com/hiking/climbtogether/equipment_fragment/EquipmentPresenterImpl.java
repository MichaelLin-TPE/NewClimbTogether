package com.hiking.climbtogether.equipment_fragment;

import android.util.Log;

import com.google.gson.Gson;
import com.hiking.climbtogether.db_modle.DataBaseApi;
import com.hiking.climbtogether.db_modle.DataBaseImpl;
import com.hiking.climbtogether.db_modle.EquipmentDTO;
import com.hiking.climbtogether.db_modle.EquipmentListDTO;
import com.hiking.climbtogether.tool.FirebaseHandler;
import com.hiking.climbtogether.tool.FirebaseHandlerImpl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class EquipmentPresenterImpl implements EquipmentPresenter {

    private EquipmentVu mView;

    private ArrayList<EquipmentDTO> equipmentArrayList;

    private FirebaseHandler firebaseHandler;

    public EquipmentPresenterImpl(EquipmentVu mView) {
        this.mView = mView;

        firebaseHandler = new FirebaseHandlerImpl();

    }

    private FirebaseHandler.OnConnectFireStoreListener<ArrayList<EquipmentDTO>> onGetEquipmentApiListener = new FirebaseHandler.OnConnectFireStoreListener<ArrayList<EquipmentDTO>>() {
        @Override
        public void onSuccess(ArrayList<EquipmentDTO> equipmentArrayList) {
            EquipmentPresenterImpl.this.equipmentArrayList = equipmentArrayList;
            mView.setRecyclerViewList(equipmentArrayList);
        }

        @Override
        public void onFail(String errorCode) {
            mView.showErrorCode(errorCode);
        }
    };

    @Override
    public void onPrepareData() {

        firebaseHandler.getEquipmentApi(onGetEquipmentApiListener);

    }

    @Override
    public void onItemCheckListener(EquipmentDTO data) {





        for (EquipmentDTO equipmentDTO : equipmentArrayList){
            if (equipmentDTO.getName().equals(data.getName())){
                if (data.getCheck().equals("false")){
                    equipmentDTO.setCheck("true");
                }else {
                    equipmentDTO.setCheck("false");
                }
                if (!firebaseHandler.isLogin()){
                    return;
                }
                updateMyFavoriteData(equipmentDTO);
                break;
            }
        }
        mView.updateListData(equipmentArrayList);


    }

    private void updateMyFavoriteData(EquipmentDTO equipmentDTO) {
        firebaseHandler.onUpdateAndDeleteEquipmentData(equipmentDTO);
    }

    @Override
    public void onButtonGoListClickListener() {
        if (firebaseHandler.isLogin()){
            mView.intentToMyEquipmentActivity();
        }else {
            mView.intentToLoginActivity();
        }

    }

    @Override
    public void onClearView() {
        for (EquipmentDTO data : equipmentArrayList){
            data.setCheck("false");
        }
        mView.updateListData(equipmentArrayList);
    }

    @Override
    public void onNotLoginEvent() {
        mView.intentToLoginActivity();
    }
}
