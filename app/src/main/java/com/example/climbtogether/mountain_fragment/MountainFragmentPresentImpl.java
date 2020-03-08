package com.example.climbtogether.mountain_fragment;

import android.util.Log;

import com.example.climbtogether.R;
import com.example.climbtogether.db_modle.DataBaseApi;
import com.example.climbtogether.db_modle.DataBaseImpl;
import com.example.climbtogether.db_modle.DataDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MountainFragmentPresentImpl implements MountainFragmentPresenter {

    private MountainFragmentVu mView;

    private static final int NO_SORT = 0;

    private static final int ORDER_BY_NOT_FAR = 1;

    private static final int ORDER_BY_FAR = 2;
    
    private DataBaseApi db;

    String levelType;

    public MountainFragmentPresentImpl(MountainFragmentVu mView) {
        this.mView = mView;
        db = new DataBaseImpl(mView.getVuContext());
    }

    @Override
    public void onFilterTextViewBackgroundChangeListener(int textType, String levelType) {
        Log.i("Michael", "篩選了 : " + levelType);
        this.levelType = levelType;
        mView.showTextViewBackgroundChange(textType);
        if (levelType.equals(mView.getVuContext().getResources().getString(R.string.all))) {
            mView.showSearchNoDataInformation(false);
            ArrayList<DataDTO> dataArrayList = db.getAllInformation();
            mView.setRecyclerView(dataArrayList);
        } else {
            ArrayList<DataDTO> levelArrayList = db.getLevelAInformation(levelType);
            if (levelArrayList.size() != 0) {
                mView.showSearchNoDataInformation(false);
                mView.setRecyclerView(levelArrayList);
            } else {
                mView.showSearchNoDataInformation(true);
                mView.setRecyclerView(levelArrayList);
            }

        }

    }

    @Override
    public void onWatchMoreClickListener() {
        mView.showAllInformation();
    }

    @Override
    public void onPrepareData() {
        mView.showProgressbar(false);
        mView.setRecyclerView(db.getAllInformation());
    }

    @Override
    public void onTopIconChange(int sid, String isShow, String topTime) {
        DataDTO dataDTO = db.getDataBySid(sid);
        dataDTO.setCheck(dataDTO.getCheck().equals("false") ? "true" : "false");
        //轉換毫秒
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.TAIWAN);
        try{
            if (topTime != null){
                long time = sdf.parse(topTime).getTime();
                dataDTO.setTime(time);
                Log.i("Michael", "修改過後的資料 check : " + dataDTO.getCheck() + " , 第 " + dataDTO.getSid() + " 筆資料");
                db.update(dataDTO);
                if (levelType == null || levelType.isEmpty()){
                    mView.setDataChange(db.getAllInformation(), isShow);
                }else {
                    mView.setDataChange(db.getLevelAInformation(levelType),isShow);
                }

            }else {
                dataDTO.setTime(0);
                Log.i("Michael", "修改過後的資料 check : " + dataDTO.getCheck() + " , 第 " + dataDTO.getSid() + " 筆資料");
                db.update(dataDTO);
                if (levelType == null || levelType.isEmpty()){
                    mView.setDataChange(db.getAllInformation(), isShow);
                }else {
                    mView.setDataChange(db.getLevelAInformation(levelType),isShow);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onShowDatePicker(int sid) {
        DataDTO dataDTO = db.getDataBySid(sid);

        if (dataDTO.getCheck().equals("false")) {
            mView.showDatePick(sid);
        } else {
            mView.deleteFavorite(sid, dataDTO);
        }


    }

    @Override
    public void onCreateDocumentInFirestore(int sid, String topTime) {
        DataDTO dataDTO = db.getDataBySid(sid);
        try{
            long time = new SimpleDateFormat("yyyy/MM/dd",Locale.TAIWAN).parse(topTime).getTime();
            mView.setFirestore(sid, time, dataDTO);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onLoginEvent() {
        mView.intentToLoginActivity();
    }

    @Override
    public void onSearchDbData(String email) {
        mView.showProgressbar(true);
        if (db.getAllInformation().size() != 0) {
            for (DataDTO dataDTO : db.getAllInformation()){
                DataDTO data = db.getDataBySid(dataDTO.getSid());
                data.setTime(0);
                data.setCheck("false");
                db.update(data);
            }
            mView.searchDataFromDb(email, db.getAllInformation());
        } else {
            Log.i("Michael", "db資料為 0");
        }
    }

    @Override
    public void onModifyDataFromFirestore(ArrayList<String> firestoreData, ArrayList<Long> timeArray, ArrayList<DataDTO> allInformation) {
        
        
        //在onCreate的時候 就修正完畢所有資料
        for (int i = 0; i < firestoreData.size(); i++) {
            for (DataDTO data : allInformation) {
                if (firestoreData.get(i).equals(data.getName())) {
                    DataDTO dataDTO = db.getDataBySid(data.getSid());
                    dataDTO.setCheck("true");
                    dataDTO.setTime(timeArray.get(i));
                    db.update(dataDTO);
                }
            }
        }
        mView.readyToProvideData();
    }

    @Override
    public void initDbData() {
        mView.showProgressbar(true);
        for (DataDTO dataDTO : db.getAllInformation()) {
            DataDTO data = db.getDataBySid(dataDTO.getSid());
            data.setCheck("false");
            data.setTime(0);
            db.update(data);
        }
        mView.readyToProvideData();
    }

    @Override
    public void onPrepareSpinnerData() {
        ArrayList<String> spinnerData = new ArrayList<>();
        spinnerData.add(mView.getVuContext().getString(R.string.no_sort));
        spinnerData.add(mView.getVuContext().getString(R.string.order_by_time));
        spinnerData.add(mView.getVuContext().getString(R.string.order_by_time_not_far));
        mView.setSpinner(spinnerData);
    }

    @Override
    public void onSpinnerSelectListener(int position) {
        Log.i("Michael","Spinner被選擇了");
        switch (position) {
            case NO_SORT:
                if (levelType == null || levelType.isEmpty() || levelType.equals("全部")){
                    mView.changeRecyclerViewSor(db.getAllInformation());
                }else {
                    mView.changeRecyclerViewSor(db.getLevelAInformation(levelType));
                }
                break;
            case ORDER_BY_NOT_FAR:
                break;
            case ORDER_BY_FAR:
                break;
            default:
                break;
        }
    }

    @Override
    public void onMountainItemClick(DataDTO data) {
        Log.i("Michael","點擊 : "+data.getName());


        mView.intentToMtDetailActivity(data);
    }
}
