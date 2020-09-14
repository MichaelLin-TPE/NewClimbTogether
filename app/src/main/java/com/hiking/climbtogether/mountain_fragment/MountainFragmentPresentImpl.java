package com.hiking.climbtogether.mountain_fragment;

import android.util.Log;

import com.google.gson.Gson;
import com.hiking.climbtogether.R;
import com.hiking.climbtogether.db_modle.DataBaseApi;
import com.hiking.climbtogether.db_modle.DataBaseImpl;
import com.hiking.climbtogether.db_modle.DataDTO;
import com.hiking.climbtogether.tool.DataProvider;
import com.hiking.climbtogether.tool.FirebaseHandler;
import com.hiking.climbtogether.tool.FirebaseHandlerImpl;
import com.hiking.climbtogether.tool.SortClass;
import com.hiking.climbtogether.tool.UserDataManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class MountainFragmentPresentImpl implements MountainFragmentPresenter {

    private MountainFragmentVu mView;

    private static final int NO_SORT = 0;

    private static final int ORDER_BY_NOT_FAR = 1;

    private UserDataManager userDataManager;

    private ArrayList<DataDTO> dataArrayList,mtDataArray;

    private DataBaseApi db;

    String levelType;

    private SimpleDateFormat sdf;

    private FirebaseHandler firebaseHandler;

    public MountainFragmentPresentImpl(MountainFragmentVu mView) {
        this.mView = mView;
        db = new DataBaseImpl(mView.getVuContext());
        userDataManager = new UserDataManager(mView.getVuContext());
        firebaseHandler = new FirebaseHandlerImpl();
        sdf = new SimpleDateFormat("yyyy/MM/dd",Locale.TAIWAN);
    }

    @Override
    public void onFilterTextViewBackgroundChangeListener(int textType, String levelType) {
        Log.i("Michael", "篩選了 : " + levelType);
        this.levelType = levelType;
        mView.showTextViewBackgroundChange(textType);

        changeViewForLevelType();




    }

    private void changeViewForLevelType() {
        if (levelType == null || levelType.isEmpty() || levelType.equals("全部")) {
            mView.showSearchNoDataInformation(false);
            boolean isTimeSort = mView.getSortType().equals("日期排序");
            if (isTimeSort){
                Collections.sort(dataArrayList,new SortClass());
            }
            mView.setRecyclerView(dataArrayList);
            return;
        }

        mtDataArray = new ArrayList<>();

        for (DataDTO dataDTO : dataArrayList) {
            Log.i("Michael","山的難度 : "+dataDTO.getDifficulty());
            if (dataDTO.getDifficulty().equals(levelType)) {
                mtDataArray.add(dataDTO);
            }
        }
        if (mtDataArray.isEmpty()) {
            mView.showSearchNoDataInformation(true);
            mView.setRecyclerView(mtDataArray);
            return;
        }
        mView.showSearchNoDataInformation(false);
        boolean isTimeSort = mView.getSortType().equals("日期排序");
        if (isTimeSort){
            Collections.sort(mtDataArray,new SortClass());
        }
        mView.setRecyclerView(mtDataArray);
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
    public void onTopIconChange(String isShow, String topTime, DataDTO data, int itemPosition) {
        //轉換毫秒
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.TAIWAN);
        try {

            long time = sdf.parse(topTime).getTime();
            dataArrayList.get(itemPosition).setCheck("true");
            dataArrayList.get(itemPosition).setTime(time);
            mView.updateRecyclerView(dataArrayList);
            firebaseHandler.onSetTopMountainData(time, data);

        } catch (Exception e) {
            e.printStackTrace();
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
        mView.showProgressbar(false);
        if (userDataManager.getMountainSortType().isEmpty() || userDataManager.getMountainSortType().equals(mView.getVuContext().getString(R.string.no_sort))) {
            dataArrayList = db.getAllInformation();
            mView.setRecyclerView(dataArrayList);
            return;
        }
        if (levelType == null || levelType.isEmpty() || levelType.equals(mView.getVuContext().getString(R.string.all))) {
            dataArrayList = db.getInformationOrderByTimeNotFar();
        } else {
            dataArrayList = db.getInformationLevelOrderByTimeNotFar(levelType);
        }
        mView.setRecyclerView(dataArrayList);
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

//        String json = new Gson().toJson(db.getAllInformation());
//        Log.i("Michael","產出的JSON : "+json);
//
//        try{
//            File writeName = mView.getVuContext().getDatabasePath("json.txt");
//            writeName.createNewFile();
//            BufferedWriter out = new BufferedWriter(new FileWriter(writeName));
//            out.write(json);
//            out.flush();
//            out.close();
//        }catch (Exception e){
//            e.printStackTrace();
//        }


        mView.readyToProvideData();

    }

    @Override
    public void onPrepareSpinnerData() {
        ArrayList<String> spinnerData = DataProvider.getInstance().getSpinnerDataArray();
        mView.setSpinner(spinnerData);
    }

    @Override
    public void onSpinnerSelectListener(int position) {
        Log.i("Michael", "Spinner被選擇了");
        mView.saveSortType(position);



        switch (position) {
            case NO_SORT:
                firebaseHandler.onGetMountainApi(onReCatchApiDataListener);
                break;
            case ORDER_BY_NOT_FAR:
                SortClass sortClass = new SortClass();
                if (mtDataArray == null || mtDataArray.isEmpty()){


                    Collections.sort(dataArrayList,sortClass);
                    mView.updateRecyclerView(dataArrayList);

                    return;
                }

                Collections.sort(mtDataArray,sortClass);
                mView.updateRecyclerView(mtDataArray);
                break;
            default:
                break;
        }
    }

    private FirebaseHandler.OnConnectFireStoreListener<ArrayList<DataDTO>> onReCatchApiDataListener = new FirebaseHandler.OnConnectFireStoreListener<ArrayList<DataDTO>>() {
        @Override
        public void onSuccess(ArrayList<DataDTO> data) {
            dataArrayList = data;
            changeViewForLevelType();
        }

        @Override
        public void onFail(String errorCode) {
            mView.showErrorCode(errorCode);
        }
    };

    private boolean compareTime(long firstTime, long secondTime) {
        Date firstDate = new Date(firstTime);
        Date secondDate = new Date(secondTime);
        return !firstDate.before(secondDate);
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

        String searchText = searchContent.toString();

        Log.i("Michael","我搜尋了 : "+searchText);

        if (searchText.isEmpty()){
            mView.showSearchNoDataInformation(false);
            mView.updateRecyclerView(dataArrayList);
            return;
        }
        mtDataArray = new ArrayList<>();
        for (DataDTO dataDTO : dataArrayList){
            if (dataDTO.getName().contains(searchText)){
                mtDataArray.add(dataDTO);
            }
        }
        if (mtDataArray.isEmpty()){
            mView.showSearchNoDataInformation(true);
            mView.updateRecyclerView(mtDataArray);
            return;
        }
        mView.showSearchNoDataInformation(false);
        mView.updateRecyclerView(mtDataArray);
    }

    @Override
    public void onShowErrorCode(String errorCode) {
        mView.showErrorCode(errorCode);
    }

    //新的寫法

    @Override
    public void onActivityCreated() {
        mView.showProgressbar(true);
        firebaseHandler.onGetMountainApi(onCatchMountainApiListener);
    }

    @Override
    public void onMtListItemIconClickListener(DataDTO data, int itemPosition) {
        if (firebaseHandler.isLogin()) {

            if (data.getCheck().equals("true")) {
                dataArrayList.get(itemPosition).setCheck("false");
                dataArrayList.get(itemPosition).setTime(0);
                mView.updateRecyclerView(dataArrayList);
                firebaseHandler.onDeleteTopDocument(dataArrayList.get(itemPosition).getName());
                return;
            }
            mView.showDatePick(data, itemPosition);
            return;
        }
        mView.intentToLoginActivity();
    }


    private FirebaseHandler.OnConnectFireStoreListener<ArrayList<DataDTO>> onCatchMountainApiListener = new FirebaseHandler.OnConnectFireStoreListener<ArrayList<DataDTO>>() {
        @Override
        public void onSuccess(ArrayList<DataDTO> mtDataArray) {
            dataArrayList = mtDataArray;

            if (mView.getSortType().equals("日期排序")){
                SortClass sortClass = new SortClass();
                Collections.sort(dataArrayList,sortClass);
            }
            mView.setRecyclerView(mtDataArray);
            mView.showProgressbar(false);
        }

        @Override
        public void onFail(String errorCode) {
            Log.i("Michael", "取得資料失敗 : " + errorCode);
            mView.showProgressbar(false);
            mView.showErrorCode(errorCode);
        }
    };


}
