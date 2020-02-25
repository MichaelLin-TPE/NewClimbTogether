package com.example.climbtogether.share_activity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.climbtogether.R;
import com.example.climbtogether.tool.ImageLoaderManager;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class ReplyDialogAdapter extends RecyclerView.Adapter<ReplyDialogAdapter.ViewHolder> {

    private ArrayList<ReplyDTO> dataArrayList;

    private Context context;

    private ImageLoaderManager imageLoaderManager;

    public ReplyDialogAdapter(ArrayList<ReplyDTO> dataArrayList, Context context) {
        this.dataArrayList = dataArrayList;
        this.context = context;
        imageLoaderManager = new ImageLoaderManager(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.reply_dialog_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReplyDTO data = dataArrayList.get(position);
        imageLoaderManager.setPhotoUrl(data.getUserPhoto(),holder.ivUserPhoto);
        holder.tvName.setText(data.getUserName());
        Log.i("Michael","留言 : "+data.getContent());
        holder.tvContent.setText(data.getContent());
    }

    @Override
    public int getItemCount() {
        Log.i("Michael","reply長度 : "+dataArrayList.size());
        return dataArrayList == null ? 0 : dataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RoundedImageView ivUserPhoto;

        private TextView tvName,tvContent;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUserPhoto = itemView.findViewById(R.id.reply_item_user_photo);
            tvName = itemView.findViewById(R.id.reply_item_user_displayName);
            tvContent = itemView.findViewById(R.id.reply_item_content);
        }
    }
}
