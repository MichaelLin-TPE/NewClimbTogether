package com.hiking.climbtogether.personal_chat_photo_activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hiking.climbtogether.R;
import com.hiking.climbtogether.tool.NewImageLoaderManager;

import java.util.ArrayList;

public class PersonalPhotoAdapter extends RecyclerView.Adapter<PersonalPhotoAdapter.ViewHolder> {
    private Context context;

    private ArrayList<String> imageUrlArray;

    private OnPhotoClickListener listener;

    public void setOnPhotoClickListener(OnPhotoClickListener listener){
        this.listener = listener;
    }

    public PersonalPhotoAdapter(Context context, ArrayList<String> imageUrlArray) {
        this.context = context;
        this.imageUrlArray = imageUrlArray;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.photo_grid_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NewImageLoaderManager.getInstance(context).setPhotoUrl(imageUrlArray.get(position),holder.ivPhoto);

        holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(imageUrlArray.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageUrlArray == null ? 0 : imageUrlArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivPhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.grid_item_photo);
        }
    }

    public interface OnPhotoClickListener{
        void onClick(String url);
    }
}
