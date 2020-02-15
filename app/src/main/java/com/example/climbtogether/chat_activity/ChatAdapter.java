package com.example.climbtogether.chat_activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.climbtogether.R;
import com.example.climbtogether.chat_activity.chat_view_presenter.ViewPresenter;
import com.example.climbtogether.chat_activity.chat_view_presenter.ViewPresenterImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.example.climbtogether.chat_activity.chat_view_presenter.ViewPresenterImpl.LEFT;
import static com.example.climbtogether.chat_activity.chat_view_presenter.ViewPresenterImpl.RIGHT;

public class ChatAdapter extends RecyclerView.Adapter {

    private Context context;


    private ViewPresenter viewPresenter;

    public ChatAdapter(Context context,ViewPresenter viewPresenter){
        this.context = context;
        this.viewPresenter = viewPresenter;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        switch (viewType){
            case LEFT:
                view = inflater.inflate(R.layout.chat_left_item_layout,parent,false);
                return new ChatLeftViewHolder(view);
            case RIGHT:
                view = inflater.inflate(R.layout.chat_right_item_layout,parent,false);
                return new ChatRightViewHolder(view);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //明天繼續做


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
