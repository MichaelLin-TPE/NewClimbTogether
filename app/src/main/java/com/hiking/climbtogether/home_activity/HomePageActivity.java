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

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
import com.hiking.climbtogether.MainActivity;
import com.hiking.climbtogether.R;
import com.hiking.climbtogether.hms_tools.CipherUtil;
import com.hiking.climbtogether.hms_tools.Key;
import com.hiking.climbtogether.member_activity.MemberActivity;
import com.google.android.material.tabs.TabLayout;
import com.hiking.climbtogether.tool.GoogleUpdater;
import com.huawei.hianalytics.hms.HiAnalyticsTools;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.analytics.HiAnalytics;
import com.huawei.hms.analytics.HiAnalyticsInstance;
import com.huawei.hms.iap.Iap;
import com.huawei.hms.iap.IapApiException;
import com.huawei.hms.iap.IapClient;
import com.huawei.hms.iap.entity.ConsumeOwnedPurchaseReq;
import com.huawei.hms.iap.entity.ConsumeOwnedPurchaseResult;
import com.huawei.hms.iap.entity.InAppPurchaseData;
import com.huawei.hms.iap.entity.IsEnvReadyResult;
import com.huawei.hms.iap.entity.IsSandboxActivatedReq;
import com.huawei.hms.iap.entity.IsSandboxActivatedResult;
import com.huawei.hms.iap.entity.OrderStatusCode;
import com.huawei.hms.iap.entity.ProductInfo;
import com.huawei.hms.iap.entity.ProductInfoReq;
import com.huawei.hms.iap.entity.ProductInfoResult;
import com.huawei.hms.iap.entity.PurchaseIntentReq;
import com.huawei.hms.iap.entity.PurchaseIntentResult;
import com.huawei.hms.iap.entity.PurchaseResultInfo;
import com.huawei.hms.iap.util.IapClientHelper;
import com.huawei.hms.support.api.client.AidlApiClient;
import com.huawei.hms.support.api.client.InnerApiClient;
import com.huawei.hms.support.api.client.Status;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.huawei.hms.analytics.type.HAEventType.ADDPRODUCT2WISHLIST;
import static com.huawei.hms.analytics.type.HAParamType.CATEGORY;
import static com.huawei.hms.analytics.type.HAParamType.CURRNAME;
import static com.huawei.hms.analytics.type.HAParamType.PLACEID;
import static com.huawei.hms.analytics.type.HAParamType.PRICE;
import static com.huawei.hms.analytics.type.HAParamType.PRODUCTID;
import static com.huawei.hms.analytics.type.HAParamType.PRODUCTNAME;
import static com.huawei.hms.analytics.type.HAParamType.QUANTITY;
import static com.huawei.hms.analytics.type.HAParamType.REVENUE;

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

    private IapClient iapClient;

    private HiAnalyticsInstance instance;

    private static final int REQ_CODE_BUY = 0;

    private static final int SUBSCRIPTION = 2;

    private static final int REQUEST_CODE = 666;


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

        homePresenter.onCheckGoogleUpdateVersion();


        //HMS 分析系統
        HiAnalyticsTools.enableLog();
        instance = HiAnalytics.getInstance(this);
        //傳送事件 給HMS分析系統
        Bundle bundle = new Bundle();
        bundle.putString("exam_difficulty","high");
        bundle.putString("exam_level","1-1");
        bundle.putString("exam_time","20190520-08");
        bundle.putString(PRODUCTID,"123");
        bundle.putString(PRODUCTNAME,"Michael");
        bundle.putString(CATEGORY,"human");
        bundle.putLong(QUANTITY,1000);
        bundle.putLong(PRICE,20000);
        bundle.putDouble(REVENUE,10);
        bundle.putString(CURRNAME,"TWD");
        bundle.putString(PLACEID,"locationID");
        instance.onEvent("begin_examination",bundle);

        //預置事件打點

    }

    private void hmsInAppPurchase() {
        IapClient client = Iap.getIapClient(this);
        Task<IsSandboxActivatedResult> sandTask = client.isSandboxActivated(new IsSandboxActivatedReq());
        sandTask.addOnSuccessListener(new OnSuccessListener<IsSandboxActivatedResult>() {
            @Override
            public void onSuccess(IsSandboxActivatedResult isSandboxActivatedResult) {

                if (isSandboxActivatedResult.getIsSandboxApk()){

                    Task<IsEnvReadyResult> task = Iap.getIapClient(HomePageActivity.this).isEnvReady();
                    task.addOnSuccessListener(new OnSuccessListener<IsEnvReadyResult>() {
                        @Override
                        public void onSuccess(IsEnvReadyResult isEnvReadyResult) {
                            if (isEnvReadyResult.getReturnCode() == 0){
                                Log.i("Michael","沙盒測試環境成功"+" , 是否滿足沙盒條件 : "+isSandboxActivatedResult.getIsSandboxApk()+ " , 是否為沙盒帳號 : "+isSandboxActivatedResult.getIsSandboxUser()+" , 沙盒通關碼 : "+isSandboxActivatedResult.getReturnCode()+" , 市場版本 : "+isSandboxActivatedResult.getVersionFrMarket()+" , Status : "+isSandboxActivatedResult.getStatus());
                                ArrayList<String> productIdArray = new ArrayList<>();
                                productIdArray.add("drink");
                                ProductInfoReq req = new ProductInfoReq();
                                /**
                                 * priceType: 0: consumable; 1: non-consumable; 2: auto-renewable subscription
                                 */
                                req.setPriceType(SUBSCRIPTION);
                                req.setProductIds(productIdArray);

                                Task<ProductInfoResult> task = Iap.getIapClient(HomePageActivity.this).obtainProductInfo(req);
                                task.addOnSuccessListener(new OnSuccessListener<ProductInfoResult>() {
                                    @Override
                                    public void onSuccess(ProductInfoResult result) {
                                        if (result != null) {
                                            List<ProductInfo> productList = result.getProductInfoList();

                                            String[] name = new String[productList.size()];
                                            for (int i = 0; i < productList.size(); i++) {
                                                name[i] = productList.get(i).getProductName();
                                            }

                                            AlertDialog dialog = new AlertDialog.Builder(HomePageActivity.this)
                                                    .setItems(name, new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            if (which == 0) {
                                                                PurchaseIntentReq purchase = new PurchaseIntentReq();
                                                                purchase.setProductId(productList.get(which).getProductId());
                                                                purchase.setPriceType(IapClient.PriceType.IN_APP_SUBSCRIPTION);
                                                                purchase.setDeveloperPayload("test");
                                                                Activity activity = HomePageActivity.this;
                                                                Task<PurchaseIntentResult> task = Iap.getIapClient(activity).createPurchaseIntent(purchase);
                                                                task.addOnSuccessListener(new OnSuccessListener<PurchaseIntentResult>() {
                                                                    @Override
                                                                    public void onSuccess(PurchaseIntentResult result) {

                                                                        if (result != null) {
                                                                            Status status = result.getStatus();
                                                                            if (status.hasResolution()) {
                                                                                try {
                                                                                    status.startResolutionForResult(HomePageActivity.this, REQUEST_CODE);
                                                                                } catch (IntentSender.SendIntentException e) {
                                                                                    e.printStackTrace();
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(Exception e) {
                                                                        if (e instanceof IapApiException) {
                                                                            IapApiException apiException = (IapApiException) e;
                                                                            Status status = apiException.getStatus();
                                                                            int returnCode = apiException.getStatusCode();
                                                                            Log.i("Michael", "Iap 錯誤 status : " + status + " , returnCode : " + returnCode);
                                                                        } else {
                                                                            Log.i("Michael", "Iap 其他錯誤 : " + e.toString());
                                                                            // Other external errors
                                                                        }
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    }).create();
                                            dialog.show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception e) {
                                        if (e instanceof IapApiException) {
                                            IapApiException apiException = (IapApiException) e;
                                            int returnCode = apiException.getStatusCode();
                                            Log.i("Michael", "IAP 錯誤 : " + returnCode);
                                        } else {
                                            Log.i("Michael", "其他錯誤 : " + e.toString());
                                        }
                                    }
                                });


                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Log.i("Michael","購買啟動失敗 : "+e.toString());
                        }
                    });






                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.e("Michael", "isSandboxActivated fail");
                if (e instanceof IapApiException) {
                    IapApiException apiException = (IapApiException)e;
                    int returnCode = apiException.getStatusCode();
                    Log.i("Michael","沙盒測試錯誤 : "+returnCode);
                } else {
                    // Other external errors
                    Log.i("Michael","沙盒測試其他錯誤 : "+e.toString());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_CODE) {
            if (data == null) {
                Log.i("Michael", "ActivityResult data == null");
                return;
            }
            PurchaseResultInfo purchaseResultInfo = Iap.getIapClient(HomePageActivity.this).parsePurchaseResultInfoFromIntent(data);
            switch (purchaseResultInfo.getReturnCode()) {
                case OrderStatusCode.ORDER_STATE_CANCEL:
                    Log.i("Michael", "使用者自行取消");
                    break;
                case OrderStatusCode.ORDER_STATE_FAILED:
                case OrderStatusCode.ORDER_PRODUCT_OWNED:
                    Log.i("Michael", "商品已擁有");
                    break;
                case OrderStatusCode.ORDER_STATE_SUCCESS:
                    String inAppPurchaseData = purchaseResultInfo.getInAppPurchaseData();
                    Log.i("Michael","購買成功後的 Json : "+inAppPurchaseData);
                    String purchaseToken = "";
                    Log.i("Michae;","確認交易");

//                    try{
//                        InAppPurchaseData inAppPurchaseDataBean = new InAppPurchaseData(inAppPurchaseData);
//                        purchaseToken = inAppPurchaseDataBean.getPurchaseToken();
//                    }catch (Exception e){
//                        Log.i("Michael","錯誤 : "+e.toString());
//                    }
//                    ConsumeOwnedPurchaseReq req = new ConsumeOwnedPurchaseReq();
//                    req.setPurchaseToken(purchaseToken);
//
//
//                    Task<ConsumeOwnedPurchaseResult> task = Iap.getIapClient(HomePageActivity.this).consumeOwnedPurchase(req);
//                    task.addOnSuccessListener(new OnSuccessListener<ConsumeOwnedPurchaseResult>() {
//                        @Override
//                        public void onSuccess(ConsumeOwnedPurchaseResult consumeOwnedPurchaseResult) {
//                            Log.i("Michael", "consumeOwnedPurchase success");
//                            Toast.makeText(HomePageActivity.this, "Pay success, and the product has been delivered", Toast.LENGTH_SHORT).show();
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(Exception e) {
//                            if (e instanceof IapApiException) {
//                                IapApiException apiException = (IapApiException) e;
//                                Status status = apiException.getStatus();
//                                int returnCode = apiException.getStatusCode();
//                                Log.i("Michael", "Iap 錯誤 status : " + status + " , returnCode : " + returnCode);
//                            } else {
//                                Log.i("Michael", "Iap 其他錯誤 : " + e.toString());
//                                // Other external errors
//                            }
//                        }
//                    });
                    break;
                default:
                    break;
            }
        }



    }

    private void showProductList(List<ProductInfo> productInfoList) {

        String[] productId = new String[productInfoList.size()];

        for (int i = 0; i < productInfoList.size(); i++) {
            productId[i] = productInfoList.get(i).getProductName();
            Log.i("Michael", "productName : " + productInfoList.get(i).getProductName());
        }
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setItems(productId, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            String productId = productInfoList.get(0).getProductId();
                            gotoPay(HomePageActivity.this, productId, IapClient.PriceType.IN_APP_CONSUMABLE);
                        }
                    }
                }).create();
        dialog.show();


        //這邊是顯示RecyclerView的地方
        //假的 要刪掉
        viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void gotoPay(Activity activity, String productId, int type) {
        IapClient mClient = Iap.getIapClient(activity);
        Task<PurchaseIntentResult> task = mClient.createPurchaseIntent(createProductInfoReq(type, productId));
        task.addOnSuccessListener(new OnSuccessListener<PurchaseIntentResult>() {
            @Override
            public void onSuccess(PurchaseIntentResult result) {
                if (result == null) {
                    Log.i("Michael", "result == null");
                    return;
                }
                Status status = result.getStatus();
                if (status == null) {
                    Log.i("Michael", "status == null");
                    return;
                }

                if (status.hasResolution()) {
                    try {
                        status.startResolutionForResult(activity, REQ_CODE_BUY);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.i("Michael", e.getMessage());
                Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                if (e instanceof IapApiException) {
                    IapApiException apiException = (IapApiException) e;
                    int returnCode = apiException.getStatusCode();
                    Log.i("Michael", "createPurchaseIntent, returnCode: " + returnCode);
                    // handle error scenarios
                } else {
                    // Other external errors
                }
            }
        });
    }

    private PurchaseIntentReq createProductInfoReq(int type, String productId) {
        PurchaseIntentReq req = new PurchaseIntentReq();
        req.setProductId(productId);
        req.setPriceType(type);
        req.setDeveloperPayload("test");
        return req;
    }

    private ProductInfoReq createProductInfoReq() {

        ProductInfoReq req = new ProductInfoReq();
        // In-app product type contains:
        // 0: consumable
        // 1: non-consumable
        // 2: auto-renewable subscription
        req.setPriceType(IapClient.PriceType.IN_APP_CONSUMABLE);
        ArrayList<String> productIds = new ArrayList<>();
        productIds.add("drink");
        req.setProductIds(productIds);
        return req;
    }

    //接資料




    private void consumeOwnedPurchase(HomePageActivity homePageActivity, String inAppPurchaseData) {

        IapClient client = Iap.getIapClient(homePageActivity);
        Task<ConsumeOwnedPurchaseResult> task = client.consumeOwnedPurchase(createConsumeOwnedPurchaseReq(inAppPurchaseData));
        task.addOnSuccessListener(new OnSuccessListener<ConsumeOwnedPurchaseResult>() {
            @Override
            public void onSuccess(ConsumeOwnedPurchaseResult consumeOwnedPurchaseResult) {
                Log.i("Michael", "consumeOwnedPurchase success");
                Toast.makeText(homePageActivity, "Pay success, and the product has been delivered", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.i("Michael", e.getMessage());
                Toast.makeText(homePageActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
                if (e instanceof IapApiException) {
                    IapApiException apiException = (IapApiException) e;
                    Status status = apiException.getStatus();
                    int returnCode = apiException.getStatusCode();
                    Log.e("Michael", "consumeOwnedPurchase fail,returnCode: " + returnCode);
                } else {
                    // Other external errors
                }
            }
        });
    }

    private ConsumeOwnedPurchaseReq createConsumeOwnedPurchaseReq(String inAppPurchaseData) {
        ConsumeOwnedPurchaseReq req = new ConsumeOwnedPurchaseReq();
        try {
            InAppPurchaseData data = new InAppPurchaseData(inAppPurchaseData);
            req.setPurchaseToken(data.getPurchaseToken());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return req;
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

        hmsInAppPurchase();


//        ArrayList<String> paymentList = new ArrayList<>();
//        paymentList.add(getString(R.string.single_buy));
//        paymentList.add(getString(R.string.loop_buy));
//        String[] list = paymentList.toArray(new String[0]);
//        AlertDialog dialog = new AlertDialog.Builder(this)
//                .setItems(list, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (which == 0) {
//                            skuType = BillingClient.SkuType.INAPP;
//                        } else {
//                            skuType = BillingClient.SkuType.SUBS;
//                        }
//                        homePresenter.onSelectItemClickListener(which);
//                        dialog.dismiss();
//                    }
//                }).create();
//        dialog.show();

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
                    Log.i("Michael", "消耗商品 : " + consumeImmediately);

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
                                            if (skuType.equals(BillingClient.SkuType.INAPP)) {
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
                            Toast.makeText(HomePageActivity.this, "沒關係唷~下次下次", Toast.LENGTH_LONG).show();
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

    public String getVersionCode() {
        PackageManager pm = getPackageManager();
        PackageInfo info = null;
        try {
            info = pm.getPackageInfo(getPackageName(), 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return info != null ? info.versionName + "" : "";
    }

    @Override
    public void checkGoogleUpdate() {
        GoogleUpdater updater = new GoogleUpdater();
        updater.execute();
        updater.setOnCheckUpdateListener(new GoogleUpdater.OnCheckUpdateListener() {
            @Override
            public void onSuccess(String result) {

                double lastV = Double.parseDouble(result);
                double currentV = Double.parseDouble(getVersionCode());
                Log.i("Michael", "lastVersion : " + lastV + " ,currentVersion : " + currentV);
                if (currentV < lastV) {
                    homePresenter.onShowUpdateDialog();
                }
            }

            @Override
            public void onFail(String errorCode) {
                Log.i("Michael", "取得GOOLE PLAY 資訊錯誤 : " + errorCode);
            }
        });
    }

    @Override
    public void showUpdateDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.need_to_update))
                .setMessage(getString(R.string.search_a_new_version))
                .setPositiveButton(getString(R.string.go_to_update), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        homePresenter.onUpdateConfirmClickListener();
                    }
                }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        dialog.show();
    }

    @Override
    public void intentToGooglePlay() {
        Intent it = new Intent(Intent.ACTION_VIEW);
        it.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.hiking.climbtogether"));
        startActivity(it);
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
