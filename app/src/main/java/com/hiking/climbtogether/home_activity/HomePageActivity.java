package com.hiking.climbtogether.home_activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryRecord;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.api.Billing;
import com.hiking.climbtogether.R;
import com.hiking.climbtogether.member_activity.MemberActivity;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity implements HomePageVu {

    private TabLayout tabLayout;

    private ViewPager viewPager;

    private ImageView ivTabIcon;

    private HomePagePresenter homePresenter;

    private FragmentManager fragmentManager;

    private BillingClient billingClient;

    private String drinkPrice = "";

    private static final int CONSUME_DELAY = 1;

    private int consumeImmediately = 0;

    private Handler handler = new Handler();

    private AlertDialog paymentDialog;

    private String skuType;

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
                }).setNegativeButton(getString(R.string.donate), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        homePresenter.onDonateClickListener();

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
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"go.hiking.together@gmail.com"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.question_report));
            emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);
            startActivity(emailIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void checkGooglePlayAccount() {
        Log.i("Michael", "贊助");
        ArrayList<String> paymentList = new ArrayList<>();
        paymentList.add(getString(R.string.single_buy));
        paymentList.add(getString(R.string.loop_buy));
        String[] list = paymentList.toArray(new String[0]);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setItems(list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            skuType = BillingClient.SkuType.INAPP;
                        }else {
                            skuType = BillingClient.SkuType.SUBS;
                        }
                        homePresenter.onSelectItemClickListener(which);
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();

    }

    //消耗商品
    private void consumePurchase(Purchase data, int state) {
        Log.i("Michael", "消耗商品");
        ConsumeParams.Builder consumeParams = ConsumeParams.newBuilder();
        consumeParams.setPurchaseToken(data.getPurchaseToken());
        consumeParams.setDeveloperPayload(data.getDeveloperPayload());
        billingClient.consumeAsync(consumeParams.build(), new ConsumeResponseListener() {
            @Override
            public void onConsumeResponse(BillingResult billingResult, String s) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    if (state == consumeImmediately) {
                        Log.i("Michael", "消耗商品 : " + consumeImmediately);
                    }
                } else {
                    //如果消耗不成功 那就在消耗一次
                    Log.i("Michael", "再次消耗一次");
                    if (state == CONSUME_DELAY && billingResult.getDebugMessage().contains("Server error,please try again")) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                queryAndConsumePurchaseSingle();
                            }
                        }, 5 * 1000);
                    }
                }
            }
        });


    }

    private void queryAndConsumePurchaseSingle() {
        //queryPurchases() 方法会使用 Google Play 商店应用的缓存，而不会发起网络请求
        billingClient.queryPurchaseHistoryAsync(skuType,
                new PurchaseHistoryResponseListener() {

                    @Override
                    public void onPurchaseHistoryResponse(BillingResult billingResult, List<PurchaseHistoryRecord> list) {
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
                            for (PurchaseHistoryRecord record : list) {
                                //Process the result
                                //確認購買交易 不然三天後會退款給用戶
                                try {
                                    Purchase purchase = new Purchase(record.getOriginalJson(), record.getSignature());
                                    if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                                        //消耗品開始消耗
                                        consumePurchase(purchase, consumeImmediately);
                                        //確認購買交易
                                        if (!purchase.isAcknowledged()) {
                                            acknowledgePurchase(purchase);
                                        }
                                        //TODO: 這裡可以添加訂單找回功能,防止變態用戶付完錢就殺死App的那種
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
    }

    //確認訂單
    private void acknowledgePurchase(Purchase data) {
        AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(data.getPurchaseToken())
                .build();
        AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener = new AcknowledgePurchaseResponseListener() {
            @Override
            public void onAcknowledgePurchaseResponse(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    paymentDialog.dismiss();
                    Toast.makeText(HomePageActivity.this, "山岳感謝您~", Toast.LENGTH_SHORT).show();
                    Log.i("Michael", "Acknowledge purchase success");
                } else {
                    Log.i("Michael", "Acknowledge purchase fail");
                }
            }
        };
        billingClient.acknowledgePurchase(acknowledgePurchaseParams, acknowledgePurchaseResponseListener);
    }

    //單次購買
    @Override
    public void showSingleDonateDialog() {

        billingClient = BillingClient.newBuilder(this)
                .enablePendingPurchases()
                .setListener(new PurchasesUpdatedListener() {
                    @Override
                    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> list) {

                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
                            for (Purchase data : list) {
                                if (data.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                                    Log.i("Michael", "Purchase success");
                                    if (!data.isAcknowledged()) {
                                        acknowledgePurchase(data);
                                    }

                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            consumePurchase(data, CONSUME_DELAY);
                                        }
                                    }, 2000);
                                    //TODO:發放商品
                                } else if (data.getPurchaseState() == Purchase.PurchaseState.PENDING) {

                                }
                            }
                        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
                            Log.i("Michael", "使用者取消");
                        } else if (list == null) {
                            Log.i("Michael", "支付錯誤 : " + billingResult.getResponseCode() + " , list == null");
                        } else {
                            Log.i("Michael", "支付錯誤 : " + billingResult.getResponseCode());
                        }
                    }
                }).build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    Log.i("Michael", "startConnection : " + BillingClient.BillingResponseCode.OK);
                    ArrayList<String> skuList = new ArrayList<>();
                    skuList.add("treat_a_box");
                    skuList.add("buy_me_a_drink");

                    SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                    params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
                    billingClient.querySkuDetailsAsync(params.build(), new SkuDetailsResponseListener() {
                        @Override
                        public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> list) {
                            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {

                                //列出清單
                                View view = View.inflate(HomePageActivity.this, R.layout.payment_view, null);
                                RecyclerView rvPay = view.findViewById(R.id.payment_recycler_view);
                                rvPay.setLayoutManager(new LinearLayoutManager(HomePageActivity.this));

                                PaymentAdapter paymentAdapter = new PaymentAdapter(list, HomePageActivity.this);
                                rvPay.setAdapter(paymentAdapter);

                                paymentDialog = new AlertDialog.Builder(HomePageActivity.this)
                                        .setView(view).create();
                                paymentDialog.show();


                                paymentAdapter.setOnPaymentItemClickListener(new PaymentAdapter.OnPaymentItemClickListener() {
                                    @Override
                                    public void onPay(SkuDetails data) {

                                        BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                                .setSkuDetails(data)
                                                .build();
                                        billingClient.launchBillingFlow(HomePageActivity.this, flowParams);
                                    }
                                });

                            }
                        }
                    });
                } else {
                    Toast.makeText(HomePageActivity.this, "購買頁錯誤 : " + billingResult.getResponseCode(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                Log.i("Michael", "BillingServiceDisconnected");

            }
        });


    }

    //訂閱
    @Override
    public void showLoopDonateDialog() {
        billingClient = BillingClient.newBuilder(this)
                .enablePendingPurchases()
                .setListener(new PurchasesUpdatedListener() {
                    @Override
                    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> list) {

                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
                            for (Purchase data : list) {
                                if (data.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                                    Log.i("Michael", "Purchase success");
                                    if (!data.isAcknowledged()) {
                                        acknowledgePurchase(data);
                                    }

                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (skuType.equals(BillingClient.SkuType.INAPP)){
                                                consumePurchase(data, CONSUME_DELAY);
                                            }

                                        }
                                    }, 2000);
                                    //TODO:發放商品
                                } else if (data.getPurchaseState() == Purchase.PurchaseState.PENDING) {

                                }
                            }
                        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
                            Log.i("Michael", "使用者取消");
                            Toast.makeText(HomePageActivity.this,"沒關係唷~下次下次",Toast.LENGTH_LONG).show();
                        } else if (list == null) {
                            Log.i("Michael", "支付錯誤 : " + billingResult.getResponseCode() + " , list == null");
                        } else {
                            Log.i("Michael", "支付錯誤 : " + billingResult.getResponseCode());
                        }
                    }
                }).build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    Log.i("Michael", "startConnection : " + BillingClient.BillingResponseCode.OK);
                    ArrayList<String> skuList = new ArrayList<>();
                    //商品ID
                    skuList.add("give_me_money_every_month");
                    skuList.add("treat_me_one_year");
                    SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                    //訂閱
                    params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);

                    billingClient.querySkuDetailsAsync(params.build(), new SkuDetailsResponseListener() {
                        @Override
                        public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> list) {
                            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {

                                //列出清單
                                View view = View.inflate(HomePageActivity.this, R.layout.payment_view, null);
                                RecyclerView rvPay = view.findViewById(R.id.payment_recycler_view);
                                rvPay.setLayoutManager(new LinearLayoutManager(HomePageActivity.this));

                                PaymentAdapter paymentAdapter = new PaymentAdapter(list, HomePageActivity.this);
                                rvPay.setAdapter(paymentAdapter);

                                paymentDialog = new AlertDialog.Builder(HomePageActivity.this)
                                        .setView(view).create();
                                paymentDialog.show();


                                paymentAdapter.setOnPaymentItemClickListener(new PaymentAdapter.OnPaymentItemClickListener() {
                                    @Override
                                    public void onPay(SkuDetails data) {

                                        BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                                .setSkuDetails(data)
                                                .build();
                                        billingClient.launchBillingFlow(HomePageActivity.this, flowParams);
                                    }
                                });

                            }
                        }
                    });
                } else {
                    Toast.makeText(HomePageActivity.this, "購買頁錯誤 : " + billingResult.getResponseCode(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                Log.i("Michael", "BillingServiceDisconnected");

            }
        });
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
