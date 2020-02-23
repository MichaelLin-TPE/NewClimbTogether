package com.example.climbtogether.disscuss_fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.climbtogether.R;
import com.example.climbtogether.chat_activity.ChatActivity;
import com.example.climbtogether.login_activity.LoginActivity;
import com.example.climbtogether.share_activity.ShareActivity;
import com.example.climbtogether.tool.SpaceItemDecoration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nostra13.universalimageloader.utils.L;

import java.util.ArrayList;


public class DiscussFragment extends Fragment implements DiscussFragmentVu {

    private DiscussFragmentPresenter presenter;

    private FirebaseAuth mAuth;

    private FirebaseFirestore firestore;

    private Context context;

    private RecyclerView recyclerView;

    private DiscussFragmentAdapter adapter;

    private FirebaseUser user;

    private TextView tvNotice;

    private ProgressBar progressBar;

    private Button btnLogin;
    private ImageView ivIcon;

    private static final String DISCUSSION = "discussion";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public static DiscussFragment newInstance() {
        DiscussFragment fragment = new DiscussFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
        initFirebase();
        checkLoginStatus();
    }

    @Override
    public void onStart() {
        super.onStart();
        checkLoginStatus();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onClearView();
        presenter.onSearchFirestoreData();
    }

    private void checkLoginStatus() {
        user = mAuth.getCurrentUser();
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    private void initPresenter() {
        presenter = new DiscussFragmentPresenterImpl(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discuss, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {
        progressBar = view.findViewById(R.id.discuss_fragment_progressbar);

        recyclerView = view.findViewById(R.id.discuss_fragment_recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        int pix = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());

        recyclerView.addItemDecoration(new SpaceItemDecoration(pix));

        tvNotice = view.findViewById(R.id.discuss_fragment_text_notice);
        btnLogin = view.findViewById(R.id.discuss_fragment_btn_login);
        ivIcon = view.findViewById(R.id.discuss_fragment_iv_icon);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onBtnLoginClickListener();
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new DiscussFragmentAdapter(context);
        presenter.onSearchFirestoreData();

    }

    @Override
    public void searchFirestoreData() {
        if (user != null){
            presenter.onShowProgressbar();
            final ArrayList<String> listArrayList = new ArrayList<>();
            firestore.collection(DISCUSSION).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                if (task.getResult() != null){
                                    for (QueryDocumentSnapshot document : task.getResult()){
                                        listArrayList.add(document.getId());
                                    }
                                    presenter.onCatchDataSuccessful(listArrayList);
                                }
                            }
                        }
                    });
        }else {
            presenter.onNotLoginEvent();
        }
    }

    @Override
    public void setViewMaintain(boolean isShow) {
        tvNotice.setVisibility(isShow ? View.VISIBLE : View.GONE);
        ivIcon.setVisibility(isShow ? View.VISIBLE : View.GONE);
        btnLogin.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void intentToLoginActivity() {
        Intent it = new Intent(context, LoginActivity.class);
        context.startActivity(it);
    }

    @Override
    public void setRecyclerView(final ArrayList<String> listArrayList) {
        adapter.setData(listArrayList);
        recyclerView.setAdapter(adapter);
        adapter.setOnDisCussItemClickListener(new DiscussFragmentAdapter.OnDiscussItemClickListener() {
            @Override
            public void onClick(String listName) {
                presenter.onDiscussItemClickListener(listName);
            }
        });
    }

    @Override
    public void showProgressbar(boolean isShow) {
        progressBar.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void clearView() {
        adapter.setData(new ArrayList<String>());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void intentToChatActivity(String listName) {
        Intent it = new Intent(context, ChatActivity.class);
        it.putExtra("listName",listName);
        startActivity(it);
    }

    @Override
    public void intentToShareActivity(String listName) {
        Intent it = new Intent(context, ShareActivity.class);
        startActivity(it);
    }
}
