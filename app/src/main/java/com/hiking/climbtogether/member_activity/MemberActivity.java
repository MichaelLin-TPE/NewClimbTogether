package com.hiking.climbtogether.member_activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hiking.climbtogether.R;
import com.hiking.climbtogether.friend_manager_activity.FriendManagerActivity;
import com.hiking.climbtogether.login_activity.LoginActivity;
import com.hiking.climbtogether.mountain_collection_activity.MountainCollectionActivity;
import com.hiking.climbtogether.my_equipment_activity.MyEquipmentActivity;
import com.hiking.climbtogether.tool.ImageLoaderManager;
import com.hiking.climbtogether.tool.UserDataManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
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

    private static final String INVITE_FRIEND = "invite_friend";

    private static final String INVITE = "invite";

    private String displayName;

    private int inviteCount;

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
        if (currentUser != null && currentUser.getEmail() != null) {
            presenter.onChangeView(false, "");
            firestore.collection("users")
                    .document(currentUser.getEmail())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                DocumentSnapshot snapshot = task.getResult();
                                userDataManager.saveUserData((String) snapshot.get("email"), (String) snapshot.get("displayName"), (String) snapshot.get("photoUrl"));
                                displayName = (String) snapshot.get("displayName");
                                presenter.onChangeView(false, displayName);
                            }
                        }
                    });

            //申請推播TOKEN
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                String token = task.getResult().getToken();

                                Log.i("Michael", "new Token : " + token);
                                userDataManager.saveNotificationToken(token);
                                updateUserToken(token);
                            }

                        }
                    });

        } else {
            presenter.onChangeView(true, displayName);
        }
    }

    private void updateUserToken(String token) {
        if (currentUser != null && currentUser.getEmail() != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("token", token);
            firestore.collection("users").document(currentUser.getEmail())
                    .set(map, SetOptions.merge());
        }
    }

    @Override
    public void changeView(boolean isShow, String displayName) {
        tvNotice.setVisibility(isShow ? View.VISIBLE : View.GONE);
        btnLogin.setVisibility(isShow ? View.VISIBLE : View.GONE);
        ivUserIcon.setVisibility(isShow ? View.GONE : View.VISIBLE);
        tvEmail.setVisibility(isShow ? View.GONE : View.VISIBLE);
        tvEmail.setText(isShow ? "" : String.format(Locale.getDefault(), "目前登入 : %s", displayName));
        if (signOut != null) {
            signOut.setVisible(!isShow);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        currentUser = mAuth.getCurrentUser();
        searchDataFromFirebase();
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
                    .requestIdToken(getResources().getString(R.string.client_id))
                    .requestEmail()
                    .build();
            if (signInClient == null) {
                signInClient = GoogleSignIn.getClient(this, gso);
            }
            mAuth.signOut();
            signInClient.revokeAccess().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    presenter.onChangeView(true, displayName);

                }
            });
            removeUserToken();

        }
    }

    private void removeUserToken() {
        if (currentUser != null && currentUser.getEmail() != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("token", "remove");
            firestore.collection("users")
                    .document(currentUser.getEmail())
                    .set(map, SetOptions.merge());
        }

    }

    @Override
    public void showConfirmSignOutDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(getString(R.string.confirm_to_sign_out))
                .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userDataManager.clearAllData();
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
        if (currentUser != null){
            Intent it = new Intent(this, MountainCollectionActivity.class);
            startActivity(it);
        }else {
            Intent it = new Intent(this, LoginActivity.class);
            startActivity(it);
        }

    }


    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void downloadUserPhoto() {
        if (currentUser != null) {
            StorageReference river = storage.child(currentUser.getEmail() + "/userPhoto/" + currentUser.getEmail() + ".jpg");
            river.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String url = uri.toString();
                    ivUserIcon.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageLoaderManager.setPhotoUrl(url, ivUserIcon);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //do nothing
                    e.printStackTrace();
                    Log.i("Michael", "沒照片");
                    Log.i("Michael", e.toString());
                    ivUserIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    ivUserIcon.setImageResource(R.drawable.empty_photo);
                }
            });
        }
    }

    @Override
    public void intentToFriendManagerActivity() {
        if (currentUser != null){
            Intent it = new Intent(this, FriendManagerActivity.class);
            startActivity(it);
        }else {
            Intent it = new Intent(this,LoginActivity.class);
            startActivity(it);
        }

    }

    @Override
    public void intentToMyEquipmentActivity() {
        if (currentUser != null){
            Intent it = new Intent(this, MyEquipmentActivity.class);
            startActivity(it);
        }else {
            Intent it = new Intent(this, LoginActivity.class);
            startActivity(it);
        }

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
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int quality = 80;
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                    byte[] bytes = baos.toByteArray();

                    String message = getString(R.string.please_wait);

                    presenter.onShowProgressToast(message);

                    uploadPhotoToStorage(bytes);

                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void uploadPhotoToStorage(final byte[] bytes) {
        if (currentUser != null) {
            final StorageReference river = storage.child(currentUser.getEmail() + "/userPhoto/" + currentUser.getEmail() + ".jpg");
            UploadTask task = river.putBytes(bytes);
            task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String message = getString(R.string.upload_success);
                    presenter.onShowProgressToast(message);
                    ivUserIcon.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
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
        if (currentUser != null && currentUser.getEmail() != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("photoUrl", photoUrl);
            UserDataManager userDataManager = new UserDataManager(this);
            if (!userDataManager.getToken().isEmpty()) {
                map.put("token", userDataManager.getToken());
            }
            firestore.collection("users").document(currentUser.getEmail())
                    .set(map, SetOptions.merge());
            firestore.collection("users").document(currentUser.getEmail())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                DocumentSnapshot snapshot = task.getResult();
                                userDataManager.saveUserData((String) snapshot.get("email"), (String) snapshot.get("displayName"), photoUrl);
                            }
                        }
                    });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);
        initFirebase();
        initPresenter();
        initView();
        searchDataFromFirebase();
        updateUI(currentUser);

    }
    private void searchDataFromFirebase() {
        Log.i("Michael","查詢邀請");
        currentUser = mAuth.getCurrentUser();
        final ArrayList<String> nameList = new ArrayList<>();
        if (currentUser != null && currentUser.getEmail() != null){
            firestore.collection(INVITE_FRIEND).document(currentUser.getEmail()).collection(INVITE).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                if (task.getResult() != null){
                                    for (QueryDocumentSnapshot document : task.getResult()){
                                        nameList.add(document.getId());
                                    }
                                    if (nameList.size() != 0){
                                        Log.i("Michael","有邀請");
                                        inviteCount = nameList.size();
                                    }else {
                                        Log.i("Michael","沒邀請");
                                        inviteCount = 0;
                                    }
                                    presenter.onShowRecycler();

                                }
                            }
                        }
                    });
        }else {
            presenter.onShowRecycler();
        }

    }

    private void initFirebase() {
        //初始化Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();
        imageLoaderManager = new ImageLoaderManager(this);
        userDataManager = new UserDataManager(this);
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
        iconArray.add(R.drawable.backpack);

        adapter = new MemberRecyclerViewAdapter(iconArray, btnList, this);
        adapter.setInviteCount(inviteCount);
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
