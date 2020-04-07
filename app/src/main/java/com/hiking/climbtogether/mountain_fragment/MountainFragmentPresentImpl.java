package com.hiking.climbtogether.mountain_fragment;

import android.util.Log;

import com.hiking.climbtogether.R;
import com.hiking.climbtogether.db_modle.DataBaseApi;
import com.hiking.climbtogether.db_modle.DataBaseImpl;
import com.hiking.climbtogether.db_modle.DataDTO;
import com.hiking.climbtogether.tool.UserDataManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MountainFragmentPresentImpl implements MountainFragmentPresenter {

    private MountainFragmentVu mView;

    private static final int NO_SORT = 0;

    private static final int ORDER_BY_NOT_FAR = 1;

    private UserDataManager userDataManager;

    private ArrayList<DataDTO> dataArrayList;

    private DataBaseApi db;

    String levelType;

    public MountainFragmentPresentImpl(MountainFragmentVu mView) {
        this.mView = mView;
        db = new DataBaseImpl(mView.getVuContext());
        userDataManager = new UserDataManager(mView.getVuContext());
    }

    @Override
    public void onFilterTextViewBackgroundChangeListener(int textType, String levelType) {
        Log.i("Michael", "篩選了 : " + levelType);
        this.levelType = levelType;
        mView.showTextViewBackgroundChange(textType);

        if (userDataManager.getMountainSortType().isEmpty() || userDataManager.getMountainSortType().equals(mView.getVuContext().getString(R.string.no_sort))) {
            if (levelType.isEmpty() || levelType.equals(mView.getVuContext().getString(R.string.all))) {
                mView.showSearchNoDataInformation(false);
                dataArrayList = db.getAllInformation();
                mView.setRecyclerView(dataArrayList);
            } else {
                if (db.getLevelAInformation(levelType).size() != 0){
                    mView.showSearchNoDataInformation(false);
                }else {
                    mView.showSearchNoDataInformation(true);
                }
                dataArrayList = db.getLevelAInformation(levelType);
                mView.setRecyclerView(dataArrayList);
            }
        } else {
            if (levelType.isEmpty() || levelType.equals(mView.getVuContext().getString(R.string.all))) {
                mView.showSearchNoDataInformation(false);
                dataArrayList = db.getInformationOrderByTimeNotFar();
                mView.setRecyclerView(dataArrayList);
            } else {
                if (db.getInformationLevelOrderByTimeNotFar(levelType).size() != 0) {
                    mView.showSearchNoDataInformation(false);
                } else {
                    mView.showSearchNoDataInformation(true);
                }
                dataArrayList = db.getInformationLevelOrderByTimeNotFar(levelType);
                mView.setRecyclerView(dataArrayList);
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
        dataArrayList = db.getAllInformation();
        mView.setRecyclerView(dataArrayList);
    }

    @Override
    public void onTopIconChange(int sid, String isShow, String topTime, DataDTO data, int itemPosition) {
        DataDTO dataDTO = db.getDataBySid(sid);
        dataDTO.setCheck(dataDTO.getCheck().equals("false") ? "true" : "false");
        //轉換毫秒
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.TAIWAN);
        try {
            if (topTime != null) {
                long time = sdf.parse(topTime).getTime();
                dataDTO.setTime(time);
                Log.i("Michael", "修改過後的資料 check : " + dataDTO.getCheck() + " , 第 " + dataDTO.getSid() + " 筆資料");
                db.update(dataDTO);

                mView.setDataChange(dataArrayList, isShow);

                dataArrayList.get(itemPosition).setCheck(dataDTO.getCheck());
                dataArrayList.get(itemPosition).setTime(time);
                mView.setDataChange(dataArrayList,isShow);

            } else {
                dataDTO.setTime(0);
                Log.i("Michael", "修改過後的資料 check : " + dataDTO.getCheck() + " , 第 " + dataDTO.getSid() + " 筆資料");
                db.update(dataDTO);
                dataArrayList.get(itemPosition).setCheck(dataDTO.getCheck());
                dataArrayList.get(itemPosition).setTime(0);
                mView.setDataChange(dataArrayList,isShow);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onShowDatePicker(int sid, DataDTO data, int itemPosition) {
        if (data.getCheck().equals("false")) {
            mView.showDatePick(sid,data,itemPosition);
        } else {
            mView.deleteFavorite(sid, data,itemPosition);
        }


    }

    @Override
    public void onCreateDocumentInFirestore(int sid, String topTime) {
        DataDTO dataDTO = db.getDataBySid(sid);
        try {
            long time = new SimpleDateFormat("yyyy/MM/dd", Locale.TAIWAN).parse(topTime).getTime();
            mView.setFirestore(sid, time, dataDTO);
        } catch (Exception e) {
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
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (DataDTO dataDTO : db.getAllInformation()) {
                        if (dataDTO.getTime() != 0) {
                            DataDTO data = db.getDataBySid(dataDTO.getSid());
                            data.setTime(0);
                            data.setCheck("false");
                            db.update(data);
                        }
                    }
                    mView.searchDataFromDb(email, db.getAllInformation());
                }
            }).start();

        } else {
            Log.i("Michael", "db資料為 0");
        }
    }

    @Override
    public void onModifyDataFromFirestore(ArrayList<String> firestoreData, ArrayList<Long> timeArray, ArrayList<DataDTO> allInformation) {


        //在onCreate的時候 就修正完畢所有資料
        new Thread(new Runnable() {
            @Override
            public void run() {
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
                mView.showProgressbar(false);
                if (userDataManager.getMountainSortType().isEmpty() || userDataManager.getMountainSortType().equals(mView.getVuContext().getString(R.string.no_sort))) {
                    dataArrayList = db.getAllInformation();
                    mView.setRecyclerView(dataArrayList);
                } else {
                    if (levelType == null || levelType.isEmpty() || levelType.equals(mView.getVuContext().getString(R.string.all))) {
                        dataArrayList = db.getInformationOrderByTimeNotFar();
                        mView.setRecyclerView(dataArrayList);
                    } else {
                        dataArrayList = db.getInformationLevelOrderByTimeNotFar(levelType);
                        mView.setRecyclerView(dataArrayList);
                    }
                }

            }
        }).start();

    }

    @Override
    public void initDbData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mView.showProgressbar(true);
                for (DataDTO dataDTO : db.getAllInformation()) {
                    DataDTO data = db.getDataBySid(dataDTO.getSid());
                    data.setCheck("false");
                    data.setTime(0);
                    db.update(data);
                }
                mView.readyToProvideData();
            }
        }).start();
    }

    @Override
    public void onPrepareSpinnerData() {
        ArrayList<String> spinnerData = new ArrayList<>();
        spinnerData.add(mView.getVuContext().getString(R.string.no_sort));
        spinnerData.add(mView.getVuContext().getString(R.string.order_by_time));
        mView.setSpinner(spinnerData);
    }

    @Override
    public void onSpinnerSelectListener(int position) {
        Log.i("Michael", "Spinner被選擇了");
        mView.saveSortType(position);
        switch (position) {
            case NO_SORT:
                if (levelType == null || levelType.isEmpty() || levelType.equals("全部")) {
                    mView.changeRecyclerViewSor(db.getAllInformation());
                } else {
                    mView.changeRecyclerViewSor(db.getLevelAInformation(levelType));
                }
                break;
            case ORDER_BY_NOT_FAR:
                if (levelType == null || levelType.isEmpty() || levelType.equals("全部")) {
                    mView.changeRecyclerViewSor(db.getInformationOrderByTimeNotFar());
                } else {
                    mView.changeRecyclerViewSor(db.getInformationLevelOrderByTimeNotFar(levelType));
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onMountainItemClick(DataDTO data) {
        Log.i("Michael", "點擊 : " + data.getName());


        mView.intentToMtDetailActivity(data);
    }

    @Override
    public void onShowSpinnerDialog(ArrayList<String> spinnerData) {
        mView.showSpinnerDialog(spinnerData);
    }

    @Override
    public void onSearchMtListener(CharSequence searchContent) {
        if (userDataManager.getMountainSortType().isEmpty() ||
                userDataManager.getMountainSortType().equals(mView.getVuContext().getString(R.string.no_sort))){
            if (levelType == null || levelType.isEmpty() || levelType.equals(mView.getVuContext().getString(R.string.all))){

                if (db.searchAllInformation(searchContent.toString(),false).size() != 0){
                    mView.showSearchNoDataInformation(false);
                }else {
                    mView.showSearchNoDataInformation(true);
                }
                dataArrayList = db.searchAllInformation(searchContent.toString(),false);
                mView.setRecyclerView(dataArrayList);

            }else {
                if (db.searchAllLevelInformation(levelType,searchContent.toString(),false).size() != 0){
                    mView.showSearchNoDataInformation(false);
                }else {
                    mView.showSearchNoDataInformation(true);
                }
                dataArrayList = db.searchAllLevelInformation(levelType,searchContent.toString(),false);
                mView.setRecyclerView(dataArrayList);
            }
        }else {
            if (levelType == null || levelType.isEmpty() || levelType.equals(mView.getVuContext().getString(R.string.all))){

                if (db.searchAllInformation(searchContent.toString(),true).size() != 0){
                    mView.showSearchNoDataInformation(false);
                }else {
                    mView.showSearchNoDataInformation(true);
                }
                dataArrayList = db.searchAllInformation(searchContent.toString(),true);
                mView.setRecyclerView(dataArrayList);

            }else {
                if (db.searchAllLevelInformation(levelType,searchContent.toString(),true).size() != 0){
                    mView.showSearchNoDataInformation(false);
                }else {
                    mView.showSearchNoDataInformation(true);
                }
                dataArrayList = db.searchAllLevelInformation(levelType,searchContent.toString(),true);
                mView.setRecyclerView(dataArrayList);
            }
        }
    }
}
