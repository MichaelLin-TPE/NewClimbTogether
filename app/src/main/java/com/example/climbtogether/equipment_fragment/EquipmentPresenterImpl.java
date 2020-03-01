package com.example.climbtogether.equipment_fragment;

import com.example.climbtogether.db_modle.DataBaseApi;
import com.example.climbtogether.db_modle.DataBaseImpl;
import com.example.climbtogether.db_modle.EquipmentDTO;

import java.util.ArrayList;

public class EquipmentPresenterImpl implements EquipmentPresenter {

    private EquipmentVu mView;

    private DataBaseApi dataBaseApi;

    private ArrayList<EquipmentDTO> bodyArrayList,moveArrayList,campArrayList,foodArrayList,electronicArrayList,drogArrayList,otherArrayList;

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

        mView.setRecyclerView(bodyArrayList,moveArrayList,campArrayList,foodArrayList,electronicArrayList,drogArrayList,otherArrayList);
    }
}
