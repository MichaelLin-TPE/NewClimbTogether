package com.example.climbtogether.friend_manager_activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.climbtogether.R;
import com.example.climbtogether.friend_manager_activity.friend_presenter.FriendPresenter;
import com.example.climbtogether.friend_manager_activity.friend_presenter.FriendPresenterImpl;
import com.example.climbtogether.friend_manager_activity.view.FriendViewAdapter;
import com.example.climbtogether.friend_manager_activity.view.InviteViewAdapter;
import com.example.climbtogether.personal_chat_activity.PersonalChatActivity;
import com.example.climbtogether.tool.ImageLoaderManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Map;

public class FriendManagerActivity extends AppCompatActivity implements FriendManagerVu {

    private FriendManagerPresenter presenter;

    private Toolbar toolbar;

    private RecyclerView recyclerView;

    private FriendPresenter friendPresenter;

    private FirebaseAuth mAuth;

    private FirebaseUser user;

    private StorageReference storage;

    private FirebaseFirestore firestore;

    private static final String FRIENDSHIP = "friendship";

    private static final String FRIEND = "friend";

    private static final String INVITE_FRIEND = "invite_friend";

    private static final String INVITE = "invite";

    private ArrayList<FriendInviteDTO> inviteArrayList;

    private ArrayList<FriendDTO> friendArrayList;

    private FriendManagerAdapter adapter;

    private ImageLoaderManager imageLoaderManager;

    private String friendPhotoUrl;

    private static final String PERSONAL_CHAT = "personal_chat";

    private static final String CHAT_DATA = "chat_data";

    private AlertDialog userDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_manager);
        imageLoaderManager = new ImageLoaderManager(this);
        initPresenter();
        initFirebase();
        initView();
        checkAllData();
    }

    /**
     * 先檢查邀清
     * 在檢查好友,如果有好友的話,列出列表並下載好友頭貼
     */

    private void checkAllData() {
        if (user != null){
            if (user.getEmail() != null){
                firestore.collection(INVITE_FRIEND).document(user.getEmail()).collection(INVITE)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    inviteArrayList = new ArrayList<>();
                                    if (task.getResult() != null){
                                        for (QueryDocumentSnapshot document : task.getResult()){
                                            FriendInviteDTO friendInviteDTO = new FriendInviteDTO();
                                            friendInviteDTO.setAnswer((String)document.getData().get("answer"));
                                            friendInviteDTO.setEmail(document.getId());
                                            inviteArrayList.add(friendInviteDTO);
                                        }
                                        if (inviteArrayList.size() != 0){
                                            Log.i("Michael","有邀情");
                                        }else {
                                            Log.i("Michael","沒邀請");
                                        }

                                        searchForFriend(user.getEmail());
                                    }
                                }
                            }
                        });
            }
        }
    }

    private void searchForFriend(String email) {
        firestore.collection(FRIENDSHIP).document(email).collection(FRIEND)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            if (task.getResult() != null){
                                friendArrayList = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()){
                                    FriendDTO friendDTO = new FriendDTO();
                                    friendDTO.setDisplayName((String)document.getData().get("displayName"));
                                    friendDTO.setEmail((String)document.getData().get("email"));
                                    friendArrayList.add(friendDTO);
                                }
                                if (friendArrayList.size() != 0){
                                    Log.i("Michael","有朋友");
                                }else {
                                    Log.i("Michael","沒朋友");
                                }

                                presenter.onCatchDataSuccessful(inviteArrayList,friendArrayList);

                            }
                        }
                    }
                });
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();
    }

    private void initView() {
        toolbar = findViewById(R.id.friend_manager_toolbar);
        recyclerView = findViewById(R.id.friend_manager_recycler_view);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onBackIconClickLiistener();
            }
        });
    }

    private void initPresenter() {
        presenter = new FriendManagerPresenterImpl(this);
        friendPresenter = new FriendPresenterImpl();
    }

    @Override
    public void closePage() {
        finish();
    }

    @Override
    public void setRecyclerView(ArrayList<FriendInviteDTO> inviteArrayList, ArrayList<FriendDTO> friendArrayList) {

        friendPresenter.setData(inviteArrayList,friendArrayList);

        adapter = new FriendManagerAdapter(friendPresenter,this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        recyclerView.setAdapter(adapter);

        adapter.setOnfriendItemClickListener(new FriendViewAdapter.OnfriendItemClickListener() {
            @Override
            public void onClick(FriendDTO data, int itemPosition) {
                presenter.onFriendItemClickListener(data,itemPosition);
            }
        });

        adapter.setOnCheckInviteListener(new InviteViewAdapter.OnCheckInviteIconClickListener() {
            @Override
            public void onClickYes(boolean isAdd, FriendInviteDTO data, int itemPosition) {
                deleteInviteData(data, itemPosition);
                createFriendData(data);
            }

            @Override
            public void onClickNo(boolean isAdd, FriendInviteDTO data, int itemPosition) {
                deleteInviteData(data, itemPosition);
            }
        });

    }

    private void createFriendData(final FriendInviteDTO data) {
        firestore.collection("users").document(data.getEmail()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null) {
                                DocumentSnapshot document = task.getResult();
                                Map<String, Object> map = document.getData();
                                if (user != null) {
                                    if (user.getEmail() != null) {
                                        if (map != null) {
                                            becomeToFriend(user.getEmail(),data.getEmail(),map);
                                        }
                                    }
                                }

                            }
                        }
                    }
                });
    }

    private void becomeToFriend(final String userEmail, final String friendEmail, Map<String,Object> map) {
        firestore.collection(FRIENDSHIP).document(userEmail)
                .collection(FRIEND).document(friendEmail)
                .set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            createUserData(userEmail,friendEmail);
                        }
                    }
                });
    }

    private void createUserData(final String userEmail, final String friendEmail) {
        firestore.collection("users").document(userEmail).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null) {
                                DocumentSnapshot document = task.getResult();
                                Map<String, Object> map = document.getData();
                                if (user != null) {
                                    if (user.getEmail() != null) {
                                        if (map != null) {
                                            becomeToNewFriend(userEmail,friendEmail,map);
                                        }
                                    }
                                }

                            }
                        }
                    }
                });
    }

    private void becomeToNewFriend(String friendEmail, String userEmail, Map<String, Object> map) {
        firestore.collection(FRIENDSHIP).document(userEmail)
                .collection(FRIEND).document(friendEmail)
                .set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.i("Michael", "新增成功");
                            inviteArrayList = new ArrayList<>();
                            friendArrayList = new ArrayList<>();
                            friendPresenter.setData(inviteArrayList,friendArrayList);
                            adapter = new FriendManagerAdapter(friendPresenter,FriendManagerActivity.this);
                            recyclerView.setAdapter(adapter);
                            checkAllData();
                        }
                    }
                });
    }

    private void deleteInviteData(FriendInviteDTO data, final int itemPosition) {
        if (user != null) {
            if (user.getEmail() != null) {
                firestore.collection(INVITE_FRIEND).document(user.getEmail()).collection(INVITE).document(data.getEmail())
                        .delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.i("Michael", "已刪除");
                                    inviteArrayList.remove(itemPosition);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        });
            }
        } else {
            Log.i("Michael", "user null");
        }
    }



    @Override
    public void showUserDialog(final FriendDTO data, final int itemPosition) {
        final View view = View.inflate(this,R.layout.friend_managemnet_dialog_custom_view,null);
        final RoundedImageView ivUserPhoto = view.findViewById(R.id.friend_dialog_photo);
        final TextView tvName = view.findViewById(R.id.friend_dialog_display_name);
        final TextView tvEmail = view.findViewById(R.id.friend_dialog_email);
        LinearLayout chatClickArea = view.findViewById(R.id.friend_dialog_chat_clickArea);
        LinearLayout deleteClickArea = view.findViewById(R.id.friend_dialog_delete_clickArea);

        StorageReference river = storage.child(data.getEmail()+"/userPhoto/"+data.getEmail()+".jpg");
        river.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        friendPhotoUrl = uri.toString();
                        ivUserPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imageLoaderManager.setPhotoUrl(friendPhotoUrl,ivUserPhoto);
                        tvEmail.setText(data.getEmail());
                        tvName.setText(data.getDisplayName());
                        showView(view);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                friendPhotoUrl = "";
                ivUserPhoto.setScaleType(ImageView.ScaleType.FIT_CENTER);
                ivUserPhoto.setImageResource(R.drawable.empty_photo);
                tvEmail.setText(data.getEmail());
                tvName.setText(data.getDisplayName());
                showView(view);
            }
        });

        chatClickArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onChatButtonClickListener(data.getEmail(),data.getDisplayName(),friendPhotoUrl);
            }
        });
        deleteClickArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onDeleteButtonClickListener(data.getEmail(),itemPosition);
            }
        });
    }

    @Override
    public void intentToPersonalChatActivity(String email, String displayName, String friendPhotoUrl) {
        Intent it = new Intent(this, PersonalChatActivity.class);
        it.putExtra("displayName",displayName);
        it.putExtra("mail",email);
        it.putExtra("photoUrl",friendPhotoUrl);
        startActivity(it);
        finish();
    }

    @Override
    public void showConfirmDialog(final String friendEmail, final int itemPosition) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.warning))
                .setMessage(getString(R.string.confirm_to_delete))
                .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                          if (userDialog != null){
                              userDialog.dismiss();
                          }
                          presenter.onConfirmToDelectFriendClick(friendEmail,itemPosition);

                    }
                }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create();
        dialog.show();
    }

    @Override
    public void deleteFriendData(final String friendEmail) {
        if (user != null && user.getEmail() != null){
            firestore.collection(FRIENDSHIP)
                    .document(friendEmail)
                    .collection(FRIEND)
                    .document(user.getEmail())
                    .delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                deleteUserFriend(user.getEmail(),friendEmail);
                            }
                        }
                    });
        }

    }

    @Override
    public void changeRecyclerView(int itemPosition) {
        friendArrayList.remove(itemPosition);
        friendPresenter.setData(inviteArrayList,friendArrayList);
        if (adapter != null){
            adapter.notifyDataSetChanged();
        }
    }

    private void deleteUserFriend(final String userEmail, final String friendEmail) {
        firestore.collection(FRIENDSHIP)
                .document(userEmail)
                .collection(FRIEND)
                .document(friendEmail)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            deleteChatData(userEmail,friendEmail);
                        }
                    }
                });
    }

    private void deleteChatData(final String userEmail, final String friendEmail) {


        new Thread(new Runnable() {
            @Override
            public void run() {
                final String path = userEmail + "And" + friendEmail;
                final ArrayList<String> documentId = new ArrayList<>();
                firestore.collection(PERSONAL_CHAT).document(path)
                        .collection(CHAT_DATA)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful() && task.getResult() != null){
                                    for (QueryDocumentSnapshot snapshot : task.getResult()){
                                        documentId.add(snapshot.getId());
                                        Log.i("Michael",snapshot.getId());
                                    }
                                    if (documentId.size() != 0){
                                        for (String id : documentId){
                                            firestore.collection(PERSONAL_CHAT).document(path)
                                                    .collection(CHAT_DATA)
                                                    .document(id)
                                                    .delete()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()){
                                                                Log.i("Michael","刪除成功");
                                                            }
                                                        }
                                                    });
                                        }
                                        deletePath(path);
                                    }else {
                                        searchAgain(userEmail,friendEmail);
                                    }
                                }
                            }
                        });
            }
        }).start();

    }

    private void deletePath(String path) {
        firestore.collection(PERSONAL_CHAT).document(path)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.i("Michael","路徑刪除");
                        }
                    }
                });
    }

    private void searchAgain(String userEmail, String friendEmail) {
        final String path = friendEmail + "And" + userEmail;
        final ArrayList<String> documentId = new ArrayList<>();
        firestore.collection(PERSONAL_CHAT).document(path)
                .collection(CHAT_DATA)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null){
                            for (QueryDocumentSnapshot snapshot : task.getResult()){
                                documentId.add(snapshot.getId());
                                Log.i("Michael",snapshot.getId());
                            }
                            if (documentId.size() != 0){
                                for (String id : documentId){
                                    firestore.collection(PERSONAL_CHAT).document(path)
                                            .collection(CHAT_DATA)
                                            .document(id)
                                            .delete()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Log.i("Michael","刪除成功");
                                                    }
                                                }
                                            });
                                }
                                deletePath(path);
                            }
                        }
                    }
                });
    }

    private void showView(View view) {
        userDialog = new AlertDialog.Builder(this)
                .setView(view).create();
        Window window = userDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        userDialog.show();
    }
}
