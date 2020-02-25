package com.example.climbtogether.share_activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.climbtogether.R;
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
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShareActivity extends AppCompatActivity implements ShareActivityVu {

    private RecyclerView recyclerView;

    private Toolbar toolbar;

    private FloatingActionButton floatingBtn;

    private ShareActivityPresenter presenter;

    private UserDataManager userDataManager;

    private FirebaseAuth mAuth;

    private FirebaseUser user;

    private FirebaseFirestore firestore;

    private ProgressBar progressBar;

    private StorageReference storage;

    private ArrayList<ShareArticleDTO> shareArray;

    private ArrayList<LikeMemberDTO> likeMemberArray;

    private static final String ADD_LIKE = "addLike";

    private static final String SHARE = "share";

    private static final String REPLY = "reply";

    private byte[] selectPhotoBytes;

    private ViewPager viewPager;

    private int memberCount = 0;

    private ShareAdapter adapter;

    private ArrayList<ReplyDTO> replayArray,replyDialogArray;

    private ArrayList<ReplyObject> replayArrayList;

    private ImageLoaderManager imageLoaderManager;

    private ReplyDialogAdapter replyAdapter;

    private int itemPosition;

    private ArrayList<Bitmap> bitmapArrayList;

    private ArrayList<byte[]> photoBytesArray;

    private ArrayList<String> downloadUrlArray;

    private String shareContent;
    private int photoCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        userDataManager = new UserDataManager(this);
        imageLoaderManager = new ImageLoaderManager(this);
        initFirebase();
        initPresenter();
        initView();

        searchData();
    }

    private void searchData() {
        clearView();
        presenter.onShowProgress();
        firestore.collection(SHARE).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        shareArray = new ArrayList<>();
                        likeMemberArray = new ArrayList<>();
                        ShareArticleDTO data = null;
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                data = new ShareArticleDTO();
                                data.setContent((String) snapshot.get("content"));
                                data.setDiaplayName((String) snapshot.get("user"));
                                data.setEmail((String) snapshot.get("userEmail"));
                                data.setSelectPhoto((String) snapshot.get("selectPhotoUrl0"));
                                data.setSelectPhoto1((String)snapshot.get("selectPhotoUrl1"));
                                data.setSelectPhoto2((String)snapshot.get("selectPhotoUrl2"));
                                data.setUserPhoto((String) snapshot.get("userPhotoUrl"));
                                data.setLike((Long) snapshot.get("like"));
                                shareArray.add(data);
                            }
                            replayArrayList = new ArrayList<>();
                            searchReplyData();
                        }
                    }
                });

    }

    private void clearView() {
        shareArray = new ArrayList<>();
        likeMemberArray = new ArrayList<>();
        replayArrayList = new ArrayList<>();
        if (adapter != null){
            adapter = new ShareAdapter(shareArray,likeMemberArray,user.getEmail(),replayArrayList,this);

            recyclerView.setAdapter(adapter);
        }
    }

    private void searchReplyData() {
        replayArray = new ArrayList<>();
        if (memberCount < shareArray.size()) {
            firestore.collection(SHARE).document(shareArray.get(memberCount).getContent())
                    .collection(REPLY)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                Log.i("Michael","搜尋回覆內容");
                                ReplyObject data = new ReplyObject();
                                data.setArticleName(shareArray.get(memberCount).getContent());
                                for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                    ReplyDTO reply = new ReplyDTO();
                                    reply.setContent((String) snapshot.get("content"));
                                    reply.setUserName((String) snapshot.get("user"));
                                    reply.setUserPhoto((String) snapshot.get("userPhoto"));
                                    Log.i("Michael","留言 : "+snapshot.get("content"));
                                    replayArray.add(reply);
                                }
                                data.setReplyArray(replayArray);
                                replayArrayList.add(data);

                                memberCount++;
                                searchReplyData();
                            }
                        }
                    });
        }else {
            memberCount = 0;
            searchForLikeMember();
        }


    }

    private void searchForLikeMember() {
        if (user != null && user.getEmail() != null) {
            if (memberCount < shareArray.size()) {
                Log.i("Michael", "文章資料長度 : " + shareArray.size() + " , 筆數 : " + memberCount + " , 文章 : " + shareArray.get(memberCount).getContent());
                Log.i("Michael", "搜尋按讚人數");
                firestore.collection(SHARE).document(shareArray.get(memberCount).getContent()).collection(ADD_LIKE)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful() && task.getResult() != null) {
                                    LikeMemberDTO data = new LikeMemberDTO();
                                    ArrayList<String> nameList = new ArrayList<>();
                                    ArrayList<Boolean> isCheckArray = new ArrayList<>();
                                    data.setContent(shareArray.get(memberCount).getContent());
                                    Log.i("Michael", "文章 : " + shareArray.get(memberCount).getContent());
                                    for (QueryDocumentSnapshot snapshot : task.getResult()) {

                                        if (user.getEmail().equals(snapshot.getId())) {
                                            isCheckArray.add(true);
                                        } else {
                                            isCheckArray.add(false);
                                        }
                                        Log.i("Michael", "按讚的有 : " + snapshot.getId());
                                        nameList.add(snapshot.getId());
                                    }
                                    data.setIsCheckArray(isCheckArray);
                                    data.setMemberEmail(nameList);
                                    likeMemberArray.add(data);
                                    memberCount++;
                                    searchForLikeMember();
                                } else {
                                    Log.i("Michael", "搜尋失敗");
                                }
                            }
                        });
            } else {
                memberCount = 0;
                presenter.onCatchAllData(shareArray, likeMemberArray,replayArrayList);
            }
        }


    }


    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance().getReference();
    }

    private void initPresenter() {
        presenter = new ShareActivityPresenterImpl(this);
    }

    private void initView() {
        progressBar = findViewById(R.id.share_progressbar);
        toolbar = findViewById(R.id.share_toolbar);
        recyclerView = findViewById(R.id.share_recycler_view);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onBackButtonClick();
            }
        });
        floatingBtn = findViewById(R.id.share_floating_btn);
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
                                for (int i = 0 ; i < result.size() ;i ++){
                                    File file = new File(result.get(i).getCutPath());
                                    Uri uri = Uri.fromFile(file);
                                    try{
                                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                                        bitmapArrayList.add(bitmap);
                                        //再次解壓縮
                                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(bitmap.getByteCount());
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, outputStream);
                                        photoBytesArray.add(outputStream.toByteArray());
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                                Log.i("Michael","準備好上傳的bytes長度 : "+photoBytesArray.size());
                                DialogViewPagerAdapter adapter = new DialogViewPagerAdapter(ShareActivity.this,bitmapArrayList);
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
    public void shareArticle(UserDataManager userDataManager, String content, ArrayList<byte[]> photoBytesArray) {

        this.photoBytesArray = photoBytesArray;
        this.shareContent = content;
        downloadUrlArray = new ArrayList<>();
        uploadPhoto();
        //難題來了 命名方式

    }

    private void uploadPhoto() {
        if (photoCount < photoBytesArray.size()){
            StorageReference river = storage.child(userDataManager.getEmail() + "/" + SHARE + "/" + shareContent +"/"+shareContent+photoCount+ ".jpg");
            UploadTask uploadTask = river.putBytes(photoBytesArray.get(photoCount));
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    river.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            downloadUrlArray.add(url);
                            photoCount++;
                            uploadPhoto();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }else {
            presenter.onCatchSelectPhotoUrl(downloadUrlArray, shareContent);
        }

    }

    @Override
    public void createArticle(ArrayList<String> downloadUrlArray, String content) {
        Map<String, Object> map = new HashMap<>();
        int urlSize = downloadUrlArray.size();
        if (urlSize == 1){
            map.put("content", content);
            map.put("like", 0);
            map.put("selectPhotoUrl0", downloadUrlArray.get(0));
            map.put("selectPhotoUrl1", "");
            map.put("selectPhotoUrl2", "");
            map.put("user", userDataManager.getDisplayName());
            map.put("userPhotoUrl", userDataManager.getPhotoUrl());
            map.put("userEmail", userDataManager.getEmail());
        }else if (urlSize == 2){
            map.put("content", content);
            map.put("like", 0);
            map.put("selectPhotoUrl0", downloadUrlArray.get(0));
            map.put("selectPhotoUrl1", downloadUrlArray.get(1));
            map.put("selectPhotoUrl2", "");
            map.put("user", userDataManager.getDisplayName());
            map.put("userPhotoUrl", userDataManager.getPhotoUrl());
            map.put("userEmail", userDataManager.getEmail());
        }else {
            map.put("content", content);
            map.put("like", 0);
            map.put("selectPhotoUrl0", downloadUrlArray.get(0));
            map.put("selectPhotoUrl1", downloadUrlArray.get(1));
            map.put("selectPhotoUrl2", downloadUrlArray.get(2));
            map.put("user", userDataManager.getDisplayName());
            map.put("userPhotoUrl", userDataManager.getPhotoUrl());
            map.put("userEmail", userDataManager.getEmail());
        }




        firestore.collection(SHARE).document(content).set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            presenter.onShowSuccessShareArticle();
                            Log.i("Michael", "文章上傳成功");
                            searchData();
                        }
                    }
                });
    }

    @Override
    public void setRecyclerView(ArrayList<ShareArticleDTO> shareArray, ArrayList<LikeMemberDTO> listMemberArray,ArrayList<ReplyObject> replyArray) {

        presenter.onCloseProgress();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ShareAdapter(shareArray, listMemberArray, user.getEmail(),replyArray, this);
        recyclerView.setAdapter(adapter);

        adapter.setonArticleItemClickListener(new ShareAdapter.onArticleItemClickListener() {
            @Override
            public void onAddLike(int itemPosition, int memberIndex) {
                long likeCount = shareArray.get(itemPosition).getLike();
                if (likeMemberArray.get(itemPosition).getIsCheckArray() == null
                        || likeMemberArray.get(itemPosition).getIsCheckArray().size() == 0) {
                    changeAddLikeData(true, itemPosition, likeCount);
                    likeMemberArray.get(itemPosition).getIsCheckArray().add(true);
                    shareArray.get(itemPosition).setLike(likeCount + 1);
                    adapter.notifyDataSetChanged();
                    return;
                }
                if (likeMemberArray.get(itemPosition).getIsCheckArray().get(memberIndex)) {
                    likeMemberArray.get(itemPosition).getIsCheckArray().set(memberIndex, false);
                    changeAddLikeData(likeMemberArray.get(itemPosition).getIsCheckArray().get(memberIndex), itemPosition, likeCount);
                    shareArray.get(itemPosition).setLike(likeCount - 1);
                    adapter.notifyDataSetChanged();
                } else {
                    likeMemberArray.get(itemPosition).getIsCheckArray().set(memberIndex, true);
                    changeAddLikeData(likeMemberArray.get(itemPosition).getIsCheckArray().get(memberIndex), itemPosition, likeCount);
                    shareArray.get(itemPosition).setLike(likeCount + 1);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onSendClick(ShareArticleDTO data) {

            }

            @Override
            public void onSetting(ShareArticleDTO data, int itemPosition) {

            }

            @Override
            public void onUserClick(ShareArticleDTO data) {

            }

            @Override
            public void onReplyClick(ReplyObject data,ShareArticleDTO shareArticleDTO,int position) {
                itemPosition = position;
                presenter.onReplyButtonClick(data,shareArticleDTO);

            }
        });

    }

    @Override
    public void showReplayDialog(ReplyObject data,ShareArticleDTO shareArticleDTO) {
        View view = View.inflate(this,R.layout.reply_custom_dialog,null);
        RoundedImageView ivUserPhoto = view.findViewById(R.id.reply_dialog_user_photo);
        TextView tvName = view.findViewById(R.id.reply_dialog_user_displayName);
        TextView tvContent = view.findViewById(R.id.reply_dialog_content);
        RecyclerView replayRv = view.findViewById(R.id.reply_dialog_recycler_view);
        EditText edContent = view.findViewById(R.id.reply_dialog_edit_content);
        ImageView ivSend = view.findViewById(R.id.reply_dialog_iv_send);

        replayRv.setLayoutManager(new LinearLayoutManager(this));
        this.replyDialogArray = data.getReplyArray();
        replyAdapter = new ReplyDialogAdapter(replyDialogArray,this);
        replayRv.setAdapter(replyAdapter);

        tvName.setText(shareArticleDTO.getDiaplayName());
        imageLoaderManager.setPhotoUrl(shareArticleDTO.getUserPhoto(),ivUserPhoto);
        tvContent.setText(shareArticleDTO.getContent());


        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view).create();
        Window window = dialog.getWindow();
        if (window != null){
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        dialog.show();


        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = edContent.getText().toString();
                presenter.onButtonSendReplyClick(data.getReplyArray(),content,shareArticleDTO);
                edContent.setText("");
            }
        });



    }

    @Override
    public void sendReply(String content, ArrayList<ReplyDTO> replyArray, ShareArticleDTO shareArticleDTO) {

        Map<String,Object> map = new HashMap<>();
        map.put("content",content);
        map.put("user",userDataManager.getDisplayName());
        map.put("userPhoto",userDataManager.getPhotoUrl());
        firestore.collection(SHARE).document(shareArticleDTO.getContent())
                .collection(REPLY)
                .document(content)
                .set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            ReplyDTO data = new ReplyDTO();
                            data.setUserPhoto(userDataManager.getPhotoUrl());
                            data.setContent(content);
                            data.setUserName(userDataManager.getDisplayName());
                            replyDialogArray.add(data);
                            replyAdapter.notifyDataSetChanged();

                            replayArrayList.get(itemPosition).setReplyArray(replyDialogArray);
                            adapter.notifyDataSetChanged();
                            Log.i("Michael","新增留言成功");
                        }
                    }
                });

    }

    @Override
    public void showProgress(boolean isShow) {
        progressBar.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }



    private void changeAddLikeData(Boolean isCheck, int itemPosition, long likeCount) {
        if (user != null && user.getEmail() != null) {
            if (isCheck) {
                Map<String, Object> map = new HashMap<>();
                firestore.collection(SHARE).document(shareArray.get(itemPosition).getContent())
                        .collection(ADD_LIKE).document(user.getEmail())
                        .set(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.i("Michael", "點讚新增成功");
                                    Map<String, Object> countMap = new HashMap<>();
                                    countMap.put("like", likeCount + 1);
                                    firestore.collection(SHARE).document(shareArray.get(itemPosition).getContent())
                                            .set(countMap, SetOptions.merge());
                                }
                            }
                        });
            } else {
                firestore.collection(SHARE).document(shareArray.get(itemPosition).getContent())
                        .collection(ADD_LIKE).document(user.getEmail())
                        .delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.i("Michael", "刪除點讚成功");
                                Map<String, Object> countMap = new HashMap<>();
                                countMap.put("like", likeCount - 1);
                                firestore.collection(SHARE).document(shareArray.get(itemPosition).getContent())
                                        .set(countMap, SetOptions.merge());
                            }
                        });

            }

        }
    }


}
