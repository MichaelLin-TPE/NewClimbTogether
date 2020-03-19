package com.hiking.climbtogether.mountain_collection_activity;

import android.util.Log;

import com.hiking.climbtogether.R;
import com.hiking.climbtogether.db_modle.DataBaseApi;
import com.hiking.climbtogether.db_modle.DataBaseImpl;
import com.hiking.climbtogether.db_modle.DataDTO;

import java.util.ArrayList;

public class MountainCollectionPresenterImpl implements MountainCollectionPresenter {

    private MountainCollectionVu mView;

    public static final int UPLOAD = 0;

    public static final int WATCH_INFORMATION = 1;

    public static final int SHARE_EXPERIENCE = 2;

    public static final int MODIFY_TIME = 3;

    public static final int REMOVE_DATA = 4;
    
    private DataBaseApi db;

    private int currentSid;

    public MountainCollectionPresenterImpl(MountainCollectionVu mView) {
        this.mView = mView;
        db = new DataBaseImpl(mView.getVuContext());
    }

    @Override
    public void onToolbarNavigationIconClickListener() {
        mView.closePage();
    }

    @Override
    public void onBackPressedListener() {
        mView.closePage();
    }

    @Override
    public void onUserExist() {
        mView.showProgressbar(true);
        mView.setViewMaintain(false);

        mView.searchDataFromFirebase();
    }

    @Override
    public void onUserNotExist() {
        mView.setViewMaintain(true);
    }

    @Override
    public void onBtnLoginClickListener() {
        mView.intentToLoginActivity();
    }

    @Override
    public void onSearchNoDataFromFirebase() {
        mView.showProgressbar(false);
        mView.showSearchNoDataView();
    }

    @Override
    public void onSearchDataSuccessFromFirebase(ArrayList<String> mtNameArray, ArrayList<Long> timeArray,ArrayList<String> downloadUrlArray) {

        for (int i = 0; i < mtNameArray.size(); i++) {
            for (DataDTO data : db.getAllInformation()) {
                if (data.getName().equals(mtNameArray.get(i))) {
                    DataDTO dataDTO = db.getDataBySid(data.getSid());
                    dataDTO.setTime(timeArray.get(i));
                    dataDTO.setCheck("true");
                    dataDTO.setUserPhoto(downloadUrlArray.get(i));
                    db.update(dataDTO);
                    break;
                }
            }
        }

        ArrayList<DataDTO> dataArrayList = new ArrayList<>();

        for (String mtNmae : mtNameArray) {
            for (DataDTO data : db.getAllInformation()) {
                if (data.getName().equals(mtNmae)) {
                    dataArrayList.add(data);
                }
            }
        }
        for (DataDTO dataDTO : dataArrayList) {
            Log.i("Michael", "有成功抓到山名 : " + dataDTO.getName());
        }
        mView.showProgressbar(false);

        mView.setRecyclerViewData(dataArrayList);
    }

    @Override
    public void onPrepareSpinnerData() {
        ArrayList<String> listArray = new ArrayList<>();
        listArray.add(mView.getVuContext().getString(R.string.port_view));
        listArray.add(mView.getVuContext().getString(R.string.land_view));
        mView.setSpinner(listArray);
    }

    @Override
    public void onSpinnerItemSelectedListener(int position) {
        mView.saveViewType(position);
        mView.setViewType(position);
    }

    @Override
    public void onItemClickListener(DataDTO dataDTO,int position) {
        mView.showItemAlertDialog(dataDTO,position);
    }

    @Override
    public void onItemDialogClickListener(int itemPosition,DataDTO dataDTO,int position) {
        currentSid = dataDTO.getSid();
        switch (itemPosition) {
            case UPLOAD:
                mView.selectPhoto(dataDTO.getName());
                break;
            case WATCH_INFORMATION:
                mView.intentToDetailActivity(dataDTO);
                break;
            case SHARE_EXPERIENCE:
                mView.intentToShareActivity();
                break;
            case MODIFY_TIME:
                mView.showDatePickerDialog(dataDTO);
                break;
            case REMOVE_DATA:
                DataDTO data = db.getDataBySid(dataDTO.getSid());
                data.setCheck("false");
                data.setUserPhoto("");
                data.setTime(0);
                db.update(data);
                mView.changeRecyclerView(position);
                mView.removeData(dataDTO);
                break;
            default:
                break;
        }
    }

    @Override
    public void onShowProgressToast(String message) {
        mView.showToast(message);
    }

    @Override
    public void onDatePickerDialogClickListener(DataDTO dataDTO, long pickTime) {
        DataDTO data = db.getDataBySid(dataDTO.getSid());
        data.setTime(pickTime);
        db.update(data);
        mView.modifyFirestoreData(data,pickTime);
    }

    @Override
    public void onShowSpinnerDialog(ArrayList<String> listArray) {
        mView.showSpinnerDialog(listArray);
    }

}
