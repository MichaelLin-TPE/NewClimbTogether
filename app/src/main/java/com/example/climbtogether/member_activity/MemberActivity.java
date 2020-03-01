package com.example.climbtogether.member_activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.climbtogether.R;
import com.example.climbtogether.friend_manager_activity.FriendManagerActivity;
import com.example.climbtogether.login_activity.LoginActivity;
import com.example.climbtogether.mountain_collection_activity.MountainCollectionActivity;
import com.example.climbtogether.tool.ImageLoaderManager;
import com.example.climbtogether.tool.UserDataManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MemberActivity extends AppCompatActivity implements MemberActivityVu {

    private RecyclerView recyclerView;

    private TextView tvNotice, tvEmail;

    private RoundedImageView ivUserIcon;

    private Button btnLogin;

    private FirebaseAuth mAuth;

    private MemberActivityPresenter presenter;

    private FirebaseUser currentUser;

    private MenuItem signOut;

    private GoogleSignInClient signInClient;

    private StorageReference storage;

    private MemberRecyclerViewAdapter adapter;

    private FirebaseFirestore firestore;

    private ImageLoaderManager imageLoaderManager;

    private UserDataManager userDataManager;

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.member_menu, menu);

        signOut = menu.findItem(R.id.member_sign_out);

        if (currentUser != null) {
            signOut.setVisible(true);
        } else {
            signOut.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.member_sign_out) {
            presenter.onSignOutClickListener();
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            presenter.onChangeView(false);
            if (currentUser != null && currentUser.getEmail() != null){
                firestore.collection("users")
                        .document(currentUser.getEmail())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful() && task.getResult() != null){
                                    DocumentSnapshot snapshot = task.getResult();
                                    userDataManager.saveUserData((String)snapshot.get("email"),(String)snapshot.get("displayName"),(String)snapshot.get("photoUrl"));
                                }
                            }
                        });

                //申請推播TOKEN
                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                if (task.isSuccessful() && task.getResult() != null){
                                    String token = task.getResult().getToken();

                                    Log.i("Michael","new Token : "+token);
                                    userDataManager.saveNotificationToken(token);
                                    updateUserToken(token);
                                }

                            }
                        });
            }

        } else {
            presenter.onChangeView(true);
        }
    }

    private void updateUserToken(String token) {
        if (currentUser != null && currentUser.getEmail() != null){
            Map<String,Object> map = new HashMap<>();
            map.put("token",token);
            firestore.collection("users").document(currentUser.getEmail())
                    .set(map, SetOptions.merge());
        }
    }

    @Override
    public void changeView(boolean isShow) {
        tvNotice.setVisibility(isShow ? View.VISIBLE : View.GONE);
        btnLogin.setVisibility(isShow ? View.VISIBLE : View.GONE);
        ivUserIcon.setVisibility(isShow ? View.GONE : View.VISIBLE);
        tvEmail.setVisibility(isShow ? View.GONE : View.VISIBLE);
        tvEmail.setText(isShow ? "" : String.format(Locale.getDefault(), "目前登入 : %s", currentUser.getEmail()));

        if (signOut != null) {
            signOut.setVisible(!isShow);
        }
        adapter.setCurrent(isShow ? null : currentUser);
        adapter.notifyDataSetChanged();


    }

    @Override
    protected void onResume() {
        super.onResume();
        currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);


    }

    @Override
    public void intentToLoginActivity() {
        Intent it = new Intent(this, LoginActivity.class);
        startActivity(it);
    }

    @Override
    public void signOut() {
        if (mAuth != null) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getResources().getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            if (signInClient == null) {
                signInClient = GoogleSignIn.getClient(this, gso);
            }
            mAuth.signOut();
            signInClient.revokeAccess().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    presenter.onChangeView(true);

                }
            });
            removeUserToken();

        }
    }

    private void removeUserToken() {
        if (currentUser != null && currentUser.getEmail() != null){
            Map<String,Object> map = new HashMap<>();
            map.put("token","remove");
            firestore.collection("users")
                    .document(currentUser.getEmail())
                    .set(map,SetOptions.merge());
        }

    }

    @Override
    public void showConfirmSignOutDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(getString(R.string.confirm_to_sign_out))
                .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.onConfirmSignOutClickListener();
                    }
                }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        dialog.show();
    }

    @Override
    public void intentToBrowser(String url) {
        Intent it = new Intent(Intent.ACTION_VIEW);
        it.setData(Uri.parse(url));
        startActivity(it);
    }

    @Override
    public void intentToMtCollectionActivity() {
        Intent it = new Intent(this, MountainCollectionActivity.class);
        startActivity(it);
    }



    @Override
    public void showToast(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void downloadUserPhoto() {
        if (currentUser != null){
            StorageReference river = storage.child(currentUser.getEmail()+"/userPhoto/"+currentUser.getEmail()+".jpg");
            river.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String url = uri.toString();
                    ivUserIcon.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageLoaderManager.setPhotoUrl(url,ivUserIcon);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //do nothing
                    e.printStackTrace();
                    Log.i("Michael","沒照片");
                    Log.i("Michael",e.toString());
                    ivUserIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    ivUserIcon.setImageResource(R.drawable.empty_photo);
                }
            });
        }
    }

    @Override
    public void intentToFriendManagerActivity() {
        Intent it = new Intent(this, FriendManagerActivity.class);
        startActivity(it);
    }

    @Override
    public void uploadUserPhoto() {

//        Intent it = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(it, IMAGE_REQUEST_CODE);

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK && result != null) {

                Uri resultUri = result.getUri();
                try{
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),resultUri);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int quality = 80;
                    bitmap.compress(Bitmap.CompressFormat.JPEG,quality,baos);
                    byte[] bytes = baos.toByteArray();

                    String message = getString(R.string.please_wait);

                    presenter.onShowProgressToast(message);

                    uploadPhotoToStorage(bytes);

                }catch (Exception e){
                    e.printStackTrace();
                }




            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

//        if (requestCode == IMAGE_REQUEST_CODE) {
//            Bitmap bitmap;
//            try {
//                Log.i("Michael", "壓縮照片");
//                if (data != null) {
//                    Uri uri = data.getData();
//                    ContentResolver or = this.getContentResolver();
//                    if (uri != null) {
//                        bitmap = BitmapFactory.decodeStream(or.openInputStream(uri));
//                        Log.i("Michael", "取得的照片 : " + bitmap);
//                        //壓縮照片
//                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                        int quality = 10;
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
//                        byte[] bytes = baos.toByteArray();
//                        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                        Log.i("Michael", "壓縮後的圖片大小 : " + (bitmap.getByteCount() / 1024 / 1024) + " , 寬度 : " + bitmap.getWidth()
//                                + " , 高度為 : " + bitmap.getHeight() + " , bytes 長度 : "
//                                + (bytes.length / 1024) + " kb " + "quality = " + quality);
//                        //Uri 轉 Bitmap
////                            uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(),bitmap,null,null));
//                        String message = getString(R.string.please_wait);
//
//                        presenter.onShowProgressToast(message);
//
//                        uploadPhotoToStorage(bytes);
//
//
//                    } else {
//                        Log.i("Michael", "photo uri = null");
//                    }
//                } else {
//                    Log.i("Michael", " data = null");
//                }
//
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                Log.i("Michael", "壓縮照片錯誤");
//            }
//        }
    }

    private void uploadPhotoToStorage(final byte[] bytes) {
        if (currentUser != null){
            final StorageReference river = storage.child(currentUser.getEmail()+"/userPhoto/"+currentUser.getEmail()+".jpg");
            UploadTask task = river.putBytes(bytes);
            task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String message = getString(R.string.upload_success);
                    presenter.onShowProgressToast(message);
                    ivUserIcon.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    ivUserIcon.setImageBitmap(bitmap);

                    river.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String photoUrl = uri.toString();
                            updateUserData(photoUrl);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }

    private void updateUserData(String photoUrl) {
        if (currentUser != null && currentUser.getEmail() != null){
            Map<String,Object> map = new HashMap<>();
            map.put("photoUrl",photoUrl);
            UserDataManager userDataManager = new UserDataManager(this);
            if (!userDataManager.getToken().isEmpty()){
                map.put("token",userDataManager.getToken());
            }
            firestore.collection("users").document(currentUser.getEmail())
                    .set(map, SetOptions.merge());
            firestore.collection("users").document(currentUser.getEmail())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null){
                                DocumentSnapshot snapshot = task.getResult();
                                userDataManager.saveUserData((String)snapshot.get("email"),(String) snapshot.get("displayName"),photoUrl);
                            }
                        }
                    });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);
        //初始化Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();
        imageLoaderManager = new ImageLoaderManager(this);
        userDataManager = new UserDataManager(this);
        initPresenter();
        initView();
        presenter.onShowRecycler();
    }


    private void initView() {
        Toolbar toolbar = findViewById(R.id.member_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
        recyclerView = findViewById(R.id.member_recycler_view);
        tvNotice = findViewById(R.id.member_text_notice);
        tvEmail = findViewById(R.id.member_text_email);
        ivUserIcon = findViewById(R.id.member_login_icon);
        btnLogin = findViewById(R.id.member_btn_login);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onLoginButtonClickListener();
            }
        });


        ivUserIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onUploadUserPhotoListener();
            }
        });

    }

    private void initPresenter() {
        presenter = new MemberActivityPresenterImpl(this);
    }

    @Override
    public Context getVuContext() {
        return this;
    }

    @Override
    public void setRecyclerView(ArrayList<String> btnList) {
        ArrayList<Integer> iconArray = new ArrayList<>();
        iconArray.add(R.drawable.flag_top);
        iconArray.add(R.drawable.bed);
        iconArray.add(R.drawable.apply);
        iconArray.add(R.drawable.weather);
        iconArray.add(R.drawable.add_user);

        adapter = new MemberRecyclerViewAdapter(iconArray, btnList, this);
        currentUser = mAuth.getCurrentUser();
        adapter.setCurrent(currentUser);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new MemberRecyclerViewAdapter.OnMemberListItemClickListener() {
            @Override
            public void onClick(int itemPosition) {
                presenter.onListItemClickListener(itemPosition);
            }
        });

    }
}
