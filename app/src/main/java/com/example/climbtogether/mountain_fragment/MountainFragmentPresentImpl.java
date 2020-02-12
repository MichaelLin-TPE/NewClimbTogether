package com.example.climbtogether.mountain_fragment;

import android.util.Log;

import com.example.climbtogether.R;
import com.example.climbtogether.home_fragment.news_view.MountainNewsVu;
import com.example.climbtogether.mountain_fragment.db_modle.DataBaseApi;
import com.example.climbtogether.mountain_fragment.db_modle.DataBaseImpl;
import com.example.climbtogether.mountain_fragment.db_modle.DataDTO;

import java.util.ArrayList;

public class MountainFragmentPresentImpl implements MountainFragmentPresenter {

    private MountainFragmentVu mView;

    private ArrayList<DataDTO> allInformation;

    public MountainFragmentPresentImpl(MountainFragmentVu mView){
        this.mView = mView;
    }

    @Override
    public void onFilterTextViewBackgroundChangeListener(int textType,String levelType) {
        Log.i("Michael","篩選了 : "+levelType);
        mView.showTextViewBackgroundChange(textType);
        if (levelType.equals(mView.getVuContext().getResources().getString(R.string.all))){
            mView.showSearchNoDataInformation(false);
            ArrayList<DataDTO> dataArrayList = getDataBase().getAllInformation();
            mView.setRecyclerView(dataArrayList);
        }else {
            ArrayList<DataDTO> levelArrayList = getDataBase().getLevelAInformation(levelType);
            if (levelArrayList.size() != 0){
                mView.showSearchNoDataInformation(false);
                mView.setRecyclerView(levelArrayList);
            }else{
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
        mView.setRecyclerView(getDataBase().getAllInformation());
    }

    @Override
    public void onTopIconChange(int sid,String isShow,String topTime) {
        DataDTO dataDTO = getDataBase().getDataBySid(sid);
        dataDTO.setCheck(dataDTO.getCheck().equals("false") ? "true" : "false");
        dataDTO.setTime(topTime);
        Log.i("Michael","修改過後的資料 check : "+dataDTO.getCheck() + " , 第 "+dataDTO.getSid()+" 筆資料");
        getDataBase().update(dataDTO);
        mView.setDataChange(getDataBase().getAllInformation(),isShow);

    }

    @Override
    public void onShowDatePicker(int sid) {
        DataDTO dataDTO = getDataBase().getDataBySid(sid);

        if (dataDTO.getCheck().equals("false")){
            mView.showDatePick(sid);
        }else {
            mView.deleteFavorite(sid,dataDTO);
        }



    }

    @Override
    public void onCreateDocumentInFirestore(int sid, String topTime) {
        DataDTO dataDTO = getDataBase().getDataBySid(sid);
        mView.setFirestore(sid,topTime,dataDTO);
    }

    @Override
    public void onLoginEvent() {
        mView.intentToLoginActivity();
    }

    @Override
    public void onSearchDbData(String email) {
        mView.showProgressbar(true);
        if (getDataBase().getAllInformation().size() != 0){
            mView.searchDataFromDb(email,getDataBase().getAllInformation());
        }else {
            Log.i("Michael","db資料為 0");
        }
    }

    @Override
    public void onModifyDataFromFirestore(ArrayList<String> firestoreData,ArrayList<String> timeArray, ArrayList<DataDTO> allInformation) {
        //在onCreate的時候 就修正完畢所有資料
        for (int i = 0 ; i < firestoreData.size() ;i ++){
            for (DataDTO data : allInformation){
                if (firestoreData.get(i).equals(data.getName())){
                    DataDTO dataDTO = getDataBase().getDataBySid(data.getSid());
                    dataDTO.setCheck("true");
                    dataDTO.setTime(timeArray.get(i));
                    getDataBase().update(dataDTO);
                }
            }
        }
        mView.readyToProvideData();
    }

    @Override
    public void initDbData() {
        mView.showProgressbar(true);
        for (DataDTO dataDTO : getDataBase().getAllInformation()){
            DataDTO data = getDataBase().getDataBySid(dataDTO.getSid());
            data.setCheck("false");
            data.setTime(null);
            getDataBase().update(data);
        }
        mView.readyToProvideData();
    }

    @Override
    public void onPrepareSpinnerData() {
        ArrayList<String> spinnerData = new ArrayList<>();
        spinnerData.add(mView.getVuContext().getString(R.string.order_by_time));
        mView.setSpinner(spinnerData);
    }

    private DataBaseApi getDataBase(){
        return new DataBaseImpl(mView.getVuContext());
    }
}
