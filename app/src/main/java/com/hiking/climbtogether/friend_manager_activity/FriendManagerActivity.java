package com.hiking.climbtogether.friend_manager_activity;

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

import com.hiking.climbtogether.R;
import com.hiking.climbtogether.friend_manager_activity.friend_presenter.FriendPresenter;
import com.hiking.climbtogether.friend_manager_activity.friend_presenter.FriendPresenterImpl;
import com.hiking.climbtogether.friend_manager_activity.view.FriendViewAdapter;
import com.hiking.climbtogether.friend_manager_activity.view.InviteViewAdapter;
import com.hiking.climbtogether.personal_chat_activity.PersonalChatActivity;
import com.hiking.climbtogether.tool.ImageLoaderManager;
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
import java.util.HashMap;
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

    private AlertDialog userDialog;

    private String chatRoomPath;

    private TextView tvInfo;

    private ImageView ivLogo;

    private ArrayList<ChatRoomDTO> chatPathArray;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_manager);
        imageLoaderManager = new ImageLoaderManager(this);
        initPresenter();
        initFirebase();
        searchForChatRoom();
        initView();
        checkAllData();
    }

    private void searchForChatRoom() {
        firestore.collection("chat_room")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null){
                            chatPathArray = new ArrayList<>();
                            for (QueryDocumentSnapshot snapshot : task.getResult()){
                                ChatRoomDTO data = new ChatRoomDTO();
                                data.setChatPath(snapshot.getId());
                                data.setUser1((String)snapshot.get("user1"));
                                data.setUser2((String)snapshot.get("user2"));
                                chatPathArray.add(data);
                            }
                        }
                    }
                });
    }

    /**
     * 先檢查邀清
     * 在檢查好友,如果有好友的話,列出列表並下載好友頭貼
     */

    private void checkAllData() {
        if (user != null) {
            if (user.getEmail() != null) {
                firestore.collection(INVITE_FRIEND).document(user.getEmail()).collection(INVITE)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    inviteArrayList = new ArrayList<>();
                                    if (task.getResult() != null) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            FriendInviteDTO friendInviteDTO = new FriendInviteDTO();
                                            friendInviteDTO.setAnswer((String) document.getData().get("answer"));
                                            friendInviteDTO.setEmail(document.getId());
                                            inviteArrayList.add(friendInviteDTO);
                                        }
                                        if (inviteArrayList.size() != 0) {
                                            Log.i("Michael", "有邀情");
                                        } else {
                                            Log.i("Michael", "沒邀請");
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
                        if (task.isSuccessful()) {
                            if (task.getResult() != null) {
                                friendArrayList = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    FriendDTO friendDTO = new FriendDTO();
                                    friendDTO.setDisplayName((String) document.getData().get("displayName"));
                                    friendDTO.setEmail((String) document.getData().get("email"));
                                    friendArrayList.add(friendDTO);
                                }
                                if (friendArrayList.size() != 0) {
                                    Log.i("Michael", "有朋友");
                                } else {
                                    Log.i("Michael", "沒朋友");
                                }

                                presenter.onCatchDataSuccessful(inviteArrayList, friendArrayList);

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
        tvInfo = findViewById(R.id.friend_manager_text_info);
        ivLogo = findViewById(R.id.friend_manager_logo);
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

        friendPresenter.setData(inviteArrayList, friendArrayList);

        adapter = new FriendManagerAdapter(friendPresenter, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        recyclerView.setAdapter(adapter);

        adapter.setOnfriendItemClickListener(new FriendViewAdapter.OnfriendItemClickListener() {
            @Override
            public void onClick(FriendDTO data, int itemPosition) {
                presenter.onFriendItemClickListener(data, itemPosition);
            }
        });

        adapter.setOnCheckInviteListener(new InviteViewAdapter.OnCheckInviteIconClickListener() {
            @Override
            public void onClickYes(boolean isAdd, FriendInviteDTO data, int itemPosition) {
                inviteArrayList.remove(itemPosition);
                friendPresenter.setData(inviteArrayList, friendArrayList);
                adapter.notifyDataSetChanged();
                deleteInviteData(data);
                createFriendData(data);
            }

            @Override
            public void onClickNo(boolean isAdd, FriendInviteDTO data, int itemPosition) {
                inviteArrayList.remove(itemPosition);
                friendPresenter.setData(inviteArrayList, friendArrayList);
                adapter.notifyDataSetChanged();
                deleteInviteData(data);
            }
        });

    }

    private void createFriendData(final FriendInviteDTO data) {
        firestore.collection("users").document(data.getEmail()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            DocumentSnapshot document = task.getResult();

                            //處理畫面
                            FriendDTO data = new FriendDTO();
                            data.setDisplayName((String) document.get("displayName"));
                            data.setEmail((String) document.get("email"));
                            friendArrayList.add(data);
                            friendPresenter.setData(inviteArrayList, friendArrayList);
                            adapter.notifyDataSetChanged();

                            //繼續做連線端的事情
                            becomeToFriend(user.getEmail(), data.getEmail(), document.getData());
                        }
                    }
                });
    }

    private void becomeToFriend(final String userEmail, final String friendEmail, Map<String, Object> map) {
        firestore.collection(FRIENDSHIP).document(userEmail)
                .collection(FRIEND).document(friendEmail)
                .set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            createUserData(userEmail, friendEmail);
                        }
                    }
                });
    }

    private void createUserData(final String userEmail, final String friendEmail) {
        firestore.collection("users").document(userEmail).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {

                            DocumentSnapshot document = task.getResult();
                            Map<String, Object> map = document.getData();

                            becomeToNewFriend(userEmail, friendEmail, map);

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
                            createChatRoom(friendEmail, userEmail);
                        }
                    }
                });
    }

    private void createChatRoom(String friendEmail, String userEmail) {
        Map<String, Object> map = new HashMap<>();
        map.put("user1", userEmail);
        map.put("user2", friendEmail);
        firestore.collection("chat_room")
                .document()
                .set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.i("Michael", "新增房間成功");
                            searchForChatRoom();
                        }
                    }
                });

    }

    private void deleteInviteData(FriendInviteDTO data) {
        if (user != null) {
            if (user.getEmail() != null) {
                firestore.collection(INVITE_FRIEND).document(user.getEmail()).collection(INVITE).document(data.getEmail())
                        .delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.i("Michael", "已刪除");
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
        final View view = View.inflate(this, R.layout.friend_managemnet_dialog_custom_view, null);
        final RoundedImageView ivUserPhoto = view.findViewById(R.id.friend_dialog_photo);
        final TextView tvName = view.findViewById(R.id.friend_dialog_display_name);
        final TextView tvEmail = view.findViewById(R.id.friend_dialog_email);
        LinearLayout chatClickArea = view.findViewById(R.id.friend_dialog_chat_clickArea);
        LinearLayout deleteClickArea = view.findViewById(R.id.friend_dialog_delete_clickArea);

        StorageReference river = storage.child(data.getEmail() + "/userPhoto/" + data.getEmail() + ".jpg");
        river.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        friendPhotoUrl = uri.toString();
                        ivUserPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imageLoaderManager.setPhotoUrl(friendPhotoUrl, ivUserPhoto);
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

                presenter.onChatButtonClickListener(data.getEmail(), data.getDisplayName(), friendPhotoUrl);
                userDialog.dismiss();
            }
        });
        deleteClickArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onDeleteButtonClickListener(data.getEmail(), itemPosition);
                userDialog.dismiss();
            }
        });
    }

    @Override
    public void intentToPersonalChatActivity(String email, String displayName, String friendPhotoUrl) {

        for (ChatRoomDTO data : chatPathArray){
            if (data.getUser1().equals(user.getEmail()) && data.getUser2().equals(email)){
                chatRoomPath = data.getChatPath();
                break;
            }else if (data.getUser1().equals(email)&&data.getUser2().equals(user.getEmail())){
                chatRoomPath = data.getChatPath();
                break;
            }
        }
        Intent it = new Intent(this, PersonalChatActivity.class);
        it.putExtra("displayName", displayName);
        it.putExtra("mail", email);
        it.putExtra("photoUrl", friendPhotoUrl);
        it.putExtra("path",chatRoomPath);
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

                        presenter.onConfirmToDelectFriendClick(friendEmail, itemPosition);
                        if (userDialog != null) {
                            userDialog.dismiss();
                        }

                    }
                }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (userDialog != null) {
                            userDialog.dismiss();
                        }
                    }
                }).create();
        dialog.show();
    }

    @Override
    public void deleteFriendData(final String friendEmail) {
        if (user != null && user.getEmail() != null) {
            firestore.collection(FRIENDSHIP)
                    .document(friendEmail)
                    .collection(FRIEND)
                    .document(user.getEmail())
                    .delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                deleteUserFriend(user.getEmail(), friendEmail);
                            }
                        }
                    });
        }

    }

    @Override
    public void changeRecyclerView(int itemPosition) {
        friendArrayList.remove(itemPosition);
        friendPresenter.setData(inviteArrayList, friendArrayList);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showNoFriendInfo(boolean isShow) {
        tvInfo.setVisibility(isShow ? View.VISIBLE : View.GONE);
        ivLogo.setVisibility(isShow ? View.VISIBLE : View.GONE);
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
                        if (task.isSuccessful()) {
                            deleteChatData(userEmail, friendEmail);
                        }
                    }
                });
    }

    private void deleteChatData(final String userEmail, final String friendEmail) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                firestore.collection("chat_room")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful() && task.getResult() != null) {
                                    for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                        String user1 = (String) snapshot.get("user1");
                                        String user2 = (String) snapshot.get("user2");
                                        if (user1 != null && user2 != null) {
                                            if (user1.equals(userEmail) && user2.equals(friendEmail)) {
                                                chatRoomPath = snapshot.getId();
                                                break;
                                            } else if (user1.equals(friendEmail) && user2.equals(userEmail)) {
                                                chatRoomPath = snapshot.getId();
                                                break;
                                            }
                                        } else {
                                            Log.i("Michael", "沒資料");
                                        }
                                    }
                                    if (chatRoomPath != null) {
                                        firestore.collection("chat_data")
                                                .document(chatRoomPath)
                                                .delete()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.i("Michael", "聊天刪除成功");
                                                        }
                                                    }
                                                });
                                        firestore.collection("chat_room")
                                                .document(chatRoomPath)
                                                .delete()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            Log.i("Michael","路徑刪除成功");
                                                        }
                                                    }
                                                });
                                    }

                                }
                            }
                        });
            }
        }).start();

    }


    private void showView(View view) {
        userDialog = new AlertDialog.Builder(this)
                .setView(view).create();
        Window window = userDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        if (!userDialog.isShowing()){
            userDialog.show();
        }

    }
}
