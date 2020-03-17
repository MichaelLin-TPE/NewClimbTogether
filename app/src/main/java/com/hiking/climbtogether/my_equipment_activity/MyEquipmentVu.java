package com.hiking.climbtogether.my_equipment_activity;

import android.content.Context;

import com.hiking.climbtogether.db_modle.EquipmentListDTO;

import java.util.ArrayList;

public interface MyEquipmentVu {
    void closePage();

    Context getVuContext();

    void setViewMaintain( boolean isShow);

    void setRecyclerView(ArrayList<EquipmentListDTO> allMyEquipment, ArrayList<EquipmentListDTO> preparedArrayList);

    void showFriendListDialog();

    void searchFriend();

    void setFriendRecyclerView(ArrayList<FriendData> dataArrayList);

    void showNoFriendView(boolean isShow);

    void shareEquipmentListToFriend(String email);

    String getShareSuccessful();

    void showToast(String message);

    String getPleaseWait();

    void showDeleteDialog();

    void deleteAllList();

    void clearView();

    String getDeleteDataSuccess();

    void showConfirmDialog(String name, int itemPosition, String type);
}
