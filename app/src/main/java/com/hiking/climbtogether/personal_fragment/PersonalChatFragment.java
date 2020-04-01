package com.hiking.climbtogether.personal_fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.hiking.climbtogether.R;
import com.hiking.climbtogether.login_activity.LoginActivity;
import com.hiking.climbtogether.personal_chat_activity.PersonalChatActivity;
import com.hiking.climbtogether.tool.FireStoreManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class PersonalChatFragment extends Fragment implements PersonalFragmentVu {

    private PersonalFragmentPresenter presenter;

    private Context context;

    private RecyclerView recyclerView;

    private FirebaseAuth mAuth;

    private FirebaseFirestore firestore;

    private FireStoreManager manager;

    private FirebaseUser user;

    private ImageView ivLogo;

    private TextView tvNotice;

    private ProgressBar progressBar;

    private Button btnLogin;

    private PersonalFragmentAdapter adapter;

    private ArrayList<String> documentIdArray;

    private int itemPosition;

    private ArrayList<PersonalChatDTO> chatDataArrayList;


    private ArrayList<String> jsonArray;

    public static PersonalChatFragment newInstance() {
        PersonalChatFragment fragment = new PersonalChatFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        Log.i("Michael", "chat onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFirebase();
        initPresenter();
        Log.i("Michael", "chat onCreate");
    }

    private void initPresenter() {
        presenter = new PersonalFragmentPresenterImpl(this);
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        manager = new FireStoreManager();
        firestore = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("Michael", "chat onCreateView");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal_chat, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        progressBar = view.findViewById(R.id.personal_fragment_progressbar);
        recyclerView = view.findViewById(R.id.personal_fragment_recycler_view);
        btnLogin = view.findViewById(R.id.personal_fragment_btn_login);
        tvNotice = view.findViewById(R.id.personal_fragment_text_notice);
        ivLogo = view.findViewById(R.id.personal_fragment_iv_icon);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onLoginClickListener();
            }
        });
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("Michael", "chat onActivityCreated");

    }

    private void searchData() {
        user = mAuth.getCurrentUser();
        if (user != null && user.getEmail() != null) {

            presenter.onShowProgress(true);
            jsonArray = new ArrayList<>();
            documentIdArray = new ArrayList<>();

            firestore.collection("chat_data")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                    String jsonStr = (String) snapshot.get("json");
                                    String path = snapshot.getId();
                                    if (jsonStr != null) {
                                        jsonArray.add(jsonStr);
                                        documentIdArray.add(path);
                                    }
                                }
                                presenter.onCatchallData(jsonArray,documentIdArray);
                            }
                        }
                    });
        } else {
            presenter.onNoUserEvent();
        }
    }

    @Override
    public void showLoginInformation(boolean isShow) {
        ivLogo.setVisibility(isShow ? View.VISIBLE : View.GONE);
        tvNotice.setVisibility(isShow ? View.VISIBLE : View.GONE);
        tvNotice.setText(getString(R.string.login_notice_chat));
        btnLogin.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void intentToLoginActivity() {
        Intent it = new Intent(context, LoginActivity.class);
        context.startActivity(it);
    }

    @Override
    public void showProgress(boolean isShow) {
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
    public void setRecyclerView(ArrayList<PersonalChatDTO> chatDataArray) {
        if (getActivity() != null){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    chatDataArrayList = chatDataArray;

                    if (adapter != null){
                        adapter.setData(chatDataArrayList);
                        adapter.notifyDataSetChanged();
                    }

                    adapter = new PersonalFragmentAdapter(context);
                    adapter.setData(chatDataArrayList);
                    recyclerView.setAdapter(adapter);
                    adapter.setOnChatItemClickListener(new PersonalFragmentAdapter.OnChatItemClickListener() {
                        @Override
                        public void onClick(String displayName, String friendEmail, String photoUrl, int position, String documentPath) {
                            presenter.onItemClickListener(displayName, friendEmail, photoUrl,documentPath);
                        }
                    });
                    adapter.setOnChatItemLongClickListener(new PersonalFragmentAdapter.OnChatItemLongClickListener() {
                        @Override
                        public void onClick(String documentPath, int itemPosition1) {
                            itemPosition = itemPosition1;
                            presenter.onShowDeleteMessageConfirmDialog(documentPath);
                        }
                    });
                }
            });
        }

    }

    @Override
    public void intentToPersonalChatActivity(String displayName, String friendEmail, String
            photoUrl, String path) {
        Intent it = new Intent(context, PersonalChatActivity.class);
        it.putExtra("displayName", displayName);
        it.putExtra("mail", friendEmail);
        it.putExtra("photoUrl", photoUrl);
        it.putExtra("path",path);
        context.startActivity(it);
    }

    @Override
    public void showNoChatDataView(boolean isShow) {
        if (getActivity() != null){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ivLogo.setVisibility(isShow ? View.VISIBLE : View.GONE);
                    tvNotice.setVisibility(isShow ? View.VISIBLE : View.GONE);
                    tvNotice.setText(isShow ? getString(R.string.no_chat_data) : "");
                }
            });
        }
    }

    @Override
    public void showDeleteConfirmDialog(String documentPath) {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.warning))
                .setMessage(getString(R.string.confirm_to_delete_message))
                .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        chatDataArrayList.remove(itemPosition);
                        adapter.notifyDataSetChanged();
                        deleteMessage();
                    }
                }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        dialog.show();
    }

    private void deleteMessage() {
        firestore.collection("chat_data")
                .document(documentIdArray.get(itemPosition))
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.i("Michael","刪除成功");
                            documentIdArray.remove(itemPosition);
                        }
                    }
                });
    }

    @Override
    public Context getVuContext() {
        return context;
    }

    @Override
    public String getUserEmail() {
        return user.getEmail();
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i("Michael", "chat OnResume");
        user = mAuth.getCurrentUser();
        if (user != null) {
            Log.i("Michael", "有用戶");

            firestore.collection("chat_data")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                            if (e != null){
                                Log.i("Michael","Listen failed : "+e);
                                return;
                            }
                            if (value != null){
                                jsonArray = new ArrayList<>();
                                documentIdArray = new ArrayList<>();
                                for (QueryDocumentSnapshot snapshot : value){
                                    if (snapshot.get("json") != null){
                                        Log.i("Michael","監聽器json : "+snapshot.get("json"));
                                        String jsonStr = (String) snapshot.get("json");
                                        String path = snapshot.getId();
                                        if (jsonStr != null) {
                                            jsonArray.add(jsonStr);
                                            documentIdArray.add(path);
                                        }
                                    }
                                }
                                presenter.onCatchallData(jsonArray,documentIdArray);
                            }
                        }
                    });

        } else {
            if (adapter != null) {
                adapter = new PersonalFragmentAdapter(context);
                adapter.setData(new ArrayList<PersonalChatDTO>());
                recyclerView.setAdapter(adapter);
            }
            presenter.onNoUserEvent();
            Log.i("Michael", "沒用戶");
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.i("Michael", "chat onPause");
        if (adapter != null) {
            adapter = new PersonalFragmentAdapter(context);
            adapter.setData(new ArrayList<PersonalChatDTO>());
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("Michael", "chat onStop");
    }


}
