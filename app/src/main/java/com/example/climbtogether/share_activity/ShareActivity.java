package com.example.climbtogether.share_activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.climbtogether.R;
import com.example.climbtogether.personal_chat_activity.PersonalChatActivity;
import com.example.climbtogether.share_activity.share_json.ShareArticleJson;
import com.example.climbtogether.share_activity.share_json.ShareClickLikeObject;
import com.example.climbtogether.tool.GlideEngine;
import com.example.climbtogether.tool.ImageLoaderManager;
import com.example.climbtogether.tool.UserDataManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShareActivity extends AppCompatActivity implements ShareActivityVu {

    private RecyclerView recyclerView;

    private ShareActivityPresenter presenter;

    private UserDataManager userDataManager;

    private FirebaseUser user;

    private FirebaseFirestore firestore;

    private ProgressBar progressBar;

    private StorageReference storage;

    private static final String SHARE = "share";

    private static final String REPLY = "reply";

    private ViewPager viewPager;

    private ArrayList<ReplyDTO> replayArray;

    private ImageLoaderManager imageLoaderManager;

    private ReplyDialogAdapter replyAdapter;

    private int itemPosition;

    private ArrayList<Bitmap> bitmapArrayList;

    private ArrayList<byte[]> photoBytesArray;

    private ArrayList<String> downloadUrlArray;

    private static final String FRIENDSHIP = "friendship";

    private static final String FRIEND = "friend";

    private static final String INVITE_FRIEND = "invite_friend";

    private static final String INVITE = "invite";

    private String shareContent;

    private int photoCount = 0;

    private boolean isFriend, isInvite;

    private String content;

    private RoundedImageView ivUserPhoto;
    private TextView tvName, tvEmail, tvInvite;
    private ImageView ivAddFriend, ivFriend;
    private LinearLayout chatClickArea;
    private ProgressBar userDialogProgressbar;

    private NewShareAdapter newAdapter;


    private ArrayList<ShareArticleJson> dataArrayList;
    private Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        userDataManager = new UserDataManager(this);
        imageLoaderManager = new ImageLoaderManager(this);
        gson = new Gson();
        initFirebase();
        initPresenter();
        initView();

        searchData();
    }

    private void searchData() {
        presenter.onShowProgress();
        firestore.collection(SHARE)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            ArrayList<String> jsonStrArray = new ArrayList<>();
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                jsonStrArray.add((String) snapshot.get("json"));
                            }
                            if (jsonStrArray.size() != 0) {
                                presenter.onCatchAllJson(jsonStrArray);
                            }
                        }
                    }
                });

    }


    private void initFirebase() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance().getReference();
    }

    private void initPresenter() {
        presenter = new ShareActivityPresenterImpl(this);
    }

    private void initView() {
        progressBar = findViewById(R.id.share_progressbar);
        Toolbar toolbar = findViewById(R.id.share_toolbar);
        recyclerView = findViewById(R.id.share_recycler_view);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onBackButtonClick();
            }
        });
        FloatingActionButton floatingBtn = findViewById(R.id.share_floating_btn);
        floatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onAddArticleClick();
            }
        });
    }

    @Override
    public void closePage() {
        finish();
    }

    @Override
    public void onShowAddArticleDialog() {
        View view = View.inflate(this, R.layout.add_article_custom_dialog, null);
        viewPager = view.findViewById(R.id.article_view_pager);
        ImageView addBtn = view.findViewById(R.id.article_add_btn);

        TextView tvShare = view.findViewById(R.id.article_text_share);
        TextView tvCancel = view.findViewById(R.id.article_text_cancel);

        EditText edContent = view.findViewById(R.id.article_edit_content);
        //设置EditText的显示方式为多行文本输入
        edContent.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        //文本显示的位置在EditText的最上方
        edContent.setGravity(Gravity.TOP);
        //改变默认的单行模式
        edContent.setSingleLine(false);
        //水平滚动设置为False
        edContent.setHorizontallyScrolling(false);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view).create();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        dialog.show();

        tvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = edContent.getText().toString();
                presenter.onShareButtonClick(userDataManager, content, photoBytesArray);
                dialog.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        addBtn.setVisibility(View.VISIBLE);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PictureSelector.create(ShareActivity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .loadImageEngine(GlideEngine.createGlideEngine())
                        .maxSelectNum(3)
                        .enableCrop(true)
                        .hideBottomControls(false)
                        .showCropFrame(false)
                        .freeStyleCropEnabled(true)
                        .forResult(new OnResultCallbackListener() {
                            @Override
                            public void onResult(List<LocalMedia> result) {
                                addBtn.setVisibility(View.GONE);
                                bitmapArrayList = new ArrayList<>();
                                photoBytesArray = new ArrayList<>();
                                for (int i = 0; i < result.size(); i++) {
                                    File file = new File(result.get(i).getCutPath());
                                    Uri uri = Uri.fromFile(file);
                                    try {
                                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                                        bitmapArrayList.add(bitmap);
                                        //再次解壓縮
                                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(bitmap.getByteCount());
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, outputStream);
                                        photoBytesArray.add(outputStream.toByteArray());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                DialogViewPagerAdapter adapter = new DialogViewPagerAdapter(ShareActivity.this, bitmapArrayList);
                                viewPager.setAdapter(adapter);
                            }
                        });
            }
        });
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


    @Override
    public void showReplayDialog(ShareArticleJson data) {
        View view = View.inflate(this, R.layout.reply_custom_dialog, null);
        RoundedImageView ivUserPhoto = view.findViewById(R.id.reply_dialog_user_photo);
        TextView tvName = view.findViewById(R.id.reply_dialog_user_displayName);
        TextView tvContent = view.findViewById(R.id.reply_dialog_content);
        RecyclerView replayRv = view.findViewById(R.id.reply_dialog_recycler_view);
        EditText edContent = view.findViewById(R.id.reply_dialog_edit_content);
        ImageView ivSend = view.findViewById(R.id.reply_dialog_iv_send);

        replayRv.setLayoutManager(new LinearLayoutManager(this));

        firestore.collection(SHARE)
                .document(data.getContent())
                .collection(REPLY)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            replayArray = new ArrayList<>();
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                ReplyDTO replyDTO = new ReplyDTO();
                                replyDTO.setContent((String) snapshot.get("content"));
                                replyDTO.setUserName((String) snapshot.get("user"));
                                replyDTO.setUserPhoto((String) snapshot.get("userPhoto"));
                                replayArray.add(replyDTO);
                            }
                            if (replayArray != null) {
                                replyAdapter = new ReplyDialogAdapter(replayArray, ShareActivity.this);
                                replayRv.setAdapter(replyAdapter);

                                tvName.setText(data.getDisplayName());
                                imageLoaderManager.setPhotoUrl(data.getUserPhoto(), ivUserPhoto);
                                tvContent.setText(data.getContent());


                                AlertDialog dialog = new AlertDialog.Builder(ShareActivity.this)
                                        .setView(view).create();
                                Window window = dialog.getWindow();
                                if (window != null) {
                                    window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                }
                                dialog.show();


                                ivSend.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String content = edContent.getText().toString();
                                        presenter.onButtonSendReplyClick(replayArray, content, data);
                                        edContent.setText("");
                                    }
                                });
                            }


                        }
                    }
                });


    }

    @Override
    public void sendReply(String content, ArrayList<ReplyDTO> replyArray, ShareArticleJson shareArticleDTO) {

        Map<String, Object> map = new HashMap<>();
        map.put("content", content);
        map.put("user", userDataManager.getDisplayName());
        map.put("userPhoto", userDataManager.getPhotoUrl());
        firestore.collection(SHARE).document(shareArticleDTO.getContent())
                .collection(REPLY)
                .document(content)
                .set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            ReplyDTO data = new ReplyDTO();
                            data.setUserPhoto(userDataManager.getPhotoUrl());
                            data.setContent(content);
                            data.setUserName(userDataManager.getDisplayName());
                            replyArray.add(data);
                            replyAdapter.notifyDataSetChanged();

                            long replyCount = dataArrayList.get(itemPosition).getReply() + 1;
                            dataArrayList.get(itemPosition).setReply(replyCount);
                            newAdapter.notifyDataSetChanged();
                            updateToFirbase(dataArrayList.get(itemPosition));
                        }
                    }
                });

    }

    private void modifyDocumentData(ShareArticleJson shareArticleDTO, long replyCount) {
        Map<String, Object> map = new HashMap<>();
        map.put("reply", replyCount);
        firestore.collection(SHARE)
                .document(shareArticleDTO.getContent())
                .set(map, SetOptions.merge());
    }

    //先檢查好友
    @Override
    public void searchForFriendship(ShareArticleJson data) {
        if (user != null && user.getEmail() != null) {
            firestore.collection(FRIENDSHIP)
                    .document(user.getEmail())
                    .collection(FRIEND)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                    if (snapshot.getId().equals(data.getEmail())) {
                                        isFriend = true;
                                        break;
                                    }
                                }
                                if (!isFriend) {
                                    searchForInvite(data);
                                } else {
                                    presenter.onShowUserDialog(data, isInvite, isFriend);
                                }
                            }
                        }
                    });


        }

    }

    @Override
    public void showUserDialog(ShareArticleJson data, boolean isInvite, boolean isFriend) {


        if (data.getUserPhoto().isEmpty()) {
            ivUserPhoto.setScaleType(ImageView.ScaleType.FIT_CENTER);
        } else {
            ivUserPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageLoaderManager.setPhotoUrl(data.getUserPhoto(), ivUserPhoto);
        }
        tvName.setText(data.getDisplayName());
        tvEmail.setText(data.getEmail());
        ivFriend.setVisibility(isFriend ? View.VISIBLE : View.GONE);
        tvInvite.setVisibility(isInvite ? View.VISIBLE : View.GONE);
        ivAddFriend.setVisibility(!isFriend && !isInvite ? View.VISIBLE : View.GONE);
        chatClickArea.setVisibility(isFriend ? View.VISIBLE : View.GONE);

        ivAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onAddFriendButtonClickListener(data.getEmail(), user.getEmail());
            }
        });
        this.isFriend = false;
        this.isInvite = false;

    }

    @Override
    public void showUserDialog() {
        View view = View.inflate(this, R.layout.user_dialog_custom_view, null);
        ivUserPhoto = view.findViewById(R.id.user_dialog_photo);
        tvName = view.findViewById(R.id.user_dialog_display_name);
        tvEmail = view.findViewById(R.id.user_dialog_email);
        ivAddFriend = view.findViewById(R.id.user_dialog_add_user);
        ivFriend = view.findViewById(R.id.user_dialog_friend);
        tvInvite = view.findViewById(R.id.user_dialog_invite_process);
        chatClickArea = view.findViewById(R.id.user_dialog_chat_clickArea);
        userDialogProgressbar = view.findViewById(R.id.user_dialog_progressbar);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        presenter.onSetDialogViewChange();

        dialog.show();

        presenter.onSearchFriendShip();

    }

    @Override
    public void setProgressStart(boolean isShow) {
        userDialogProgressbar.setVisibility(isShow ? View.VISIBLE : View.GONE);

    }

    @Override
    public void sendInviteToStranger(String strangerEmail, String userEmail) {
        Map<String, Object> map = new HashMap<>();
        map.put("answer", "");
        firestore.collection(INVITE_FRIEND)
                .document(strangerEmail)
                .collection(INVITE)
                .document(userEmail)
                .set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if (ivAddFriend != null && tvInvite != null) {
                                ivAddFriend.setVisibility(View.GONE);
                                tvInvite.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
    }

    @Override
    public void checkFriendship(ShareArticleJson data) {
        if (user != null && user.getEmail() != null) {

            firestore.collection(FRIENDSHIP)
                    .document(user.getEmail())
                    .collection(FRIEND)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                boolean isFriendSend = false;
                                for (QueryDocumentSnapshot snapshot : task.getResult()) {

                                    if (snapshot.getId().equals(data.getEmail())) {
                                        isFriendSend = true;
                                    }

                                }
                                presenter.onIsFriend(data, isFriendSend);
                            }
                        }
                    });
        }
    }

    @Override
    public void intentToPersonalChatActivity(ShareArticleJson data) {
        Intent it = new Intent(this, PersonalChatActivity.class);
        it.putExtra("displayName", data.getDisplayName());
        it.putExtra("mail", data.getEmail());
        it.putExtra("photoUrl", data.getUserPhoto());
        startActivity(it);
    }

    @Override
    public void showNoticeDialog(ShareArticleJson data) {
        View view = View.inflate(this, R.layout.notice_dialog, null);
        TextView tvDisplayName = view.findViewById(R.id.notice_dialog_text_display_name);
        ImageView ivSend = view.findViewById(R.id.notice_dialog_iv_check);
        ImageView ivCancel = view.findViewById(R.id.notice_dialog_iv_cancel);

        tvDisplayName.setText(data.getDisplayName());

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view).create();

        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        dialog.show();
        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                presenter.onAddFriendButtonClickListener(data.getEmail(), user.getEmail());
            }
        });
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void shareArticleJson(String jsonStr, String content) {
        Map<String,Object> map = new HashMap<>();
        map.put("json",jsonStr);
        firestore.collection(SHARE)
                .document(content)
                .set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.i("Michael","上傳成功");
                            Toast.makeText(ShareActivity.this,"分享成功",Toast.LENGTH_LONG).show();
                            searchData();
                        }
                    }
                });
    }

    @Override
    public void showProgressMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void uploadPhoto(UserDataManager userDataManager, String content, ArrayList<byte[]> photoBytesArray) {
        this.content = content;
        this.photoBytesArray = photoBytesArray;
        downloadUrlArray = new ArrayList<>();

        uploadPhotoToStorage();




    }

    private void uploadPhotoToStorage() {
        if (photoCount < photoBytesArray.size()){
            StorageReference river = storage.child(userDataManager.getEmail()+"/"+content+"/"+content+photoCount+".jpg");
            UploadTask task = river.putBytes(photoBytesArray.get(photoCount));
            task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    river.getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrlArray.add(uri.toString());
                                    photoCount ++;
                                    uploadPhotoToStorage();
                                }
                            });
                }
            });
        }else {
            presenter.onCatchallPhotoUrl(userDataManager,content,downloadUrlArray);
        }

    }

    @Override
    public void setNewRecyclerView(ArrayList<ShareArticleJson> dataArrayList) {
        this.dataArrayList = dataArrayList;
        progressBar.setVisibility(View.GONE);
        Log.i("Michael", "set資料");
        newAdapter = new NewShareAdapter(dataArrayList, this, user.getEmail());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(newAdapter);

        newAdapter.setonArticleItemClickListener(new NewShareAdapter.onArticleItemClickListener() {
            @Override
            public void onAddLike(int itemPosition, boolean isChecked, int clickIndex) {
                long likeCount = dataArrayList.get(itemPosition).getLike();
                if (isChecked) {
                    likeCount --;
                    dataArrayList.get(itemPosition).getClick_member().remove(clickIndex);
                    dataArrayList.get(itemPosition).setLike(likeCount);
                    newAdapter.notifyDataSetChanged();
                    updateToFirbase(dataArrayList.get(itemPosition));
                } else {
                    likeCount ++;
                    ShareClickLikeObject data = new ShareClickLikeObject();
                    data.setMemberEmail(userDataManager.getEmail());
                    data.setPhotoUrl(userDataManager.getPhotoUrl());
                    dataArrayList.get(itemPosition).getClick_member().add(data);
                    dataArrayList.get(itemPosition).setLike(likeCount);
                    newAdapter.notifyDataSetChanged();
                    updateToFirbase(dataArrayList.get(itemPosition));
                }

            }

            @Override
            public void onSendClick(ShareArticleJson data) {
                presenter.onSendMessageClickListener(data);
            }

            @Override
            public void onSetting(ShareArticleJson data, int itemPosition) {

            }

            @Override
            public void onUserClick(ShareArticleJson data) {
                presenter.onUserPhotoClickListener(data);
            }

            @Override
            public void onReplyClick(ShareArticleJson shareArticleDTO, int position) {
                itemPosition = position;
                presenter.onReplyButtonClick(shareArticleDTO);
            }
        });
    }

    private void updateToFirbase(ShareArticleJson shareArticleJson) {

        String str = gson.toJson(shareArticleJson);
        Map<String,Object> map = new HashMap<>();
        map.put("json",str);
        firestore.collection(SHARE)
                .document(shareArticleJson.getContent())
                .set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.i("Michael","更新成功");
                        }
                    }
                });

    }

    //如果不是好友 擇檢查有沒有邀請紀錄
    private void searchForInvite(ShareArticleJson data) {
        if (user != null && user.getEmail() != null) {
            firestore.collection(INVITE_FRIEND)
                    .document(user.getEmail())
                    .collection(INVITE)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                    if (snapshot.getId().equals(data.getEmail())) {
                                        isInvite = true;
                                    }
                                }
                                if (!isInvite) {
                                    searchForInviteAgain(data, user.getEmail());
                                } else {
                                    presenter.onShowUserDialog(data, isInvite, isFriend);
                                }

                            }
                        }
                    });
        }

    }

    //再次檢查另一端地邀請紀錄
    private void searchForInviteAgain(ShareArticleJson data, String email) {
        firestore.collection(INVITE_FRIEND)
                .document(data.getEmail())
                .collection(INVITE)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                if (snapshot.getId().equals(email)) {
                                    isInvite = true;
                                }
                            }
                            presenter.onShowUserDialog(data, isInvite, isFriend);
                        }
                    }
                });
    }

    @Override
    public void showProgress(boolean isShow) {
        progressBar.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

}
