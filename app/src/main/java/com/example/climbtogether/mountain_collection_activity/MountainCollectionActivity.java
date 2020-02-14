package com.example.climbtogether.mountain_collection_activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
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
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.climbtogether.R;
import com.example.climbtogether.login_activity.LoginActivity;
import com.example.climbtogether.mountain_collection_activity.view.PortView;
import com.example.climbtogether.mountain_collection_activity.view_presenter.MtPresenter;
import com.example.climbtogether.mountain_collection_activity.view_presenter.MtPresenterImpl;
import com.example.climbtogether.mountain_fragment.db_modle.DataDTO;
import com.example.climbtogether.tool.SpaceItemDecoration;
import com.example.climbtogether.tool.UserDataManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.example.climbtogether.mountain_collection_activity.MountainCollectionPresenterImpl.MODIFY_TIME;
import static com.example.climbtogether.mountain_collection_activity.MountainCollectionPresenterImpl.REMOVE_DATA;
import static com.example.climbtogether.mountain_collection_activity.MountainCollectionPresenterImpl.SHARE_EXPERIENCE;
import static com.example.climbtogether.mountain_collection_activity.MountainCollectionPresenterImpl.UPLOAD;
import static com.example.climbtogether.mountain_collection_activity.MountainCollectionPresenterImpl.WATCH_INFORMATION;

public class MountainCollectionActivity extends AppCompatActivity implements MountainCollectionVu{

    private MountainCollectionPresenter presenter;

    private RecyclerView recyclerView;

    private UserDataManager userDataManager;

    private FirebaseAuth mAuth;

    private FirebaseFirestore firestore;

    private TextView tvNotice,tvSpinnerNotice;

    private ImageView ivIcon,ivDropDown;

    private Button btnLogin;

    private FirebaseUser user;

    private MtPresenter mtPresenter;

    private ProgressBar progressBar;

    private MountainCollectionAdapter adapter;

    private Spinner spinner;

    private static final int IMAGE_REQUEST_CODE = 2;

    private StorageReference storageRef;

    private String userPreessedMtName;

    private int userPressedSid;

    private long userPressedTime;

    private long pickTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mountain_collection);
        initFirebase();
        initPresenter();
        initView();
        initSpinner();
    }

    private void initSpinner() {
        spinner = findViewById(R.id.favorite_mt_spinner);
        presenter.onPrepareSpinnerData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("Michael","collection onStart");
        user = mAuth.getCurrentUser();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Michael","collection onResume");
        checkLoginStatus();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (user != null){
            if (adapter != null){
                mtPresenter.setData(new ArrayList<DataDTO>());
                adapter.notifyDataSetChanged();
            }
        }
        Log.i("Michael","collection onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Michael","collection onStop");
    }

    private void checkLoginStatus() {
        if (user != null){
            presenter.onUserExist();
        }else {
            presenter.onUserNotExist();
        }
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        presenter.onBackPressedListener();
    }

    private void initView() {
        tvSpinnerNotice = findViewById(R.id.favorite_mt_choose_mode);
        ivDropDown = findViewById(R.id.favorite_mt_dropdown);
        progressBar = findViewById(R.id.favorite_mt_progressbar);
        Toolbar toolbar = findViewById(R.id.favorite_mt_toolbar);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onToolbarNavigationIconClickListener();
            }
        });
        tvNotice = findViewById(R.id.favorite_mt_text_notice);
        ivIcon = findViewById(R.id.favorite_mt_iv_icon);
        btnLogin = findViewById(R.id.favorite_mt_btn_login);
        recyclerView = findViewById(R.id.favorite_mt_recycler_view);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onBtnLoginClickListener();
            }
        });

        //轉DP
        int pix = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,20,getResources().getDisplayMetrics());

        recyclerView.addItemDecoration(new SpaceItemDecoration(pix));

    }

    private void initPresenter() {
        presenter = new MountainCollectionPresenterImpl(this);
        mtPresenter = new MtPresenterImpl();
    }

    @Override
    public void closePage() {
        finish();
    }

    @Override
    public void setViewMaintain(boolean isShow) {
        tvNotice.setVisibility(isShow ? View.VISIBLE : View.GONE);
        ivIcon.setVisibility(isShow ? View.VISIBLE : View.GONE);
        btnLogin.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void intentToLoginActivity() {
        Intent it = new Intent(this, LoginActivity.class);
        startActivity(it);
    }

    @Override
    public void searchDataFromFirebase() {
        final ArrayList<String> mtNameArray = new ArrayList<>();
        final ArrayList<Long> timeArray = new ArrayList<>();
        final ArrayList<String> downloadUrlArray = new ArrayList<>();

        String email = user.getEmail();
        if (email != null){
            firestore.collection(email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        if (task.getResult() != null){
                            for (QueryDocumentSnapshot snapshot : task.getResult()){
                                mtNameArray.add(snapshot.getId());
                                Map<String,Object> map = snapshot.getData();
                                timeArray.add((Long) map.get("topTime"));
                                downloadUrlArray.add((String) map.get("photoUrl"));
                            }
                            if (mtNameArray.size() != 0 && timeArray.size() != 0 && downloadUrlArray.size() != 0){
                                presenter.onSearchDataSuccessFromFirebase(mtNameArray,timeArray,downloadUrlArray);
                            }else {
                                presenter.onSearchNoDataFromFirebase();
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    public void showSearchNoDataView() {
        //這裡是如果沒有資料的話
        ivIcon.setVisibility(View.VISIBLE);
        tvNotice.setVisibility(View.VISIBLE);
        tvSpinnerNotice.setVisibility(View.GONE);
        ivDropDown.setVisibility(View.GONE);
        spinner.setVisibility(View.GONE);
        tvNotice.setText(getString(R.string.on_favorite_data));
    }

    @Override
    public Context getVuContext() {
        return this;
    }

    @Override
    public void setRecyclerViewData(final ArrayList<DataDTO> dataArrayList) {

        mtPresenter.setData(dataArrayList);

        adapter = new MountainCollectionAdapter(this,mtPresenter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mtPresenter.getSpanCount();
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnPortViewItemClickListener(new PortView.OnPortViewItemClickListener() {
            @Override
            public void onClick(DataDTO dataDTO) {
                presenter.onItemClickListener(dataDTO);
            }
        });
    }

    @Override
    public void showProgressbar(boolean isShow) {
        progressBar.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setSpinner(ArrayList<String> listArray) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.spinner_custom_layout,listArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(arrayAdapter);
        userDataManager = new UserDataManager(this);
        spinner.setSelection(userDataManager.getCollectionViewStyle());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presenter.onSpinnerItemSelectedListener(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void setViewType(int position) {
        mtPresenter.setViewType(position);
        checkLoginStatus();
    }

    @Override
    public void showItemAlertDialog(final DataDTO dataDTO) {
        this.userPressedSid = dataDTO.getSid();
        this.userPressedTime = dataDTO.getTime();

        View view = View.inflate(this,R.layout.dialog_custom_view_collection_item,null);
        TextView title = view.findViewById(R.id.collection_dialog_item_chosen);
        TextView upload = view.findViewById(R.id.collection_dialog_item_upload_photo);
        TextView watchInfo = view.findViewById(R.id.collection_dialog_item_watch_information);
        TextView shareEx = view.findViewById(R.id.collection_dialog_item_share_experience);
        TextView modifyTime = view.findViewById(R.id.collection_dialog_item_modify_time);
        TextView removeData = view.findViewById(R.id.collection_dialog_item_remove);

        title.setText(String.format(Locale.getDefault(),"您選擇了 : %s",dataDTO.getName()));

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view).create();
        dialog.show();
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onItemDialogClickListener(UPLOAD,dataDTO);
                dialog.dismiss();
            }
        });
        watchInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onItemDialogClickListener(WATCH_INFORMATION,dataDTO);
                dialog.dismiss();
            }
        });
        shareEx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onItemDialogClickListener(SHARE_EXPERIENCE,dataDTO);
                dialog.dismiss();
            }
        });
        modifyTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onItemDialogClickListener(MODIFY_TIME,dataDTO);
                dialog.dismiss();
            }
        });
        removeData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onItemDialogClickListener(REMOVE_DATA,dataDTO);
                dialog.dismiss();
            }
        });

    }

    @Override
    public void selectPhoto(String mtName) {
        this.userPreessedMtName = mtName;
        Intent it = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(it,IMAGE_REQUEST_CODE);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void saveViewType(int position) {
        userDataManager.saveCollectionViewStyle(position);
    }

    @Override
    public void removeData(DataDTO dataDTO) {
        if (user != null){
            String email = user.getEmail();
            if (email != null){
                firestore.collection(user.getEmail()).document(dataDTO.getName())
                        .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            String message = getString(R.string.delete_success);
                            presenter.onShowProgressToast(message);
                        }else {
                            String message = getString(R.string.delete_fail);
                            presenter.onShowProgressToast(message);
                        }
                    }
                });
                StorageReference river = storageRef.child(email+"/"+dataDTO.getName()+"/"+dataDTO.getName()+".jpg");
                river.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        String message = getString(R.string.delete_success);
                        presenter.onShowProgressToast(message);
                        checkLoginStatus();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String message = getString(R.string.delete_fail);
                        presenter.onShowProgressToast(message);
                    }
                });
            }
        }

    }

    @Override
    public void showDatePickerDialog(final DataDTO dataDTO) {

        DatePicker datePicker = new DatePicker(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(datePicker)
                .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (pickTime == 0){
                            pickTime = System.currentTimeMillis();
                        }
                        presenter.onDatePickerDialogClickListener(dataDTO,pickTime);
                    }
                }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        dialog.show();
        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String time = year+"/"+(monthOfYear+1)+"/"+dayOfMonth;
                try {
                    pickTime = new SimpleDateFormat("yyyy/MM/dd",Locale.TAIWAN).parse(time).getTime();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void modifyFirestoreData(DataDTO data, long pickTime) {
        if (user != null){
            String message = getString(R.string.please_wait_progressing);
            presenter.onShowProgressToast(message);
            String email = user.getEmail();
            Map<String,Object> map = new HashMap<>();
            map.put("name", data.getName());
            map.put("topTime", data.getTime());
            map.put("sid", data.getSid());
            map.put("photoUrl",data.getUserPhoto());
            if (email != null){
                firestore.collection(email).document(data.getName())
                        .set(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    checkLoginStatus();
                                }
                            }
                        });
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap;

        if (requestCode == IMAGE_REQUEST_CODE){
            try{
                Log.i("Michael","壓縮照片");
                if (data != null){
                    Uri uri = data.getData();
                    ContentResolver or = this.getContentResolver();
                    if (uri != null){
                        bitmap = BitmapFactory.decodeStream(or.openInputStream(uri));
                        Log.i("Michael","取得的照片 : "+bitmap);
                        //壓縮照片
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        int quality = 10;
                        bitmap.compress(Bitmap.CompressFormat.JPEG,quality,baos);
                        byte[] bytes = baos.toByteArray();
                        bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                        Log.i("Michael","壓縮後的圖片大小 : "+ (bitmap.getByteCount() / 1024 /1024 )+" , 寬度 : "+bitmap.getWidth()
                                + " , 高度為 : "+bitmap.getHeight() + " , bytes 長度 : "
                                + (bytes.length / 1024 ) + " kb "+ "quality = " + quality);
                        //Uri 轉 Bitmap
//                            uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(),bitmap,null,null));
                        String message = getString(R.string.please_wait);
                        presenter.onShowProgressToast(message);

                        uploadPhotoToStorage(bytes);


                    }else {
                        Log.i("Michael","photo uri = null");
                    }
                }else {
                    Log.i("Michael"," data = null");
                }


            }catch (Exception e){
                e.printStackTrace();
                Log.i("Michael","壓縮照片錯誤");
            }
        }
    }

    private void uploadPhotoToStorage(byte[] bytes) {
        if(user != null){
            final String email = user.getEmail();
            final StorageReference riverRef = storageRef.child(email+"/"+userPreessedMtName+"/"+userPreessedMtName+".jpg");
            UploadTask uploadTask = riverRef.putBytes(bytes);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    riverRef.getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadUrl = uri.toString();
                                    Log.i("Michael","照片上傳成功 : "+downloadUrl);
                                    modifyFirestoreData(downloadUrl,email);
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

    private void modifyFirestoreData(String downloadUrl,String email) {
        Map<String,Object> map = new HashMap<>();
        map.put("name", userPreessedMtName);
        map.put("topTime", userPressedTime);
        map.put("sid", userPressedSid);
        map.put("photoUrl",downloadUrl);
        firestore.collection(email).document(userPreessedMtName).set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.i("Michael","網址新增成功");
                            checkLoginStatus();
                        }
                    }
                });
    }
}
