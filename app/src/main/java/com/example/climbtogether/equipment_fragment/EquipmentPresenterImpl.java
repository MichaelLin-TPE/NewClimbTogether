package com.example.climbtogether.equipment_fragment;

import android.util.Log;

import com.example.climbtogether.db_modle.DataBaseApi;
import com.example.climbtogether.db_modle.DataBaseImpl;
import com.example.climbtogether.db_modle.EquipmentDTO;
import com.example.climbtogether.db_modle.EquipmentListDTO;

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

    public static final String EQUIPMENT = "equipment_list";

    private ArrayList<EquipmentListDTO> myList;

    public EquipmentPresenterImpl(EquipmentVu mView) {
        this.mView = mView;

        dataBaseApi = new DataBaseImpl(mView.getVuContext());

        myList = new ArrayList<>();

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


        for (EquipmentDTO data : bodyArrayList){
            if (data.getCheck().equals("true")){
                insertMyList(data);
            }
        }
        for (EquipmentDTO data : moveArrayList){
            if (data.getCheck().equals("true")){
                insertMyList(data);
            }
        }
        for (EquipmentDTO data : campArrayList){
            if (data.getCheck().equals("true")){
                insertMyList(data);
            }
        }
        for (EquipmentDTO data : foodArrayList){
            if (data.getCheck().equals("true")){
                insertMyList(data);
            }
        }
        for (EquipmentDTO data : electronicArrayList){
            if (data.getCheck().equals("true")){
                insertMyList(data);
            }
        }
        for (EquipmentDTO data : drogArrayList){
            if (data.getCheck().equals("true")){
                insertMyList(data);
            }
        }
        for (EquipmentDTO data : otherArrayList){
            if (data.getCheck().equals("true")){
                insertMyList(data);
            }
        }

        mView.setRecyclerView(bodyArrayList, moveArrayList, campArrayList, foodArrayList, electronicArrayList, drogArrayList, otherArrayList);
    }

    @Override
    public void onItemCheckListener(String name, int sid, String sort) {
        EquipmentDTO data = null;
        Log.i("Michael","分類 : "+sort);
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

        insertMyList(data);

        bodyArrayList = dataBaseApi.getStuffInformation(BODY);
        moveArrayList = dataBaseApi.getStuffInformation(MOVE);
        campArrayList = dataBaseApi.getStuffInformation(CAMP);
        foodArrayList = dataBaseApi.getStuffInformation(FOOD);
        electronicArrayList = dataBaseApi.getStuffInformation(ELECTRONIC);
        drogArrayList = dataBaseApi.getStuffInformation(DROG);
        otherArrayList = dataBaseApi.getStuffInformation(OTHER);

        mView.setUpdateData(bodyArrayList,moveArrayList,campArrayList,foodArrayList,electronicArrayList,drogArrayList,otherArrayList);
    }

    private void insertMyList(EquipmentDTO data) {
        EquipmentListDTO list = new EquipmentListDTO();
        int listIndex = 0;
        boolean isRepeat = false;

        if (myList.size() != 0){
            for (int i = 0 ; i < myList.size() ; i ++){
                if (myList.get(i).getName().equals(data.getName())){
                    listIndex = i;
                    isRepeat = true;

                    break;
                }
            }
        }
        if (isRepeat){
            Log.i("Michael","資料刪除");
            myList.remove(listIndex);
        }else {
            list.setName(data.getName());
            Log.i("Michael","資料新增成功");
            list.setDescription(data.getDescription());
            myList.add(list);
        }


    }

    @Override
    public void onButtonAddListClickListener() {
        if (myList != null && myList.size() != 0){
//            for (EquipmentListDTO data : myList){
//                dataBaseApi.insert(data);
//            }
            mView.setGoToMyEquipmentEnable();
            mView.saveMyEquipmentToFirebase(myList);
        }else {
            String message = "請選擇你要的裝備";
            mView.showAddListSuccessfulMessage(message);
        }

    }

    private void resetDatabase() {

        bodyArrayList = dataBaseApi.getStuffInformation(BODY);
        moveArrayList = dataBaseApi.getStuffInformation(MOVE);
        campArrayList = dataBaseApi.getStuffInformation(CAMP);
        foodArrayList = dataBaseApi.getStuffInformation(FOOD);
        electronicArrayList = dataBaseApi.getStuffInformation(ELECTRONIC);
        drogArrayList = dataBaseApi.getStuffInformation(DROG);
        otherArrayList = dataBaseApi.getStuffInformation(OTHER);

        for (EquipmentDTO data : bodyArrayList){
            if (data.getCheck().equals("true")){
                Log.i("Michael","資料 : "+data.getSid()+" , 改成false");
                updateData(BODY,data.getSid());
            }
        }
        for (EquipmentDTO data : moveArrayList){
            if (data.getCheck().equals("true")){
                updateData(MOVE,data.getSid());
            }
        }
        for (EquipmentDTO data : campArrayList){
            if (data.getCheck().equals("true")){
                updateData(CAMP,data.getSid());
            }
        }
        for (EquipmentDTO data : foodArrayList){
            if (data.getCheck().equals("true")){
                updateData(FOOD,data.getSid());
            }
        }
        for (EquipmentDTO data : electronicArrayList){
            if (data.getCheck().equals("true")){
                updateData(ELECTRONIC,data.getSid());
            }
        }
        for (EquipmentDTO data : drogArrayList){
            if (data.getCheck().equals("true")){
                updateData(DROG,data.getSid());
            }
        }
        for (EquipmentDTO data : otherArrayList){
            if (data.getCheck().equals("true")){
                updateData(OTHER,data.getSid());
            }
        }
        Log.i("Michael","更新畫面");

        bodyArrayList = dataBaseApi.getStuffInformation(BODY);
        moveArrayList = dataBaseApi.getStuffInformation(MOVE);
        campArrayList = dataBaseApi.getStuffInformation(CAMP);
        foodArrayList = dataBaseApi.getStuffInformation(FOOD);
        electronicArrayList = dataBaseApi.getStuffInformation(ELECTRONIC);
        drogArrayList = dataBaseApi.getStuffInformation(DROG);
        otherArrayList = dataBaseApi.getStuffInformation(OTHER);

        mView.setUpdateData(bodyArrayList,moveArrayList,campArrayList,foodArrayList,electronicArrayList,drogArrayList,otherArrayList);
        myList = new ArrayList<>();
    }

    @Override
    public void onButtonGoListClickListener() {
        mView.intentToMyEquipmentActivity();
    }

    @Override
    public void onClearView() {
        resetDatabase();
    }

    @Override
    public void onNotLoginEvent() {
        mView.intentToLoginActivity();
    }

    private void updateData(String table ,int sid) {
        Log.i("Michael","重製資料");
        EquipmentDTO data = dataBaseApi.getEquipmentBySid(table,sid);
        data.setCheck("false");
        dataBaseApi.updateEquipmentData(data,table);
    }
}
