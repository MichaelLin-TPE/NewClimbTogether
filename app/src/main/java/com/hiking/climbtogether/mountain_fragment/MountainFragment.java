package com.hiking.climbtogether.mountain_fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hiking.climbtogether.R;
import com.hiking.climbtogether.detail_activity.DetailActivity;
import com.hiking.climbtogether.login_activity.LoginActivity;
import com.hiking.climbtogether.db_modle.DataDTO;
import com.hiking.climbtogether.tool.FireStoreManager;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.hiking.climbtogether.tool.Constant.ALL;
import static com.hiking.climbtogether.tool.Constant.LEVEL_A;
import static com.hiking.climbtogether.tool.Constant.LEVEL_B;
import static com.hiking.climbtogether.tool.Constant.LEVEL_C;
import static com.hiking.climbtogether.tool.Constant.LEVEL_C_PLUS;
import static com.hiking.climbtogether.tool.Constant.LEVEL_D;
import static com.hiking.climbtogether.tool.Constant.LEVEL_E;


public class MountainFragment extends Fragment implements MountainFragmentVu {

    private TextView tvAll, tvLevelA, tvLevelB, tvLevelC, tvLevelCpuls, tvLevelD, tvLevelE, tvWatchMore, tvSearchNoData;

    private RecyclerView recyclerView;

    private MountainFragmentPresenter presenter;

    private Context context;

    private MountainRecyclerViewAdapter adapter;

    private DatePicker datePicker;

    private FirebaseAuth mAuth;

    private FirebaseUser user;

    private String topTime;

    private ArrayList<Long> timeArray;

    private ArrayList<String> firestoreData;

    private ProgressBar progressBar;

    private Spinner spinner;

    private FireStoreManager manager;

    public static MountainFragment newInstance() {
        MountainFragment fragment = new MountainFragment();

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        Log.i("Michael","Mt onAttach");
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Michael","Mt onCreate");
        mAuth = FirebaseAuth.getInstance();
        adapter = new MountainRecyclerViewAdapter(context);
        manager = new FireStoreManager();
        initPresenter();

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("Michael","Mt onStart");
        user = mAuth.getCurrentUser();

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("Michael","Mt onPause");
        adapter.setData(new ArrayList<DataDTO>());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("Michael","Mt onStop");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("Michael","Mt onResume");
        user = mAuth.getCurrentUser();
        if (user != null){
            Log.i("Michael","onResume 有用戶");
            String email = user.getEmail();
            presenter.onSearchDbData(email);
        }else {
            Log.i("Michael","onResume 沒用戶");
            presenter.initDbData();
        }
    }

    private void initDbData() {
        user = mAuth.getCurrentUser();
        if (user != null){
            String email = user.getEmail();
            Log.i("Michael","有使用者 : "+email);
            presenter.onSearchDbData(email);
        }else {
            Log.i("Michael","無使用者");
            presenter.initDbData();
        }
    }
    @Override
    public void searchDataFromDb(String email, final ArrayList<DataDTO> allInformation) {

        if (getActivity() != null){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("Michael","searchDataFromDb");
                    firestoreData = new ArrayList<>();
                    timeArray = new ArrayList<>();
                    manager.setFirstCollection(email);
                    manager.catchOneCollectionData(new FireStoreManager.OnConnectingFirebaseListener() {
                        @Override
                        public void onSuccess(Task<QuerySnapshot> task) {
                            if (getActivity() != null){
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (task.isSuccessful() && task.getResult() != null ){
                                            for (QueryDocumentSnapshot document : task.getResult()){
                                                Log.i("Michael",document.getId() + " =>" + document.getData());
                                                Map<String,Object> map = document.getData();
                                                Log.i("Michael","time : "+map.get("topTime"));
                                                timeArray.add((Long) map.get("topTime"));
                                                firestoreData.add(document.getId());
                                            }
                                            if (firestoreData.size() != 0 && timeArray.size() != 0){
                                                Log.i("Michael","有資料的Email做到這 : "+firestoreData.size());
                                                presenter.onModifyDataFromFirestore(firestoreData,timeArray,allInformation);
                                            }else {
                                                presenter.initDbData();
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            });
        }
    }

    @Override
    public void readyToProvideData() {
        if (getActivity() != null){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    presenter.onPrepareData();
                }
            });
        }

    }

    @Override
    public void showProgressbar(boolean isShow) {
        if (getActivity() != null){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(isShow ? View.VISIBLE : View.GONE);
                }
            });
        }
    }

    @Override
    public void setSpinner(ArrayList<String> spinnerData) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context,R.layout.spinner_custom_layout,spinnerData);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                presenter.onSpinnerSelectListener(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void changeRecyclerViewSor(ArrayList<DataDTO> allInformation) {
        adapter.setData(allInformation);
        adapter.notifyDataSetChanged();
    }

    private void initPresenter() {
        presenter = new MountainFragmentPresentImpl(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i("Michael","Mt onCreateView");
        View view = inflater.inflate(R.layout.fragment_mountain, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("Michael","Mt onActivityCreated");
        initDbData();

    }

    private void initView(View view) {
        spinner = view.findViewById(R.id.mountain_fragment_spinner);
        progressBar = view.findViewById(R.id.mountain_fragment_progressbar);
        tvAll = view.findViewById(R.id.mountain_fragment_all);
        tvLevelA = view.findViewById(R.id.mountain_fragment_a);
        tvLevelB = view.findViewById(R.id.mountain_fragment_b);
        tvLevelC = view.findViewById(R.id.mountain_fragment_c);
        tvLevelCpuls = view.findViewById(R.id.mountain_fragment_c_plus);
        tvLevelD = view.findViewById(R.id.mountain_fragment_d);
        tvLevelE = view.findViewById(R.id.mountain_fragment_e);
        tvWatchMore = view.findViewById(R.id.mountain_fragment_watch_more);
        recyclerView = view.findViewById(R.id.mountain_fragment_recycler_view);
        tvSearchNoData = view.findViewById(R.id.mountain_fragment_search_no_data);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        tvAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onFilterTextViewBackgroundChangeListener(ALL, tvAll.getText().toString());
            }
        });
        tvLevelA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onFilterTextViewBackgroundChangeListener(LEVEL_A, tvLevelA.getText().toString());
            }
        });
        tvLevelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onFilterTextViewBackgroundChangeListener(LEVEL_B, tvLevelB.getText().toString());
            }
        });
        tvLevelC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onFilterTextViewBackgroundChangeListener(LEVEL_C, tvLevelC.getText().toString());
            }
        });
        tvLevelCpuls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onFilterTextViewBackgroundChangeListener(LEVEL_C_PLUS, tvLevelCpuls.getText().toString());
            }
        });
        tvLevelD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onFilterTextViewBackgroundChangeListener(LEVEL_D, tvLevelD.getText().toString());
            }
        });
        tvLevelE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onFilterTextViewBackgroundChangeListener(LEVEL_E, tvLevelE.getText().toString());
            }
        });
        tvWatchMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onWatchMoreClickListener();
            }
        });
    }


    @Override
    public void showAllInformation() {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setMessage(context.getResources().getString(R.string.mountain_notice))
                .setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create();
        dialog.show();
    }

    @Override
    public void showTextViewBackgroundChange(int textType) {
        tvAll.setBackground(textType == ALL ? ContextCompat.getDrawable(context, R.drawable.text_shapte) : null);
        tvLevelA.setBackground(textType == LEVEL_A ? ContextCompat.getDrawable(context, R.drawable.text_shapte) : null);
        tvLevelB.setBackground(textType == LEVEL_B ? ContextCompat.getDrawable(context, R.drawable.text_shapte) : null);
        tvLevelC.setBackground(textType == LEVEL_C ? ContextCompat.getDrawable(context, R.drawable.text_shapte) : null);
        tvLevelCpuls.setBackground(textType == LEVEL_C_PLUS ? ContextCompat.getDrawable(context, R.drawable.text_shapte) : null);
        tvLevelD.setBackground(textType == LEVEL_D ? ContextCompat.getDrawable(context, R.drawable.text_shapte) : null);
        tvLevelE.setBackground(textType == LEVEL_E ? ContextCompat.getDrawable(context, R.drawable.text_shapte) : null);
    }

    @Override
    public Context getVuContext() {
        return context;
    }

    @Override
    public void setRecyclerView(ArrayList<DataDTO> allInformation) {

        adapter.setData(allInformation);
        recyclerView.setAdapter(adapter);
        adapter.setOnMountainItemClickListener(new MountainRecyclerViewAdapter.OnMountainItemClickListener() {
            @Override
            public void onClick(DataDTO data) {
                presenter.onMountainItemClick(data);
            }

            @Override
            public void onIconClick(int sid) {
                user = mAuth.getCurrentUser();
                if (user != null) {
                    presenter.onShowDatePicker(sid);
                } else {
                    presenter.onLoginEvent();
                }


            }
        });
        presenter.onPrepareSpinnerData();

    }

    @Override
    public void showSearchNoDataInformation(boolean isShow) {
        tvSearchNoData.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setDataChange(ArrayList<DataDTO> dataDTO,String isSHow) {
        adapter.setData(dataDTO);
        adapter.notifyDataSetChanged();
        Toast.makeText(context, isSHow.equals("true") ? "此筆資料已加入我的戰績" : "已從我的戰績移除", Toast.LENGTH_LONG).show();
    }

    @Override
    public void intentToMtDetailActivity(DataDTO data) {
        Intent it = new Intent(context ,DetailActivity.class);
        it.putExtra("data",data);
        context.startActivity(it);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void showDatePick(final int sid) {
        datePicker = new DatePicker(context);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(datePicker)
                .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i("Michael", "時間為 : " + topTime);
                        if (topTime == null || topTime.isEmpty()){
                            topTime = new SimpleDateFormat("yyyy/MM/dd",Locale.TAIWAN).format(new Date(System.currentTimeMillis()));
                        }
                        String isShow = "true";
                        presenter.onTopIconChange(sid,isShow,topTime);
                        presenter.onCreateDocumentInFirestore(sid, topTime);


                    }
                }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create();
        dialog.show();

        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int date) {
                String monthStr;
                String yearStr;
                String dateStr;
                if ((month + 1) < 10) {
                    monthStr = "0" + (month + 1);
                } else {
                    monthStr = (month + 1) + "";
                }
                if ((date) < 10) {
                    dateStr = "0" + date;
                } else {
                    dateStr = date + "";
                }
                yearStr = year + "";
                topTime = String.format(Locale.getDefault(), "%s/%s/%s", yearStr, monthStr, dateStr);
            }
        });
    }

    @Override
    public void setFirestore(final int sid, long topTime, DataDTO data) {
        user = mAuth.getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            String mountainName = data.getName();
            if (email != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("name", data.getName());
                map.put("topTime", topTime);
                map.put("sid", data.getSid());
                map.put("photoUrl","");
                manager.setFirstCollection(email);
                manager.setFirstDocument(mountainName);
                manager.setMap(map);
                manager.setDocumentData(new FireStoreManager.OnFirebaseSetDocumentListener() {
                    @Override
                    public void onSuccessful() {
                        if (getActivity() != null){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.i("Michael","資料增加成功");
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure() {

                    }
                });
//                firestore.collection(email).document(mountainName)
//                        .set(map)
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if (task.isSuccessful()) {
//
//                                }
//                            }
//                        });


            }
        }
    }

    @Override
    public void intentToLoginActivity() {
        Intent it = new Intent(context, LoginActivity.class);
        context.startActivity(it);
    }

    @Override
    public void deleteFavorite(final int sid, DataDTO dataDTO) {
        user = mAuth.getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            if (email != null) {
                manager.setFirstCollection(email);
                manager.setFirstDocument(dataDTO.getName());
                manager.deleteDocument(new FireStoreManager.OnFirebaseDeleteListener() {
                    @Override
                    public void onSuccessful() {
                        String isShow = "false";
                        presenter.onTopIconChange(sid,isShow,null);
                    }

                    @Override
                    public void onFailure() {
                        Log.i("Michael","刪除失敗");
                    }
                });
            }
        }
    }




}