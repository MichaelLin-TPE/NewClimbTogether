package com.hiking.climbtogether.chat_activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hiking.climbtogether.R;
import com.hiking.climbtogether.chat_activity.chat_view_presenter.ViewPresenter;

import static com.hiking.climbtogether.chat_activity.chat_view_presenter.ViewPresenterImpl.LEFT;
import static com.hiking.climbtogether.chat_activity.chat_view_presenter.ViewPresenterImpl.RIGHT;

public class ChatAdapter extends RecyclerView.Adapter {

    private Context context;


    private ViewPresenter viewPresenter;

    private ChatLeftViewHolder.OnUserPhotoClickListener listener;


    public void setOnUserPhotoClickListener(ChatLeftViewHolder.OnUserPhotoClickListener listener){
        this.listener = listener;
    }

    public ChatAdapter(Context context, ViewPresenter viewPresenter) {
        this.context = context;
        this.viewPresenter = viewPresenter;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        switch (viewType) {
            case LEFT:
                view = inflater.inflate(R.layout.chat_left_item_layout, parent, false);
                return new ChatLeftViewHolder(view,context);
            case RIGHT:
                view = inflater.inflate(R.layout.chat_right_item_layout, parent, false);
                return new ChatRightViewHolder(view);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //明天繼續做
        if (holder instanceof ChatRightViewHolder) {
            viewPresenter.onBindRightViewHolder((ChatRightViewHolder) holder, position);
        }
        if (holder instanceof ChatLeftViewHolder) {
            viewPresenter.onBindLeftViewHolder((ChatLeftViewHolder) holder, position);
            viewPresenter.setOnUserPhotoClickListener(listener,(ChatLeftViewHolder)holder);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return viewPresenter.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return viewPresenter.getItemCount();
    }

}
