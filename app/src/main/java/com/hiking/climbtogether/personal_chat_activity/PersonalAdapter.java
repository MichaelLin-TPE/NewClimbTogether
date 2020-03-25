package com.hiking.climbtogether.personal_chat_activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hiking.climbtogether.R;
import com.hiking.climbtogether.personal_chat_activity.personal_presenter.PersonalPresenter;

import static com.hiking.climbtogether.personal_chat_activity.personal_presenter.PersonalPresenterImpl.LEFT;
import static com.hiking.climbtogether.personal_chat_activity.personal_presenter.PersonalPresenterImpl.RIGHT;

public class PersonalAdapter extends RecyclerView.Adapter {

    private Context context;

    private PersonalPresenter personalPresenter;

    private PersonalChatLeftViewHolder.OnPhotoClickListenr listener;

    public void setOnPhotoClickListenr(PersonalChatLeftViewHolder.OnPhotoClickListenr listener){
        this.listener = listener;
    }

    public PersonalAdapter(Context context, PersonalPresenter personalPresenter) {
        this.context = context;
        this.personalPresenter = personalPresenter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;

        switch (viewType) {
            case LEFT:
                view = inflater.inflate(R.layout.chat_left_item_layout, parent, false);
                return new PersonalChatLeftViewHolder(view, context);
            case RIGHT:
                view = inflater.inflate(R.layout.chat_right_item_layout, parent, false);
                return new PersonalChatRightViewHolder(view,context);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PersonalChatLeftViewHolder){
            personalPresenter.onBindLeftViewHolder((PersonalChatLeftViewHolder)holder,position);
            personalPresenter.setOnPhotoClickListenr((PersonalChatLeftViewHolder)holder,listener);
        }
        if (holder instanceof PersonalChatRightViewHolder){
            personalPresenter.onBindRightViewHolder((PersonalChatRightViewHolder)holder,position);
            personalPresenter.setOnPhotoClickListenrRight((PersonalChatRightViewHolder)holder,listener);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return personalPresenter.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return personalPresenter.getItemCount();
    }
}
