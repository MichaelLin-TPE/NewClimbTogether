package com.example.climbtogether.mountain_collection_activity;

import android.util.Log;

import com.example.climbtogether.R;
import com.example.climbtogether.mountain_fragment.db_modle.DataBaseApi;
import com.example.climbtogether.mountain_fragment.db_modle.DataBaseImpl;
import com.example.climbtogether.mountain_fragment.db_modle.DataDTO;

import java.util.ArrayList;

import static com.example.climbtogether.mountain_collection_activity.view_presenter.MtPresenterImpl.LAND_VIEW;
import static com.example.climbtogether.mountain_collection_activity.view_presenter.MtPresenterImpl.PORT_VIEW;

public class MountainCollectionPresenterImpl implements MountainCollectionPresenter {

    private MountainCollectionVu mView;

    public MountainCollectionPresenterImpl(MountainCollectionVu mView) {
        this.mView = mView;
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
    public void onSearchDataSuccessFromFirebase(ArrayList<String> mtNameArray, ArrayList<Long> timeArray) {

        for (int i = 0; i < mtNameArray.size(); i++) {
            for (DataDTO data : getDatabase().getAllInformation()) {
                if (data.getName().equals(mtNameArray.get(i))) {
                    DataDTO dataDTO = getDatabase().getDataBySid(data.getSid());
                    dataDTO.setTime(timeArray.get(i));
                    dataDTO.setCheck("true");
                    getDatabase().update(dataDTO);
                    break;
                }
            }
        }

        ArrayList<DataDTO> dataArrayList = new ArrayList<>();

        for (String mtNmae : mtNameArray) {
            for (DataDTO data : getDatabase().getAllInformation()) {
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
        mView.setViewType(position);
    }

    private DataBaseApi getDatabase() {
        return new DataBaseImpl(mView.getVuContext());
    }
}
