package com.hiking.climbtogether.friend_manager_activity.view;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hiking.climbtogether.R;
import com.hiking.climbtogether.friend_manager_activity.FriendInviteDTO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hiking.climbtogether.tool.NewImageLoaderManager;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class InviteViewAdapter extends RecyclerView.Adapter<InviteViewAdapter.ViewHolder> {

    private Context context;

    private ArrayList<FriendInviteDTO> inviteArrayList;

    private FirebaseFirestore firestore;

    private StorageReference storage;

    private OnCheckInviteIconClickListener listener;


    public void setOnCheckInviteIconClickListener(OnCheckInviteIconClickListener listener){
        this.listener = listener;
    }

    public InviteViewAdapter(Context context, ArrayList<FriendInviteDTO> inviteArrayList) {
        this.context = context;
        this.inviteArrayList = inviteArrayList;
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance().getReference();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.invite_item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final FriendInviteDTO data = inviteArrayList.get(position);
        holder.ivNo.setVisibility(View.GONE);
        holder.ivYes.setVisibility(View.GONE);
        StorageReference river = storage.child(data.getEmail()+"/userPhoto/"+data.getEmail()+".jpg");
        river.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String photoUrl = uri.toString();
                        holder.ivUserPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        NewImageLoaderManager.getInstance(context).setPhotoUrl(photoUrl,holder.ivUserPhoto);
                        searchForStrangerData(data.getEmail(),holder.tvDisplayName,holder.tvEmail,holder.ivYes,holder.ivNo,data,position);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                holder.ivUserPhoto.setScaleType(ImageView.ScaleType.FIT_CENTER);
                holder.ivUserPhoto.setImageResource(R.drawable.empty_photo);
                searchForStrangerData(data.getEmail(),holder.tvDisplayName,holder.tvEmail,holder.ivYes,holder.ivNo,data,position);
            }
        });
    }

    private void searchForStrangerData(String email, final TextView tvDisplayName, final TextView tvEmail, final ImageView ivYes, final ImageView ivNo, final FriendInviteDTO data, final int itemPostion) {
        firestore.collection("users").document(email).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            if (task.getResult() != null){
                                DocumentSnapshot snapshot = task.getResult();
                                tvDisplayName.setText((String)snapshot.get("displayName"));
                                tvEmail.setText((String)snapshot.get("email"));
                                ivNo.setVisibility(View.VISIBLE);
                                ivYes.setVisibility(View.VISIBLE);
                                ivYes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        listener.onClickYes(true,data,itemPostion);
                                    }
                                });
                                ivNo.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        listener.onClickNo(false,data,itemPostion);
                                    }
                                });
                            }

                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return inviteArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView ivUserPhoto;

        private TextView tvEmail,tvDisplayName;

        private ImageView ivYes,ivNo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivUserPhoto = itemView.findViewById(R.id.invite_item_user_photo);
            tvEmail = itemView.findViewById(R.id.invite_item_email);
            tvDisplayName = itemView.findViewById(R.id.invite_item_display_name);
            ivYes = itemView.findViewById(R.id.invite_item_check);
            ivNo = itemView.findViewById(R.id.invite_item_cancel);
        }
    }

    public interface OnCheckInviteIconClickListener{
        void onClickYes(boolean isAdd,FriendInviteDTO data,int itemPosition);

        void onClickNo(boolean isAdd,FriendInviteDTO data,int itemPosition);
    }
}
