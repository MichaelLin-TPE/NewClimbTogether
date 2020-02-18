package com.example.climbtogether.personal_fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import com.example.climbtogether.R;
import com.example.climbtogether.login_activity.LoginActivity;
import com.example.climbtogether.personal_chat_activity.PersonalChatActivity;
import com.example.climbtogether.personal_chat_activity.PersonalChatData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nostra13.universalimageloader.utils.L;

import java.util.ArrayList;


public class PersonalChatFragment extends Fragment implements PersonalFragmentVu {

    private PersonalFragmentPresenter presenter;

    private Context context;

    private RecyclerView recyclerView;

    private FirebaseAuth mAuth;

    private FirebaseFirestore firestore;

    private StorageReference storage;

    private FirebaseUser user;

    private ImageView ivLogo;

    private TextView tvNotice;

    private ProgressBar progressBar;

    private Button btnLogin;

    private static final String PERSONAL_CHAT = "personal_chat";

    private static final String CHAT_DATA = "chat_data";

    private static final String FRIENDSHIP = "friendship";

    private static final String FRIEND = "friend";

    private int searchCount = 0;

    private ArrayList<PersonalChatDTO> chatDataArrayList;
    
    private ArrayList<String> friendsArrayList;
    
    private String currentEmail;

    private PersonalFragmentAdapter adapter;

    public static PersonalChatFragment newInstance() {
        PersonalChatFragment fragment = new PersonalChatFragment();
        Bundle args = new Bundle();
        return fragment;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        Log.i("Michael","chat onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFirebase();
        initPresenter();
        Log.i("Michael","chat onCreate");
    }

    private void initPresenter() {
        presenter = new PersonalFragmentPresenterImpl(this);
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance().getReference();
        user = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("Michael","chat onCreateView");
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
        Log.i("Michael","chat onActivityCreated");
        searchData();
    }

    private void searchData() {
        user = mAuth.getCurrentUser();
        if (user != null && user.getEmail() != null){
            presenter.onShowProgress(true);
            final ArrayList<String> friendsArrayList = new ArrayList<>();
            firestore.collection(FRIENDSHIP)
                    .document(user.getEmail())
                    .collection(FRIEND)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null){
                                for (QueryDocumentSnapshot document : task.getResult()){
                                    friendsArrayList.add(document.getId());
                                    Log.i("Michael",document.getId());
                                }
                                presenter.onSearchUserChatData(user.getEmail(),friendsArrayList);
                            }
                        }
                    });
        }else {
            presenter.onNoUserEvent();
        }
    }

    @Override
    public void showLoginInformation(boolean isShow) {
        ivLogo.setVisibility(isShow ? View.VISIBLE : View.GONE);
        tvNotice.setVisibility(isShow ? View.VISIBLE : View.GONE);
        btnLogin.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void intentToLoginActivity() {
        Intent it = new Intent(context, LoginActivity.class);
        context.startActivity(it);
    }

    @Override
    public void showProgress(boolean isShow) {
        progressBar.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void searchForChatData(final String currentEmail, final ArrayList<String> friendsArrayList) {
        this.friendsArrayList = friendsArrayList;
        this.currentEmail = currentEmail;
        chatDataArrayList = new ArrayList<>();
        searchAllData();
    }

    @Override
    public void setRecyclerView(ArrayList<PersonalChatDTO> chatDataArrayList) {
        adapter = new PersonalFragmentAdapter(context,chatDataArrayList);
        recyclerView.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        adapter.setOnChatItemClickListener(new PersonalFragmentAdapter.OnChatItemClickListener() {
            @Override
            public void onClick(String displayName, String friendEmail, String photoUrl) {
                presenter.onItemClickListener(displayName,friendEmail,photoUrl);
            }
        });
    }

    @Override
    public void intentToPersonalChatActivity(String displayName, String friendEmail, String photoUrl) {
        Intent it = new Intent(context, PersonalChatActivity.class);
        it.putExtra("displayName",displayName);
        it.putExtra("mail",friendEmail);
        it.putExtra("photoUrl",photoUrl);
        context.startActivity(it);
    }

    @Override
    public void showNoChatDataView(boolean isShow) {
        ivLogo.setVisibility(isShow ? View.VISIBLE : View.GONE);
        tvNotice.setVisibility(isShow ? View.VISIBLE : View.GONE);
        tvNotice.setText(isShow ? getString(R.string.no_chat_data) : "");
    }

    private void searchAllData() {
        if (searchCount < friendsArrayList.size()){
            Log.i("Michael","朋友有幾個 : "+friendsArrayList.size());
            searchFriendsData(friendsArrayList.get(searchCount),currentEmail);
        }else {
            Log.i("Michael","所有資料讀取完畢");
            presenter.onCatchAllDataSucessful(chatDataArrayList);
        }
    }

    private void searchFriendsData(final String friendEmail, final String currentEmail) {
        firestore.collection("users").document(friendEmail)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null){
                    DocumentSnapshot snapshot =  task.getResult();
                    PersonalChatDTO data = new PersonalChatDTO();
                    data.setDisplayName((String)snapshot.get("displayName"));
                    data.setFriendEmail((String)snapshot.get("email"));
                    data.setPhotoUrl((String)snapshot.get("photoUrl"));
                    searchChatData(data,friendEmail,currentEmail);
                }

            }
        });
    }

    private void searchChatData(final PersonalChatDTO data, final String friendEmail, final String currentEmail) {
        String path = currentEmail+"And"+friendEmail;
        firestore.collection(PERSONAL_CHAT).document(path)
                .collection(CHAT_DATA)
                .orderBy("time", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<PersonalChatData> chatArrayList = new ArrayList<>();
                        if (task.isSuccessful() && task.getResult() != null){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                PersonalChatData chatData = new PersonalChatData();
                                chatData.setEmail((String)document.get("email"));
                                chatData.setMessage((String)document.get("message"));
                                chatData.setTime((long)document.get("time"));
                                chatArrayList.add(chatData);
                            }
                            if (chatArrayList.size() != 0){
                                int size = chatArrayList.size() - 1;
                                data.setMessage(chatArrayList.get(size).getMessage());
                                data.setTime(chatArrayList.get(size).getTime());
                                chatDataArrayList.add(data);
                                searchCount++;
                                searchAllData();
                            }else {
                                String path = friendEmail+"And"+currentEmail;
                                reSearchChatData(path,data);
                            }
                            
                        }
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("Michael","chat OnResume");
        searchData();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("Michael","chat onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("Michael","chat onStop");
    }

    private void reSearchChatData(String path, final PersonalChatDTO data) {
        firestore.collection(PERSONAL_CHAT).document(path)
                .collection(CHAT_DATA)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<PersonalChatData> chatArrayList = new ArrayList<>();
                        if (task.isSuccessful() && task.getResult() != null){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                PersonalChatData chatData = new PersonalChatData();
                                chatData.setEmail((String)document.get("email"));
                                chatData.setMessage((String)document.get("message"));
                                chatData.setTime((long)document.get("time"));
                                chatArrayList.add(chatData);
                            }
                            if (chatArrayList.size() != 0){
                                int size = chatArrayList.size() - 1;
                                data.setMessage(chatArrayList.get(size).getMessage());
                                data.setTime(chatArrayList.get(size).getTime());
                                chatDataArrayList.add(data);
                                searchCount++;
                                searchAllData();
                            }else {
                                data.setMessage("");
                                data.setTime(0);
                                chatDataArrayList.add(data);
                                searchCount++;
                                searchAllData();
                            }

                        }
                    }
                });
    }
}
