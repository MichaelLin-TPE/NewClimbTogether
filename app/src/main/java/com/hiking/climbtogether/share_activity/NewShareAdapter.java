package com.hiking.climbtogether.share_activity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.hiking.climbtogether.R;
import com.hiking.climbtogether.share_activity.share_json.ShareArticleJson;
import com.hiking.climbtogether.share_activity.share_json.ShareClickLikeObject;
import com.hiking.climbtogether.tool.NewImageLoaderManager;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Locale;

public class NewShareAdapter extends RecyclerView.Adapter<NewShareAdapter.ViewHolder> {
    private ArrayList<ShareArticleJson> dataArrayList;

    private Context context;

    private onArticleItemClickListener listener;

    private String userEmail;

    private boolean isChecked;

    private int clickIndex;

    public void setonArticleItemClickListener(onArticleItemClickListener listener){
        this.listener = listener;
    }

    public NewShareAdapter(ArrayList<ShareArticleJson> dataArrayList, Context context,String userEmail) {
        this.dataArrayList = dataArrayList;
        this.context = context;
        this.userEmail = userEmail;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.share_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShareArticleJson data = dataArrayList.get(position);
        Log.i("Michael","adapter content "+data.getContent());
        holder.tvContent.setText(data.getContent());

        holder.tvContentName.setText(data.getDisplayName());
        holder.tvName.setText(data.getDisplayName());

        if (data.getEmail().equals(userEmail)){

            holder.ivSend.setVisibility(View.GONE);

        }else {

            holder.ivSend.setVisibility(View.VISIBLE);

        }



        if (data.getLike() == 0){
            holder.tvPeopleLike.setVisibility(View.GONE);
        }else {
            holder.tvPeopleLike.setVisibility(View.VISIBLE);
            holder.tvPeopleLike.setText(String.format(Locale.getDefault(),"%d位按讚",data.getLike()));
        }

        if (data.getReply() == 0){
            holder.tvReplyCount.setVisibility(View.GONE);
        }else {
            holder.tvReplyCount.setVisibility(View.VISIBLE);
            holder.tvReplyCount.setText(String.format(Locale.getDefault(),"%d筆留言",data.getReply()));
        }
        NewImageLoaderManager.getInstance(context).setPhotoUrl(data.getUserPhoto(),holder.ivUserPhoto);


        NewShareActivityViewPagerAdapter adapter = new NewShareActivityViewPagerAdapter(context,data.getSharePhoto());

        holder.viewPager.setAdapter(adapter);

        if (data.getClick_member() != null && data.getClick_member().size() != 0){
            for (ShareClickLikeObject like : data.getClick_member()){
                if (userEmail.equals(like.getMemberEmail())){
                    holder.ivLike.setImageResource(R.drawable.like_pressed);
                    break;
                }
            }
        }else {
            holder.ivLike.setImageResource(R.drawable.like_not_press);
        }
        //明天再用點擊事件
        isChecked = false;
        clickIndex = 0;
        holder.ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data.getClick_member() != null && data.getClick_member().size() != 0){

                    for (int i = 0 ; i < data.getClick_member().size() ; i++){
                        if (data.getClick_member().get(i).getMemberEmail().equals(userEmail)){
                            isChecked = true;
                            clickIndex = i;
                            break;
                        }
                    }

                    if (isChecked){
                        holder.ivLike.setImageResource(R.drawable.like_not_press);
                    }else {
                        holder.ivLike.setImageResource(R.drawable.like_pressed);
                    }
                }else {
                    holder.ivLike.setImageResource(R.drawable.like_pressed);
                }
                listener.onAddLike(position,isChecked,clickIndex);

            }
        });

        holder.tvReplyCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onReplyClick(data,position);
            }
        });

        holder.ivUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!data.getEmail().equals(userEmail)){
                    listener.onUserClick(data);
                }
            }
        });

        holder.ivReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onReplyClick(data,position);
            }
        });

        holder.ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSendClick(data);
            }
        });

        holder.ivSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSetting(data,position);
            }
        });


    }

    @Override
    public int getItemCount() {

        return dataArrayList == null ? 0 : dataArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName,tvContent,tvContentName,tvPeopleLike,tvReplyCount;

        private RoundedImageView ivUserPhoto;

        private ImageView ivLike,ivSend,ivSettings,ivReply;

        private ViewPager viewPager;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewPager = itemView.findViewById(R.id.share_item_view_pager);
            tvName = itemView.findViewById(R.id.share_item_user_displayName);
            tvContent = itemView.findViewById(R.id.share_item_content);
            tvContentName = itemView.findViewById(R.id.share_item_content_displayName);
            ivUserPhoto = itemView.findViewById(R.id.share_item_user_photo);
            ivLike = itemView.findViewById(R.id.share_item_like);
            ivSend = itemView.findViewById(R.id.share_item_send);
            ivSettings = itemView.findViewById(R.id.share_item_settings);
            tvPeopleLike = itemView.findViewById(R.id.share_item_like_people);
            ivReply = itemView.findViewById(R.id.share_item_reply);
            tvReplyCount = itemView.findViewById(R.id.share_item_reply_content);
        }
    }


    public interface onArticleItemClickListener{
        void onAddLike(int itemPosition, boolean isChecked,int clickIndex);

        void onSendClick(ShareArticleJson data);

        void onSetting(ShareArticleJson data, int itemPosition);

        void onUserClick(ShareArticleJson data);

        void onReplyClick(ShareArticleJson shareArticleDTO, int itemPosition);
    }
}
