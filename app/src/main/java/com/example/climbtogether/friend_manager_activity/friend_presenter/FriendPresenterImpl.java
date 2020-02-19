package com.example.climbtogether.friend_manager_activity.friend_presenter;

import android.util.Log;

import com.example.climbtogether.friend_manager_activity.FriendDTO;
import com.example.climbtogether.friend_manager_activity.FriendInviteDTO;
import com.example.climbtogether.friend_manager_activity.view.FriendViewAdapter;
import com.example.climbtogether.friend_manager_activity.view.FriendViewHolder;
import com.example.climbtogether.friend_manager_activity.view.InviteViewHolder;

import java.util.ArrayList;

public class FriendPresenterImpl implements FriendPresenter {

    public static final int FRIEND_INVITE = 0;

    public static final int FRIEND_MANAGEMENT = 1;

    private boolean isInvite;

    private boolean isFriendShow;
    private ArrayList<FriendInviteDTO> inviteArrayList;
    private ArrayList<FriendDTO> friendArrayList;


    public void setOnfriendItemClickListener (FriendViewAdapter.OnfriendItemClickListener listener ,FriendViewHolder holder){
        holder.setOnfriendItemClickListener(listener);
    }

    @Override
    public int getItemViewType(int position) {

        if (isInvite){
            if (position == 0){
                Log.i("Michael","做好友邀請");
                return FRIEND_INVITE;
            }
        }
        if (isFriendShow){
            if (position == 0){
                Log.i("Michael","做好友管理");
                return FRIEND_MANAGEMENT;
            }else if (position == 1){
                return FRIEND_MANAGEMENT;
            }

        }
        return 0;
    }

    @Override
    public int getItemCount() {
        int itemCount = 0;
        if (isInvite){
            itemCount ++;
        }
        if (isFriendShow){
            itemCount ++;
        }
        Log.i("Michael","item 數量 : "+itemCount);
        return itemCount;
    }

    @Override
    public void setData(ArrayList<FriendInviteDTO> inviteArrayList, ArrayList<FriendDTO> friendArrayList) {
        this.inviteArrayList = inviteArrayList;
        this.friendArrayList = friendArrayList;

        isInvite = inviteArrayList.size() != 0;

        isFriendShow = friendArrayList.size() != 0;

        Log.i("Michael","邀請 : "+isInvite + " , 好友 : "+isFriendShow);

    }

    @Override
    public void onBindInviteViewHolder(InviteViewHolder holder, int position) {
        holder.setData(inviteArrayList);
    }

    @Override
    public void onBindFriendViewHolder(FriendViewHolder holder, int position) {
        holder.setData(friendArrayList);
    }

}
