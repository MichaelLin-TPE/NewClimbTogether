package com.example.climbtogether.friend_manager_activity.view;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.climbtogether.R;
import com.example.climbtogether.friend_manager_activity.FriendDTO;
import com.example.climbtogether.friend_manager_activity.FriendInviteDTO;
import com.example.climbtogether.tool.ImageLoaderManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class FriendViewAdapter extends RecyclerView.Adapter<FriendViewAdapter.ViewHolder> {
    private Context context;

    private ArrayList<FriendDTO> friendArrayList;

    private StorageReference storage;

    private FirebaseFirestore firestore;

    private ImageLoaderManager loaderManager;

    private static final String FRIENDSHIP = "friendship";

    private static final String FRIEND = "friend";

    private FirebaseUser user;

    public FriendViewAdapter(Context context, ArrayList<FriendDTO> friendArrayList) {
        this.context = context;
        this.friendArrayList = friendArrayList;
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance().getReference();
        loaderManager = new ImageLoaderManager(context);
    }

    public void setCurrentUser(FirebaseUser user){
        this.user = user;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.friend_item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final FriendDTO data = friendArrayList.get(position);

        StorageReference river = storage.child(data.getEmail()+"/userPhoto/"+data.getEmail()+".jpg");
        river.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url = uri.toString();
                holder.ivUserPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
                loaderManager.setPhotoUrl(url,holder.ivUserPhoto);
                holder.tvEmai.setText(data.getEmail());
                holder.tvDisplayName.setText(data.getDisplayName());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                holder.ivUserPhoto.setScaleType(ImageView.ScaleType.FIT_CENTER);
                holder.ivUserPhoto.setImageResource(R.drawable.empty_photo);
                holder.tvEmai.setText(data.getEmail());
                holder.tvDisplayName.setText(data.getDisplayName());
            }
        });

    }


    @Override
    public int getItemCount() {
        return friendArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RoundedImageView ivUserPhoto;

        private TextView tvDisplayName,tvEmai;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUserPhoto = itemView.findViewById(R.id.friend_item_user_photo);
            tvDisplayName = itemView.findViewById(R.id.friend_item_display_name);
            tvEmai = itemView.findViewById(R.id.friend_item_email);


        }
    }
}
