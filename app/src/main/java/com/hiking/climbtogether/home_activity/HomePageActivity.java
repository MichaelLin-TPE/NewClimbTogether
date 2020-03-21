package com.hiking.climbtogether.home_activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hiking.climbtogether.R;
import com.hiking.climbtogether.member_activity.MemberActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class HomePageActivity extends AppCompatActivity implements HomePageVu {

    private TabLayout tabLayout;

    private ViewPager viewPager;

    private ImageView ivTabIcon;

    private HomePagePresenter homePresenter;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        fragmentManager = getSupportFragmentManager();
        initPresenter();
        initActionBar();
        initView();
        homePresenter.onPrepareData();
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.member_icon:
                homePresenter.onMemberIconClickListener();
                break;
            case R.id.question:
                homePresenter.onQuestionButtonClickListener();
                break;
            default:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initActionBar() {
        Toolbar toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
    }

    private void initPresenter() {
        homePresenter = new HomePagePresenterImpl(this);
    }

    private void initView() {
        tabLayout = findViewById(R.id.home_tab_layout);
        viewPager = findViewById(R.id.home_view_pager);
    }

    @Override
    public Context getVuContext() {
        return this;
    }

    @Override
    public void showBottomTabLayout(final ArrayList<String> tabTitleArray, final ArrayList<Drawable> notPressIconArray, final ArrayList<Drawable> pressedIconArray) {
        tabLayout.removeAllTabs();
        for (int i = 0; i < tabTitleArray.size(); i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setCustomView(prepareView(tabTitleArray.get(i), notPressIconArray.get(i)));
            tab.setTag(tabTitleArray.get(i));
            tabLayout.addTab(tab);
        }
        TabLayout.Tab firstTab = tabLayout.getTabAt(0);
        if (firstTab != null) {
            if (firstTab.getCustomView() != null) {
                ivTabIcon = firstTab.getCustomView().findViewById(R.id.bottom_tab_icon);
                ivTabIcon.setImageDrawable(pressedIconArray.get(0));
                firstTab.select();
            }
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.i("Michael", "我點擊的位置 : " + tab.getPosition());
                int position = tab.getPosition();
                TabLayout.Tab singleTab = tabLayout.getTabAt(position);
                if (singleTab != null) {
                    if (singleTab.getCustomView() != null) {
                        ivTabIcon = singleTab.getCustomView().findViewById(R.id.bottom_tab_icon);
                        ivTabIcon.setImageDrawable(pressedIconArray.get(position));
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                TabLayout.Tab singleTab = tabLayout.getTabAt(position);
                if (singleTab != null) {
                    if (singleTab.getCustomView() != null) {
                        ivTabIcon = singleTab.getCustomView().findViewById(R.id.bottom_tab_icon);
                        ivTabIcon.setImageDrawable(notPressIconArray.get(position));
                    }
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        homePresenter.onPrepareViewPager();

    }

    @Override
    public void showViewPager() {
        HomeViewPagerAdapter viewPagerAdapter = new HomeViewPagerAdapter(fragmentManager);
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void intentToMemberActivity() {
        Intent it = new Intent(this, MemberActivity.class);
        startActivity(it);
    }

    @Override
    public void showQuestionDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.free_title)
                .setMessage(getString(R.string.free_content))
                .setPositiveButton(getString(R.string.contact_me), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        homePresenter.onContactMeButtonClickListener();
                    }
                }).setNegativeButton(getString(R.string.i_know), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        dialog.show();
    }

    @Override
    public void contactMe() {
        try {
            String emailBody = getString(R.string.email_body);
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_EMAIL,new String[]{"go.hiking.together@gmail.com"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.question_report));
            emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);
            startActivity(emailIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private View prepareView(String title, Drawable icon) {
        View view = View.inflate(this, R.layout.home_bottom_tablayout_custom_view, null);
        TextView tvTabTitle = view.findViewById(R.id.bottom_tab_title);
        ivTabIcon = view.findViewById(R.id.bottom_tab_icon);
        tvTabTitle.setText(title);
        ivTabIcon.setImageDrawable(icon);
        return view;


    }
}
