package com.example.climbtogether.share_activity;

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

import com.example.climbtogether.R;
import com.example.climbtogether.tool.ImageLoaderManager;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Locale;

public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.ViewHolder> {
    private ArrayList<ShareArticleDTO> dataArrayList;

    private Context context;

    private onArticleItemClickListener listener;

    private ArrayList<LikeMemberDTO> likeMemberArray;

    private ImageLoaderManager imageLoaderManager;

    private String userEmail;

    private boolean isUserCheck;

    private int memberLikeIndex;

    private ArrayList<ReplyObject> replyArray;

    public void setonArticleItemClickListener(onArticleItemClickListener listener){
        this.listener = listener;
    }

    public ShareAdapter(ArrayList<ShareArticleDTO> dataArrayList,ArrayList<LikeMemberDTO> likeMemberArray,String userEmail,ArrayList<ReplyObject> replyArray, Context context) {
        this.dataArrayList = dataArrayList;
        this.context = context;
        this.likeMemberArray = likeMemberArray;
        this.replyArray = replyArray;
        this.userEmail = userEmail;
        imageLoaderManager = new ImageLoaderManager(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.share_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShareArticleDTO data = dataArrayList.get(position);
        holder.tvName.setText(data.getDiaplayName());
        holder.tvContentName.setText(data.getDiaplayName());
        holder.tvContent.setText(data.getContent());

        if (data.getEmail().equals(userEmail)){
            holder.ivSend.setVisibility(View.GONE);
        }

        ArrayList<String> downloadUrl = new ArrayList<>();
        if (!data.getSelectPhoto().isEmpty()){
            downloadUrl.add(data.getSelectPhoto());
        }
        if (!data.getSelectPhoto1().isEmpty()){
            downloadUrl.add(data.getSelectPhoto1());
        }
        if (!data.getSelectPhoto2().isEmpty()){
            downloadUrl.add(data.getSelectPhoto2());
        }
        ShareActivityViewPagerAdapter adapter = new ShareActivityViewPagerAdapter(context,downloadUrl);

        holder.viewPager.setAdapter(adapter);

        //大頭貼
        imageLoaderManager.setPhotoUrl(data.getUserPhoto(),holder.ivUserPhoto);


        //計算按讚有幾位
        if (data.getLike() > 0){
            holder.tvPeopleLike.setVisibility(View.VISIBLE);
            holder.tvPeopleLike.setText(String.format(Locale.getDefault(),"已有%d位按讚",data.getLike()));
        }else {
            holder.tvPeopleLike.setVisibility(View.GONE);
        }


        //判斷此帳號是否有按過
        LikeMemberDTO likeData = likeMemberArray.get(position);
        isUserCheck = false;
        for (int i = 0 ; i< likeData.getIsCheckArray().size() ; i++){
            if (likeData.getIsCheckArray().get(i)){
                memberLikeIndex = i;
                isUserCheck = true;
                break;
            }
        }

        holder.ivLike.setImageResource(isUserCheck ? R.drawable.like_pressed : R.drawable.like_not_press);


        holder.ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0 ; i< likeData.getIsCheckArray().size() ; i++){
                    if (likeData.getIsCheckArray().get(i)){
                        memberLikeIndex = i;
                        isUserCheck = true;
                        break;
                    }
                }
                listener.onAddLike(position,memberLikeIndex);
            }
        });




        ReplyObject replyObject = replyArray.get(position);

        Log.i("Michael","聊天內容有幾筆 : "+replyArray.size());

        int replyCount = replyObject.getReplyArray().size();
        if (replyCount > 0){
            holder.tvReplyCount.setVisibility(View.VISIBLE);
            holder.tvReplyCount.setText(String.format(Locale.getDefault(),"有%d筆留言...",replyCount));
        }else {
            holder.tvReplyCount.setVisibility(View.GONE);
        }

        holder.ivReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onReplyClick(replyObject,data,position);
            }
        });

        holder.tvReplyCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onReplyClick(replyObject,data,position);
            }
        });
        holder.ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSendClick(data);
            }
        });
        holder.ivUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("Michael","點擊item位置 : "+position+" , EMAIL : "+data.getEmail());

                listener.onUserClick(data);
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
        void onAddLike(int itemPosition,int memberIndex);

        void onSendClick(ShareArticleDTO data);

        void onSetting(ShareArticleDTO data,int itemPosition);

        void onUserClick(ShareArticleDTO data);

        void onReplyClick(ReplyObject replyObject,ShareArticleDTO shareArticleDTO,int itemPosition);
    }
}
