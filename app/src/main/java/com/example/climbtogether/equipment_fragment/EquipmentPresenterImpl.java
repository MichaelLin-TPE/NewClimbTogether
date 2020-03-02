package com.example.climbtogether.equipment_fragment;

import android.util.Log;

import com.example.climbtogether.db_modle.DataBaseApi;
import com.example.climbtogether.db_modle.DataBaseImpl;
import com.example.climbtogether.db_modle.EquipmentDTO;

import java.util.ArrayList;

public class EquipmentPresenterImpl implements EquipmentPresenter {

    private EquipmentVu mView;

    private DataBaseApi dataBaseApi;

    private ArrayList<EquipmentDTO> bodyArrayList, moveArrayList, campArrayList, foodArrayList, electronicArrayList, drogArrayList, otherArrayList;

    public static final String BODY = "boy_stuff";

    public static final String MOVE = "move_stuff";

    public static final String CAMP = "camp_stuff";

    public static final String FOOD = "food_stuff";

    public static final String ELECTRONIC = "elect_stuff";

    public static final String DROG = "drog_stuff";

    public static final String OTHER = "other_stuff";

    public EquipmentPresenterImpl(EquipmentVu mView) {
        this.mView = mView;

        dataBaseApi = new DataBaseImpl(mView.getVuContext());

    }

    @Override
    public void onPrepareData() {
        bodyArrayList = dataBaseApi.getStuffInformation(BODY);
        moveArrayList = dataBaseApi.getStuffInformation(MOVE);
        campArrayList = dataBaseApi.getStuffInformation(CAMP);
        foodArrayList = dataBaseApi.getStuffInformation(FOOD);
        electronicArrayList = dataBaseApi.getStuffInformation(ELECTRONIC);
        drogArrayList = dataBaseApi.getStuffInformation(DROG);
        otherArrayList = dataBaseApi.getStuffInformation(OTHER);

        mView.setRecyclerView(bodyArrayList, moveArrayList, campArrayList, foodArrayList, electronicArrayList, drogArrayList, otherArrayList);
    }

    @Override
    public void onItemCheckListener(String name, int sid, String sort) {
        EquipmentDTO data = null;
        //還差Insert
        switch (sort) {
            case "body":
                Log.i("Michael","body 資料變化 : "+sid);
                data = dataBaseApi.getEquipmentBySid(BODY,sid);
                if (data.getCheck().equals("false")){
                    Log.i("Michael","check 改成 true"+" , 名字為 : "+data.getName());
                    data.setCheck("true");
                }else {
                    Log.i("Michael","check 改成 false");
                    data.setCheck("false");
                }
                dataBaseApi.updateEquipmentData(data,BODY);
                break;
            case "camp":
                Log.i("Michael","camp 資料變化 : "+sid);
                data = dataBaseApi.getEquipmentBySid(CAMP,sid);
                if (data.getCheck().equals("false")){
                    data.setCheck("true");
                }else {
                    data.setCheck("false");
                }
                dataBaseApi.updateEquipmentData(data,CAMP);
                break;
            case "drog":
                data = dataBaseApi.getEquipmentBySid(DROG,sid);
                if (data.getCheck().equals("false")){
                    data.setCheck("true");
                }else {
                    data.setCheck("false");
                }
                dataBaseApi.updateEquipmentData(data,DROG);
                break;
            case "elect":
                Log.i("Michael","elect 資料變化 : "+sid);
                data = dataBaseApi.getEquipmentBySid(ELECTRONIC,sid);
                if (data.getCheck().equals("false")){
                    data.setCheck("true");
                }else {
                    data.setCheck("false");
                }
                dataBaseApi.updateEquipmentData(data,ELECTRONIC);
                break;
            case "food":
                Log.i("Michael","food 資料變化 : "+sid);
                data = dataBaseApi.getEquipmentBySid(FOOD,sid);
                if (data.getCheck().equals("false")){
                    data.setCheck("true");
                }else {
                    data.setCheck("false");
                }
                dataBaseApi.updateEquipmentData(data,FOOD);
                break;
            case "move":
                Log.i("Michael","move 資料變化 : "+sid);
                data = dataBaseApi.getEquipmentBySid(MOVE,sid);
                if (data.getCheck().equals("false")){
                    data.setCheck("true");
                }else {
                    data.setCheck("false");
                }
                dataBaseApi.updateEquipmentData(data,MOVE);
                break;
            case "other":
                Log.i("Michael","other 資料變化 : "+sid);
                data = dataBaseApi.getEquipmentBySid(OTHER,sid);
                if (data.getCheck().equals("false")){
                    data.setCheck("true");
                }else {
                    data.setCheck("false");
                }
                dataBaseApi.updateEquipmentData(data,OTHER);
                break;
        }

        bodyArrayList = dataBaseApi.getStuffInformation(BODY);
        moveArrayList = dataBaseApi.getStuffInformation(MOVE);
        campArrayList = dataBaseApi.getStuffInformation(CAMP);
        foodArrayList = dataBaseApi.getStuffInformation(FOOD);
        electronicArrayList = dataBaseApi.getStuffInformation(ELECTRONIC);
        drogArrayList = dataBaseApi.getStuffInformation(DROG);
        otherArrayList = dataBaseApi.getStuffInformation(OTHER);

        mView.setUpdateData(bodyArrayList,moveArrayList,campArrayList,foodArrayList,electronicArrayList,drogArrayList,otherArrayList);
    }
}
