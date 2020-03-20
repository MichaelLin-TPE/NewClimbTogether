package com.hiking.climbtogether.mountain_collection_activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
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

import com.hiking.climbtogether.R;
import com.hiking.climbtogether.detail_activity.DetailActivity;
import com.hiking.climbtogether.home_fragment.weather_view.WeatherDialogAdapter;
import com.hiking.climbtogether.login_activity.LoginActivity;
import com.hiking.climbtogether.mountain_collection_activity.view.PortView;
import com.hiking.climbtogether.mountain_collection_activity.view_presenter.MtPresenter;
import com.hiking.climbtogether.mountain_collection_activity.view_presenter.MtPresenterImpl;
import com.hiking.climbtogether.db_modle.DataDTO;
import com.hiking.climbtogether.share_activity.ShareActivity;
import com.hiking.climbtogether.tool.SpaceItemDecoration;
import com.hiking.climbtogether.tool.UserDataManager;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.hiking.climbtogether.mountain_collection_activity.MountainCollectionPresenterImpl.MODIFY_TIME;
import static com.hiking.climbtogether.mountain_collection_activity.MountainCollectionPresenterImpl.REMOVE_DATA;
import static com.hiking.climbtogether.mountain_collection_activity.MountainCollectionPresenterImpl.SHARE_EXPERIENCE;
import static com.hiking.climbtogether.mountain_collection_activity.MountainCollectionPresenterImpl.UPLOAD;
import static com.hiking.climbtogether.mountain_collection_activity.MountainCollectionPresenterImpl.WATCH_INFORMATION;

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

    private TextView spinner;

    private StorageReference storageRef;

    private String userPreessedMtName;

    private int userPressedSid;

    private long userPressedTime;

    private long pickTime;

    private ArrayList<DataDTO> dataArrayList;

    private static final String COLLECTION_MOUNTAIN = "collection_mountain";

    private static final String COLLECTION = "collection";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mountain_collection);
        initFirebase();
        user = mAuth.getCurrentUser();
        initPresenter();
        initView();
        initSpinner();
        checkLoginStatus();
    }

    private void initSpinner() {
        spinner = findViewById(R.id.favorite_mt_spinner);
        presenter.onPrepareSpinnerData();
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

            firestore.collection(COLLECTION_MOUNTAIN)
                    .document(email)
                    .collection(COLLECTION)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null){
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

        this.dataArrayList = dataArrayList;

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
            public void onClick(DataDTO dataDTO,int position) {
                presenter.onItemClickListener(dataDTO,position);
            }
        });
    }

    @Override
    public void showProgressbar(boolean isShow) {
        progressBar.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setSpinner(ArrayList<String> listArray) {

        userDataManager = new UserDataManager(this);
        presenter.onSpinnerItemSelectedListener(userDataManager.getCollectionViewStyle());
        spinner.setText(listArray.get(userDataManager.getCollectionViewStyle()));
        spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onShowSpinnerDialog(listArray);
            }
        });
    }

    @Override
    public void setViewType(int position) {
        mtPresenter.setViewType(position);
        checkLoginStatus();
    }

    @Override
    public void showItemAlertDialog(final DataDTO dataDTO, final int position) {
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
                presenter.onItemDialogClickListener(UPLOAD,dataDTO,position);
                dialog.dismiss();
            }
        });
        watchInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onItemDialogClickListener(WATCH_INFORMATION,dataDTO,position);
                dialog.dismiss();
            }
        });
        shareEx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onItemDialogClickListener(SHARE_EXPERIENCE,dataDTO,position);
                dialog.dismiss();
            }
        });
        modifyTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onItemDialogClickListener(MODIFY_TIME,dataDTO,position);
                dialog.dismiss();
            }
        });
        removeData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onItemDialogClickListener(REMOVE_DATA,dataDTO,position);
                dialog.dismiss();
            }
        });

    }

    @Override
    public void selectPhoto(String mtName) {
        this.userPreessedMtName = mtName;

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap;

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK && result != null) {
                Uri resultUri = result.getUri();
                try{
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),resultUri);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int quality = 30;
                    bitmap.compress(Bitmap.CompressFormat.JPEG,quality,baos);
                    byte[] bytes = baos.toByteArray();
                    String message = getString(R.string.please_wait);
                    presenter.onShowProgressToast(message);

                    uploadPhotoToStorage(bytes);

                }catch (Exception e){
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                if (result != null){
                    Exception error = result.getError();
                }
            }
        }
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
                firestore.collection(COLLECTION_MOUNTAIN).document(email)
                        .collection(COLLECTION)
                        .document(dataDTO.getName())
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
                        Log.i("Michael","沒有照片");
                    }
                });
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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
                firestore.collection(COLLECTION_MOUNTAIN).document(email)
                        .collection(COLLECTION)
                        .document(data.getName())
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
    public void changeRecyclerView(int position) {
        dataArrayList.remove(position);
        mtPresenter.setData(dataArrayList);
        if (adapter != null){
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showSpinnerDialog(ArrayList<String> listArray) {
        ArrayList<Integer> imageArray = new ArrayList<>();
        imageArray.add(R.drawable.port);
        imageArray.add(R.drawable.land);
        View view = View.inflate(this,R.layout.weather_spinner_dialog,null);
        RecyclerView recyclerView = view.findViewById(R.id.weather_dialog_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        WeatherDialogAdapter adapter = new WeatherDialogAdapter(this,listArray,imageArray);
        recyclerView.setAdapter(adapter);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view).create();
        dialog.show();
        adapter.setOnWeatherDialogItemClickListener(new WeatherDialogAdapter.OnWeatherDialogItemClickListener() {
            @Override
            public void onClick(String name, int position) {
                presenter.onSpinnerItemSelectedListener(position);
                spinner.setText(name);
                dialog.dismiss();
            }
        });
    }

    @Override
    public void intentToDetailActivity(DataDTO dataDTO) {
        Intent it = new Intent(this, DetailActivity.class);
        it.putExtra("data",dataDTO);
        startActivity(it);
    }

    @Override
    public void intentToShareActivity() {
        Intent it = new Intent(this, ShareActivity.class);
        startActivity(it);
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
        firestore.collection(COLLECTION_MOUNTAIN).document(email).collection(COLLECTION)
                .document(userPreessedMtName).set(map)
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
