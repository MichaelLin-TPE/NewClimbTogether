package com.example.climbtogether.friend_manager_activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.climbtogether.R;
import com.example.climbtogether.friend_manager_activity.friend_presenter.FriendPresenter;
import com.example.climbtogether.friend_manager_activity.view.FriendViewAdapter;
import com.example.climbtogether.friend_manager_activity.view.FriendViewHolder;
import com.example.climbtogether.friend_manager_activity.view.InviteViewAdapter;
import com.example.climbtogether.friend_manager_activity.view.InviteViewHolder;

import static com.example.climbtogether.friend_manager_activity.friend_presenter.FriendPresenterImpl.FRIEND_INVITE;
import static com.example.climbtogether.friend_manager_activity.friend_presenter.FriendPresenterImpl.FRIEND_MANAGEMENT;

public class FriendManagerAdapter extends RecyclerView.Adapter {

    private FriendPresenter friendPresenter;

    private Context context;

    private FriendViewAdapter.OnfriendItemClickListener listener;

    private InviteViewAdapter.OnCheckInviteIconClickListener checkListener;

    public void setOnfriendItemClickListener(FriendViewAdapter.OnfriendItemClickListener listener){
        this.listener = listener;
    }

    public void setOnCheckInviteListener(InviteViewAdapter.OnCheckInviteIconClickListener listener){
        this.checkListener = listener;
    }

    public FriendManagerAdapter(FriendPresenter friendPresenter, Context context) {
        this.friendPresenter = friendPresenter;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (viewType){
            case FRIEND_INVITE:
                view = inflater.inflate(R.layout.invite_view_layout,parent,false);
                return new InviteViewHolder(view,context);
            case FRIEND_MANAGEMENT:
                view = inflater.inflate(R.layout.friend_view_layout,parent,false);
                return new FriendViewHolder(view,context);
            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return friendPresenter.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof InviteViewHolder){
            friendPresenter.onBindInviteViewHolder((InviteViewHolder)holder,position);
            friendPresenter.setOnCheckInviteListener((InviteViewHolder)holder,checkListener);

        }
        if (holder instanceof FriendViewHolder){
            friendPresenter.onBindFriendViewHolder((FriendViewHolder)holder,position);
            friendPresenter.setOnfriendItemClickListener(listener,(FriendViewHolder)holder);
        }
    }

    @Override
    public int getItemCount() {
        return friendPresenter.getItemCount();
    }
}
