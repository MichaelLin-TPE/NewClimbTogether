package com.hiking.climbtogether.my_equipment_activity;

import com.hiking.climbtogether.db_modle.EquipmentListDTO;

import java.util.ArrayList;

public class MyEquipmentPresenterImpl implements MyEquipmentPresenter {

    private MyEquipmentVu mView;


    public MyEquipmentPresenterImpl(MyEquipmentVu mView) {
        this.mView = mView;

    }

    @Override
    public void onBackButtonClick() {
        mView.closePage();
    }

    @Override
    public void onCatchDataSuccessful(ArrayList<EquipmentListDTO> notPrepareArrayList, ArrayList<EquipmentListDTO> preparedArrayList) {
        mView.setRecyclerView(notPrepareArrayList,preparedArrayList);
    }

    @Override
    public void onViewMaintain() {
        mView.setViewMaintain(true);
    }

    @Override
    public void onShareButtonClick() {
        mView.showFriendListDialog();
    }

    @Override
    public void onSearchFriendData() {
        mView.searchFriend();
    }

    @Override
    public void onCatchFriendDataSuccessful(ArrayList<FriendData> dataArrayList) {
        mView.showNoFriendView(false);
        mView.setFriendRecyclerView(dataArrayList);
    }

    @Override
    public void onHasNoFriendEvent() {
        mView.showNoFriendView(true);
    }

    @Override
    public void onFriendItemClickListener(String email) {
        String message = mView.getPleaseWait();
        mView.showToast(message);
        mView.shareEquipmentListToFriend(email);
    }

    @Override
    public void onShareSuccessful() {
        String message = mView.getShareSuccessful();
        mView.showToast(message);
    }

    @Override
    public void onDeleteButtonClick() {
        mView.showDeleteDialog();
    }

    @Override
    public void onDeleteAllListConfirmClick() {
        mView.clearView();
        mView.deleteAllList();
        String message = mView.getDeleteDataSuccess();
        mView.showToast(message);
    }


}
