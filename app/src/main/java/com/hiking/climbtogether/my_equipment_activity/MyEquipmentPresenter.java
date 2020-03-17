package com.hiking.climbtogether.my_equipment_activity;

import com.hiking.climbtogether.db_modle.EquipmentListDTO;

import java.util.ArrayList;

public interface MyEquipmentPresenter {
    void onBackButtonClick();

    void onCatchDataSuccessful(ArrayList<EquipmentListDTO> dataArrayList, ArrayList<EquipmentListDTO> preparedArrayList);

    void onViewMaintain();

    void onShareButtonClick();

    void onSearchFriendData();

    void onCatchFriendDataSuccessful(ArrayList<FriendData> dataArrayList);

    void onHasNoFriendEvent();

    void onFriendItemClickListener(String email);

    void onShareSuccessful();

    void onDeleteButtonClick();

    void onDeleteAllListConfirmClick();

    void onDeleteLongClick(String name, int itemPosition, String type);
}
